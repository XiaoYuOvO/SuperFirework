package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

import java.util.function.Consumer;


public class BallExplosion extends SimpleExplosion {
    private final double speedMultiplier;

    public BallExplosion(ExplosionType<EmptyFireworkComponent, ? extends BallExplosion> type) {
        this(type, 1.0D);
    }

    public BallExplosion(ExplosionType<EmptyFireworkComponent, ? extends BallExplosion> type, double speedMultiplier) {
        super(type);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        createBall(x, y, z, size, speed, config);
    }

    protected void createBall(double x, double y, double z, int size, double speed, ParticleConfig config){
        createBall(x, y, z, size, speed * speedMultiplier, config, (p)->{});
    }
    /**
     * Creates a small ball or large ball type explosion effect.
     */
    protected void createBall(double x, double y, double z, int size, double speed, ParticleConfig config, Consumer<SuperFireworkParticle.Explosion> particleConfig)
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
                    particleConfig.accept(this.createParticle(speed, size, x, y, z, d3 / d6, d4 / d6, d5 / d6, config));

                    if (i != -size && i != size && j != -size && j != size)
                    {
                        k += size * 2 - 1;
                    }
                }
            }
        }
    }

}
