package net.dragonegg.sculkcatalyticchamber.events;

import net.dragonegg.sculkcatalyticchamber.content.chamber.ChamberBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import static net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.FORGE)
public class CommonEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (state.getBlock() instanceof ChamberBlock<?>) {
            ChamberBlock.breakMultiblock(state, level, pos, event.getPlayer());
        }
    }

}
