package net.xiaoyu233.superfirework.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.util.NbtUtil;

public record DistributionConfig(double mean, double deviation) {

    public static final Codec<DistributionConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE.optionalFieldOf("m", 0d).forGetter(DistributionConfig::mean),
                    Codec.DOUBLE.optionalFieldOf("d", 1.0d).forGetter(DistributionConfig::deviation)
            ).apply(instance, DistributionConfig::new)
    );

    // DistributionConfig 的 PACKET_CODEC 定义
    public static final PacketCodec<ByteBuf, DistributionConfig> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, // mean
                    DistributionConfig::mean,
                    PacketCodecs.DOUBLE, // deviation
                    DistributionConfig::deviation,
                    DistributionConfig::new
            );

    public DistributionConfig(NbtCompound compound){
        this(NbtUtil.getDouble(compound,"m").orElse(0d), NbtUtil.getDouble(compound, "d").orElse(1.0d));
    }

    public DistributionConfig(NbtCompound compound, double defaultM, double defaultD){
        this(NbtUtil.getDouble(compound,"m").orElse(defaultM), NbtUtil.getDouble(compound, "d").orElse(defaultD));
    }

    public double sample(Random random){
        return random.nextGaussian() * deviation + mean;
    }
}
