package net.xiaoyu233.superfirework.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.network.SFPacketCodecs;
import net.xiaoyu233.superfirework.util.FireworkUtil;
import net.xiaoyu233.superfirework.util.NbtUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record ParticleConfig(
    boolean trail,
    boolean flicker,
    boolean explode,
    IntList colors,
    IntList fadeColors,
    DistributionConfig gravity,
    DistributionConfig maxAge
) {
    public static final ParticleConfig DEFAULT = new ParticleConfig(false, false, false, IntList.of(), IntList.of(), new DistributionConfig(0.1, 0), new DistributionConfig(54, 6d));

    // 主要的 CODEC 定义
    public static final Codec<ParticleConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("trail", false).forGetter(ParticleConfig::trail),
                    Codec.BOOL.optionalFieldOf("flicker", false).forGetter(ParticleConfig::flicker),
                    Codec.BOOL.optionalFieldOf("explode", false).forGetter(config -> config.explode),
                    FireworkExplosionComponent.COLORS_CODEC.optionalFieldOf("colors", IntList.of()).forGetter(ParticleConfig::colors),
                    FireworkExplosionComponent.COLORS_CODEC.optionalFieldOf("fade_colors", IntList.of()).forGetter(ParticleConfig::fadeColors),
                    DistributionConfig.CODEC.optionalFieldOf("gravity", new DistributionConfig(0.1, 0)).forGetter(ParticleConfig::gravity),
                    DistributionConfig.CODEC.optionalFieldOf("max_age", new DistributionConfig(54, 6d)).forGetter(ParticleConfig::maxAge)
            ).apply(instance, ParticleConfig::new)
    );

    private static final PacketCodec<ByteBuf, IntList> COLORS_PACKET_CODEC = PacketCodecs.INTEGER
            .collect(PacketCodecs.toList())
            .xmap(IntArrayList::new, ArrayList::new);
    public static final PacketCodec<ByteBuf, ParticleConfig> PACKET_CODEC =
            SFPacketCodecs.tuple(
                    PacketCodecs.BOOL, // trail
                    ParticleConfig::trail,
                    PacketCodecs.BOOL, // flicker
                    ParticleConfig::flicker,
                    PacketCodecs.BOOL, // explode
                    ParticleConfig::explode,
                    COLORS_PACKET_CODEC, // colors
                    ParticleConfig::colors,
                    COLORS_PACKET_CODEC, // fadeColors
                    ParticleConfig::fadeColors,
                    DistributionConfig.PACKET_CODEC, // gravity
                    ParticleConfig::gravity,
                    DistributionConfig.PACKET_CODEC, // maxAge
                    ParticleConfig::maxAge,
                    ParticleConfig::new
            );

    public ParticleConfig {
        colors = IntLists.unmodifiable(colors);
        fadeColors = IntLists.unmodifiable(fadeColors);
    }

    public ParticleConfig(NbtCompound compound, Random random) {
        this(
                compound.getBoolean("Trail"),
                compound.getBoolean("Flicker"),
                compound.getBoolean("Explode"),
                NbtUtil.getColor(compound, "Colors").orElseGet(() -> FireworkUtil.getRandomSingleColor(random)),
                NbtUtil.getColor(compound, "FadeColors").map(fade -> NbtUtil.ensureColor(fade, random)).orElse(IntList.of()),
                new DistributionConfig(compound.getCompound("Gravity"), 0.1, 0),
                new DistributionConfig(compound.getCompound("MaxAge"), 54, 6d)
        );
    }

    @Contract(" -> new")
    public @NotNull ParticleConfig cancelExplodeClone(){
        if (!this.fadeColors.isEmpty())
            return new ParticleConfig(trail, flicker, false, fadeColors, colors, gravity, maxAge);
        return new ParticleConfig(trail, flicker, false, colors, fadeColors, gravity, maxAge);
    }

    public ParticleConfig withColorsClone(IntList colors, IntList fadeColor){
        return new ParticleConfig(trail, flicker, explode, colors, fadeColor, gravity, maxAge);
    }
}
