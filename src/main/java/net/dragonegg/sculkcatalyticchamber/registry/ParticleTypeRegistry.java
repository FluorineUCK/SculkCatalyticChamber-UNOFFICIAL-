package net.dragonegg.sculkcatalyticchamber.registry;

import net.dragonegg.sculkcatalyticchamber.content.shrieker.MechanicalShriekerParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber.MODID;
import static net.dragonegg.sculkcatalyticchamber.content.shrieker.MechanicalShriekerParticleData.FACTORY;

public class ParticleTypeRegistry {

    private static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static RegistryObject<ParticleType<MechanicalShriekerParticleData>> MECHANICAL_SHRIEKER =
            REGISTER.register("mechanical_shrieker", () -> FACTORY.get().createType());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        FACTORY.get().register(MECHANICAL_SHRIEKER.get(), event);
    }

}
