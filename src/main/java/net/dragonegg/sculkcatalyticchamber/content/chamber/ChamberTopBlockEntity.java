package net.dragonegg.sculkcatalyticchamber.content.chamber;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ChamberTopBlockEntity extends ChamberBlockEntity {

    public ChamberTopBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setCapabilities();
    }

    @Override
    public Optional<ChamberOperatingBlockEntity> getOperator() {
        if (level == null) return Optional.empty();
        BlockEntity be = level.getBlockEntity(worldPosition.above(2));
        if (be instanceof ChamberOperatingBlockEntity operator)
            return Optional.of(operator);
        return Optional.empty();
    }

    @Override
    public ChamberTopBlockEntity getTop() {
        return this;
    }

    @Override
    public ChamberMiddleBlockEntity getMiddle() {
        BlockEntity be = level.getBlockEntity(getBlockPos().below(1));
        if (be instanceof ChamberMiddleBlockEntity middle) {
            return middle;
        }
        return null;
    }

    @Override
    public ChamberBottomBlockEntity getBottom() {
        BlockEntity be = level.getBlockEntity(getBlockPos().below(2));
        if (be instanceof ChamberBottomBlockEntity bottom) {
            return bottom;
        }
        return null;
    }


}
