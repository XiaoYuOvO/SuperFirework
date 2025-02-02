package net.xiaoyu233.superfirework.particle;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.util.FireworkUtil;
import net.xiaoyu233.superfirework.util.NbtUtil;

import java.util.Arrays;
import java.util.Objects;

public final class ParticleConfig {
    public boolean trail;
    public boolean flicker;
    public int[] colors;
    public int[] fadeColor;
    public volatile boolean explode;
    public DistributionConfig gravity;
    public DistributionConfig maxAge;

    private ParticleConfig(boolean trail, boolean flicker, boolean explode, int[] colors, int[] fadeColor, DistributionConfig gravity, DistributionConfig maxAge) {
        this.trail = trail;
        this.flicker = flicker;
        this.colors = colors;
        this.explode = explode;
        this.fadeColor = fadeColor;
        this.gravity = gravity;
        this.maxAge = maxAge;
    }

    public ParticleConfig(NbtCompound compound, Random random) {
        this(
                compound.getBoolean("Trail"),
                compound.getBoolean("Flicker"),
                compound.getBoolean("Explode"),
                NbtUtil.getColor(compound, "Colors").orElseGet(() -> FireworkUtil.getRandomSingleColor(random)),
                NbtUtil.getColor(compound, "FadeColors").map(fade -> NbtUtil.ensureColor(fade, random)).orElse(new int[0]),
                new DistributionConfig(compound.getCompound("Gravity"), 0.1,0),
                new DistributionConfig(compound.getCompound("MaxAge"), 54, 6d));
    }

    public boolean trail() {
        return trail;
    }

    public boolean flicker() {
        return flicker;
    }

    public int[] color() {
        return colors;
    }

    public int[] fadeColor() {
        return fadeColor;
    }

    public DistributionConfig gravity() {
        return gravity;
    }

    public DistributionConfig maxAge() {
        return maxAge;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ParticleConfig) obj;
        return this.trail == that.trail &&
                this.flicker == that.flicker &&
                Arrays.equals(this.colors, that.colors) &&
                Arrays.equals(this.fadeColor, that.fadeColor) &&
                Objects.equals(this.gravity, that.gravity) &&
                Objects.equals(this.maxAge, that.maxAge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trail, flicker, Arrays.hashCode(colors), Arrays.hashCode(fadeColor), gravity, maxAge);
    }

    @Override
    public String toString() {
        return "ParticleConfig[" +
                "trail=" + trail + ", " +
                "flicker=" + flicker + ", " +
                "color=" + Arrays.toString(colors) + ", " +
                "fadeColor=" + Arrays.toString(fadeColor) + ", " +
                "gravity=" + gravity + ", " +
                "maxAge=" + maxAge + ']';
    }

}
