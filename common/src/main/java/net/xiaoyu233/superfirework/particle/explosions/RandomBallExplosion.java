package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class RandomBallExplosion extends FireworkExplosion {
    private final double randomFactor;

    public RandomBallExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 2d : speed, size, config, explosionTag);
        if (explosionTag.contains("RandomFactor", NbtElement.DOUBLE_TYPE)){
            this.randomFactor = explosionTag.getDouble("RandomFactor");
        }else this.randomFactor = 1.0d;

    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {

        for (int i = -size; i <= size; i++) {

            for (int j = -size; j <= size; j++) {

                for (int k = -size; k <= size; k++) {
                    double randSpeed = random.nextInt(6) / 5d * this.randomFactor;
                    //The larger the value, the mess the generated particles be
                    double randomizeAmount = 0.5D;
                    double d3 = ((double) j + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d4 = ((double) i + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d5 = ((double) k + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d6 = (double) MathHelper.sqrt((float) (d3 * d3 + d4 * d4 + d5 * d5)) / (speed * randSpeed) + this.random.nextGaussian() * 0.05d;
                    this.createParticle(x, y, z, d3 / d6, d4 / d6, d5 / d6);

                    if (i != -size && i != size && j != -size && j != size) {
                        k += size * 2 - 1;
                    }
                }
            }
        }
    }
}
