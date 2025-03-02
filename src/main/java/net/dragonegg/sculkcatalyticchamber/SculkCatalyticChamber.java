package net.dragonegg.sculkcatalyticchamber;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.dragonegg.sculkcatalyticchamber.registry.BlockRegistry;
import net.dragonegg.sculkcatalyticchamber.registry.CreativeModTabRegistry;
import net.dragonegg.sculkcatalyticchamber.registry.PartialModelRegistry;
import net.dragonegg.sculkcatalyticchamber.registry.RecipeRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SculkCatalyticChamber.MODID)
public class SculkCatalyticChamber {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sculkcatalyticchamber";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public SculkCatalyticChamber() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);

        CreativeModTabRegistry.register(modEventBus);
        BlockRegistry.register();
        RecipeRegistry.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                modEventBus.addListener(SculkCatalyticChamber::clientInit));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        // Some client setup code
        PartialModelRegistry.init();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
