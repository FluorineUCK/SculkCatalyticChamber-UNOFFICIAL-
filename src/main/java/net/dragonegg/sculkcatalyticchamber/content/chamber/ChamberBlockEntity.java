package net.dragonegg.sculkcatalyticchamber.content.chamber;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

import static net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber.MODID;

public abstract class ChamberBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public ChamberInventory inputInventory;
    public SmartFluidTankBehaviour inputTank;
    protected boolean contentsChanged;

    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;

    int recipeBackupCheck;

    public ChamberBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new ChamberInventory(9, this);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        contentsChanged = true;
        recipeBackupCheck = 20;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 8000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        behaviours.add(inputTank);
    }

    protected void setCapabilities() {
        itemCapability = LazyOptional.of(this::getInvs);
        fluidCapability = LazyOptional.of(this::getTanks);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("InputItems", inputInventory.serializeNBT());
    }

    @Override
    public void remove() {
        super.remove();
        onEmptied();
    }

    public void onEmptied() {
        getOperator().ifPresent(be -> be.chamberRemoved = true);
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if (!level.isClientSide) {
            if (recipeBackupCheck-- > 0)
                return;
            recipeBackupCheck = 20;
            if (isEmpty())
                return;
            notifyChangeOfContents();
        }
    }

    public void scheduleChangeOfContents() {
        if (!contentsChanged)
            return;

        contentsChanged = false;
        getOperator().ifPresent(be -> be.chamberChecker.scheduleUpdate());
    }

    public boolean canContinueProcessing() {
        ChamberBottomBlockEntity bottom = getBottom();
        return bottom != null && bottom.isBufferEmpty();
    }

    public abstract Optional<ChamberOperatingBlockEntity> getOperator();

    public void notifyChangeOfContents() {
        contentsChanged = true;
    }

    public boolean isEmpty() {
        return inputInventory.isEmpty() && inputTank.isEmpty();
    }

    public FilteringBehaviour getFilter() {
        ChamberBottomBlockEntity bottom = getBottom();
        return bottom != null? bottom.filtering : null;
    }

    public SmartInventory getInputInventory() {
        return inputInventory;
    }

    public IItemHandlerModifiable getInvs() {
        return inputInventory;
    }

    public IFluidHandler getTanks() {
        return inputTank.getCapability().orElse(null);
    }

    public abstract ChamberTopBlockEntity getTop();

    public abstract ChamberMiddleBlockEntity getMiddle();

    public abstract ChamberBottomBlockEntity getBottom();

    protected abstract NonNullList<Ingredient> ingredients(Recipe<?> recipe);

    protected InteractionResult use(Level worldIn, BlockPos pos, Player player, InteractionHand handIn) {
        ItemStack heldItem = player.getItemInHand(handIn);
        SmartInventory inv = getInputInventory();
        if (!heldItem.isEmpty()) {
            if (FluidHelper.tryEmptyItemIntoBE(worldIn, player, handIn, heldItem, this)) {
                return InteractionResult.SUCCESS;
            } else if (FluidHelper.tryFillItemFromBE(worldIn, player, handIn, heldItem, this)) {
                return InteractionResult.SUCCESS;
            } else if (GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)
                    || GenericItemFilling.canItemBeFilled(worldIn, heldItem)) {
                return InteractionResult.SUCCESS;
            } else {
                boolean canInsert = !(heldItem.getItem() instanceof WrenchItem);
                if (getOperator().isPresent()) {
                    ChamberOperatingBlockEntity operator = getOperator().get();
                    canInsert = operator.getAllRecipes().stream()
                            .map(this::ingredients)
                            .flatMap(List::stream)
                            .anyMatch(ingredient -> ingredient.test(heldItem));
                }
                if (canInsert) {
                    for (int slot = 0; slot < inv.getSlots(); ++slot) {
                        ItemStack remain = inv.insertItem(slot, heldItem, false);
                        if (remain.getCount() == heldItem.getCount()) continue;
                        player.setItemInHand(handIn, remain);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        } else if (emptyInv(inv, player)) {
            worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    protected boolean emptyInv(SmartInventory inv, Player player) {
        boolean success = false;

        for(int slot = 0; slot < inv.getSlots(); ++slot) {
            ItemStack stackInSlot = inv.getStackInSlot(slot);
            if (!stackInSlot.isEmpty()) {
                player.getInventory().placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
                success = true;
            }
        }

        onEmptied();
        return success;
    }

    // client things

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.builder(MODID).translate("gui.goggles.chamber_contents")
                .forGoggles(tooltip);

        IItemHandlerModifiable items = itemCapability.orElse(new ItemStackHandler());
        IFluidHandler fluids = fluidCapability.orElse(new FluidTank(0));
        boolean isEmpty = true;

        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            Lang.text("")
                    .add(Components.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(Lang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            Lang.text("")
                    .add(Lang.fluidName(fluidStack)
                            .add(Lang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(Lang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        if (isEmpty)
            tooltip.remove(0);

        return true;
    }

}
