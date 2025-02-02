package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

import java.util.function.Consumer;


public class BallExplosion extends FireworkExplosion {

    public BallExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 0.25d : speed, size, config, explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        createBall(x, y, z, speed);
    }

    protected void createBall(double x, double y, double z, double speed){
        createBall(x, y, z, speed, (p)->{});
    }
    /**
     * Creates a small ball or large ball type explosion effect.
     */
    protected void createBall(double x, double y, double z, double speed, Consumer<SuperFireworkParticle.Explosion> particleConfig)
    {
        for (int i = -size; i <= size; i ++)
        {
            for (int j = -size; j <= size; j ++)
            {
                for (int k = -size; k <= size; k ++)
                {
                    //The larger the value, the mess the generated particles be
                    double randomizeAmount = 0.5D;

                    double d3 = ((double)j + (random.nextDouble() - random.nextDouble()) * randomizeAmount);
                    double d4 = ((double)i + (random.nextDouble() - random.nextDouble()) * randomizeAmount);
                    double d5 = ((double)k + (random.nextDouble() - random.nextDouble()) * randomizeAmount);
                    double d6 = (double) MathHelper.sqrt((float) (d3 * d3 + d4 * d4 + d5 * d5)) / speed + random.nextGaussian() * 0.05d;
                    particleConfig.accept(this.createParticle(x, y, z, d3 / d6, d4 / d6, d5 / d6));

                    if (i != -size && i != size && j != -size && j != size)
                    {
                        k += size * 2 - 1;
                    }
                }
            }
        }
    }

}
