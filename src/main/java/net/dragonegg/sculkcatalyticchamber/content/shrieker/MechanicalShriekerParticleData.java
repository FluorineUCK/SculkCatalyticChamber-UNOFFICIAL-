package net.dragonegg.sculkcatalyticchamber.content.shrieker;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.dragonegg.sculkcatalyticchamber.registry.ParticleTypeRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class MechanicalShriekerParticleData implements
        ParticleOptions, ICustomParticleDataWithSprite<MechanicalShriekerParticleData> {

    public static final Codec<MechanicalShriekerParticleData> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                    Codec.INT.fieldOf("delay").forGetter(p -> p.delay),
                    Codec.INT.fieldOf("ordinal").forGetter(p -> p.ordinal())
            ).apply(i, MechanicalShriekerParticleData::new));

    public static final Deserializer<MechanicalShriekerParticleData> DESERIALIZER = new Deserializer<>() {
        public MechanicalShriekerParticleData fromCommand(ParticleType<MechanicalShriekerParticleData> particleTypeIn, StringReader reader)
                throws CommandSyntaxException {
            reader.expect(' ');
            int delay = reader.readInt();
            reader.expect(' ');
            int ordinal = reader.readInt();
            return new MechanicalShriekerParticleData(delay, ordinal);
        }

        public MechanicalShriekerParticleData fromNetwork(ParticleType<MechanicalShriekerParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new MechanicalShriekerParticleData(buffer.readInt(), buffer.readInt());
        }
    };

    public static final Supplier<MechanicalShriekerParticleData> FACTORY = MechanicalShriekerParticleData::new;

    private final int delay;
    private final Direction direction;

    public MechanicalShriekerParticleData(int delay, Direction direction) {
        this.delay = delay;
        this.direction = direction;
    }

    public MechanicalShriekerParticleData(int delay, int ordinal) {
        this(delay, Direction.values()[ordinal]);
    }

    public MechanicalShriekerParticleData() {
        this(0, Direction.UP);
    }

    public int getDelay() {
        return delay;
    }

    public Direction getDirection() {
        return direction;
    }

    public int ordinal() {
        return direction.ordinal();
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypeRegistry.MECHANICAL_SHRIEKER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(delay);
        buffer.writeInt(ordinal());
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d", "mechanical_shrieker", delay, ordinal());
    }

    @Override
    public Deserializer<MechanicalShriekerParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<MechanicalShriekerParticleData> getCodec(ParticleType<MechanicalShriekerParticleData> type) {
        return CODEC;
    }

    @Override
    public ParticleEngine.SpriteParticleRegistration<MechanicalShriekerParticleData> getMetaFactory() {
        return MechanicalShriekerParticle.Factory::new;
    }
}
