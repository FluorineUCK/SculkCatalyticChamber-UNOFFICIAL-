package net.dragonegg.sculkcatalyticchamber.content.shrieker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;
import static net.dragonegg.sculkcatalyticchamber.registry.PartialModelRegistry.MECHANICAL_SHRIEKER_COG;

public class MechanicalShriekerRender extends KineticBlockEntityRenderer<MechanicalShriekerBlockEntity> {

    public MechanicalShriekerRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(MechanicalShriekerBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacingVertical(MECHANICAL_SHRIEKER_COG, state, state.getValue(FACING));
    }

}
