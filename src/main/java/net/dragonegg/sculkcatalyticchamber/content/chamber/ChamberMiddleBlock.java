package net.dragonegg.sculkcatalyticchamber.content.chamber;

import net.dragonegg.sculkcatalyticchamber.registry.BlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChamberMiddleBlock extends ChamberBlock<ChamberMiddleBlockEntity> {

    public ChamberMiddleBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int ordinal() {
        return 1;
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
