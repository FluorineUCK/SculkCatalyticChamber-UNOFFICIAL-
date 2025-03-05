package net.dragonegg.sculkcatalyticchamber.content.chamber;

import net.dragonegg.sculkcatalyticchamber.registry.BlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class ChamberTopBlock extends ChamberBlock<ChamberTopBlockEntity> {

    public ChamberTopBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int ordinal() {
        return 2;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState p_199600_1_, BlockGetter p_199600_2_, BlockPos p_199600_3_) {
        return Block.box(0.0F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F);
    }

    @Override
    public Class<ChamberTopBlockEntity> getBlockEntityClass() {
        return ChamberTopBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChamberTopBlockEntity> getBlockEntityType() {
        return BlockRegistry.CHAMBER_TOP_BLOCK_TILE.get();
    }
}
