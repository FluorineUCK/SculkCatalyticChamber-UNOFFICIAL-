package net.dragonegg.sculkcatalyticchamber.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber.MODID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CreativeModTabRegistry {

    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Components.translatable("itemGroup." + MODID))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
                    .icon(Items.ECHO_SHARD::getDefaultInstance)
                    .displayItems((para, out) -> {
                        out.accept(BlockRegistry.CHAMBER_BOTTOM_BLOCK.asItem());
                        out.accept(BlockRegistry.MECHANICAL_SHRIEKER_BLOCK.asItem());
                    })
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

}
