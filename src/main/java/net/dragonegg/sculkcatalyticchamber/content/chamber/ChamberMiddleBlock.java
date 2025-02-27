package net.dragonegg.sculkcatalyticchamber.content.chamber;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.dragonegg.sculkcatalyticchamber.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ChamberMiddleBlock extends Block implements IChamberBE<ChamberMiddleBlockEntity>, IWrenchable {
    public ChamberMiddleBlock(Properties pProperties) {
        super(pProperties);
    }

    private boolean hasBottom(BlockGetter level, BlockPos pos) {
        BlockState p = level.getBlockState(pos.below(1));
        return p.getBlock() instanceof ChamberBottomBlock;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.hasBottom(level, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.interact(level, pos, player, hand, hit);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
                                  LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return !canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player pPlayer) {
        super.playerWillDestroy(level, pos, state, pPlayer);
        if (this.hasBottom(level, pos)) {
            level.destroyBlock(pos.below(1), !pPlayer.isCreative());
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return this.hasBottom(level, pos)? BlockRegistry.CHAMBER_BOTTOM_BLOCK.asStack() : ItemStack.EMPTY;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return this.getBlockEntityOptional(worldIn, pos)
                .map(be -> be.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .map(ItemHelper::calcRedstoneFromInventory).orElse(0)).orElse(0);
    }

    @Override
    public Class<ChamberMiddleBlockEntity> getBlockEntityClass() {
        return ChamberMiddleBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChamberMiddleBlockEntity> getBlockEntityType() {
        return BlockRegistry.CHAMBER_MIDDLE_BLOCK_TILE.get();
    }
}
