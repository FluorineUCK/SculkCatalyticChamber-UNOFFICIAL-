package net.dragonegg.sculkcatalyticchamber.content.chamber;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberBottomBlockEntity.OUTPUT_ANIMATION_TIME;

public class ChamberBottomRenderer extends SmartBlockEntityRenderer<ChamberBottomBlockEntity> {

    public ChamberBottomRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(ChamberBottomBlockEntity chamber, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(chamber, partialTicks, ms, buffer, light, overlay);

        BlockState blockState = chamber.getBlockState();
        if (!(blockState.getBlock() instanceof ChamberBottomBlock))
            return;
        Direction direction = blockState.getValue(ChamberBottomBlock.FACING);
        if (direction == Direction.DOWN)
            return;
        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = VecHelper.getCenterOf(BlockPos.ZERO)
                .add(directionVec.scale(.55).subtract(0, 1 / 2f, 0));

        boolean outToBasin = chamber.getLevel().
                getBlockState(chamber.getBlockPos().relative(direction).below())
                .getBlock() instanceof BasinBlock;

        for (IntAttached<ItemStack> intAttached : chamber.visualizedOutputItems) {
            float progress = 1 - (intAttached.getFirst() - partialTicks) / OUTPUT_ANIMATION_TIME;

            if (!outToBasin && progress > .35f)
                continue;

            ms.pushPose();
            TransformStack.cast(ms)
                    .translate(outVec)
                    .translate(new Vec3(0, Math.max(-.55f, -(progress * progress * 2)), 0))
                    .translate(directionVec.scale(progress * .5f))
                    .rotateY(AngleHelper.horizontalAngle(direction))
                    .rotateX(progress * 180);
            renderItem(ms, buffer, light, overlay, intAttached.getValue());
            ms.popPose();
        }
    }

    protected void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer()
                .renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, mc.level, 0);
    }

    @Override
    public int getViewDistance() {
        return 16;
    }
}
