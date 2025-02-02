package net.xiaoyu233.superfirework.particle;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.util.NbtUtil;

public record DistributionConfig(double mean, double deviation) {
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
