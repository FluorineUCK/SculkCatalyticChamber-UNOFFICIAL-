package net.dragonegg.sculkcatalyticchamber.registry;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class ArmInteractionPointTypeRegistry {

    public static final ChamberType CHAMBER = register("chamber", ChamberType::new);

    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(Create.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static void register() {}

    public static class ChamberType extends ArmInteractionPointType {
        public ChamberType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return ChamberBlock.isChamber(level, pos);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

}
