package net.dragonegg.sculkcatalyticchamber.content.chamber;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.dragonegg.sculkcatalyticchamber.registry.BlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public abstract class ChamberBlock<T extends ChamberBlockEntity> extends Block implements IBE<T>, IWrenchable {

    public ChamberBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.onBlockEntityUse(worldIn, pos, be -> be.use(worldIn, pos, player, handIn));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return ordinal() == 0 || level.getBlockState(pos.below(ordinal())).getBlock() instanceof ChamberBottomBlock;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        for (int i = 0; i < 3; i++) {
            BlockPos posI = pos.above(i - ordinal());
            BlockState stateI = level.getBlockState(posI);
            if (stateI.getBlock() instanceof ChamberBlock<?>) {
                level.destroyBlock(posI, true);
            }
        }
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        for (int i = 0; i < 3; i++) {
            BlockPos posI = pos.above(i - ordinal());
            BlockState stateI = level.getBlockState(posI);
            if (stateI.getBlock() instanceof ChamberBlock<?> chamber) {
                chamber.wasExploded(level, posI, explosion);
            }
            level.setBlock(posI, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (!(world instanceof ServerLevel serverLevel))
            return InteractionResult.SUCCESS;

        for (int i = 0; i < 3; i++) {
            BlockPos posI = pos.above(i - ordinal());
            BlockState stateI = world.getBlockState(posI);
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, posI, stateI, player);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled())
                return InteractionResult.SUCCESS;

            if (player != null && !player.isCreative()) {
                Block.getDrops(stateI, serverLevel, posI, world.getBlockEntity(posI), player, context.getItemInHand())
                        .forEach(itemStack -> player.getInventory().placeItemBackInInventory(itemStack));
            }
            stateI.spawnAfterBreak(serverLevel, posI, ItemStack.EMPTY, true);
            world.destroyBlock(posI, false);
        }

        playRemoveSound(world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return BlockRegistry.CHAMBER_BOTTOM_BLOCK.asStack();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return this.getBlockEntityOptional(worldIn, pos)
                .map(ChamberBlockEntity::getInputInventory)
                .map(ItemHelper::calcRedstoneFromInventory).orElse(0);
    }

    protected abstract int ordinal();

}
