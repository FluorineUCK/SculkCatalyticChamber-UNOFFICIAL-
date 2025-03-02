package net.dragonegg.sculkcatalyticchamber.content.shrieker;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;
import static net.dragonegg.sculkcatalyticchamber.registry.PartialModelRegistry.MECHANICAL_SHRIEKER_COG;

public class MechanicalShriekerCogInstance extends SingleRotatingInstance<MechanicalShriekerBlockEntity> implements DynamicInstance {

    public MechanicalShriekerCogInstance(MaterialManager materialManager, MechanicalShriekerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    public void beginFrame() {}

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(FACING);
        return getRotatingMaterial().getModel(MECHANICAL_SHRIEKER_COG, referenceState, facing, () -> {
            PoseStack poseStack = new PoseStack();
            TransformStack.cast(poseStack)
                    .centre()
                    .rotateToFace(facing)
                    .multiply(Axis.XN.rotationDegrees(90))
                    .unCentre();
            return poseStack;
        });
    }
}
