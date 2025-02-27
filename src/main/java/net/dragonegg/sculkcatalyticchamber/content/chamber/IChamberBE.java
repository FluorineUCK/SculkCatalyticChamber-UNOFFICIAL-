package net.dragonegg.sculkcatalyticchamber.content.chamber;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IChamberBE<T extends ChamberBlockEntity> extends IBE<T> {

    default InteractionResult interact(Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        return this.onBlockEntityUse(worldIn, pos, (be) -> {
            SmartInventory inv = be.getInputInventory();
            if (!heldItem.isEmpty()) {
                if (FluidHelper.tryEmptyItemIntoBE(worldIn, player, handIn, heldItem, be)) {
                    return InteractionResult.SUCCESS;
                } else if (FluidHelper.tryFillItemFromBE(worldIn, player, handIn, heldItem, be)) {
                    return InteractionResult.SUCCESS;
                } else if (!GenericItemEmptying.canItemBeEmptied(worldIn, heldItem) && !GenericItemFilling.canItemBeFilled(worldIn, heldItem)) {
                    return heldItem.getItem().equals(Items.SPONGE) && !be.getCapability(ForgeCapabilities.FLUID_HANDLER)
                            .map((iFluidHandler) -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE))
                            .orElse(FluidStack.EMPTY).isEmpty() ? InteractionResult.SUCCESS : InteractionResult.PASS;
                } else {
                    for (int slot = 0; slot < inv.getSlots(); ++slot) {
                        ItemStack remain = inv.insertItem(slot, heldItem, false);
                        if (remain.getCount() == heldItem.getCount()) continue;
                        player.setItemInHand(handIn, remain);
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }
            } else {
                boolean success = false;

                for(int slot = 0; slot < inv.getSlots(); ++slot) {
                    ItemStack stackInSlot = inv.getStackInSlot(slot);
                    if (!stackInSlot.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(stackInSlot);
                        inv.setStackInSlot(slot, ItemStack.EMPTY);
                        success = true;
                    }
                }

                if (success) {
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
                }

                return InteractionResult.SUCCESS;
            }
        });
    }

}
