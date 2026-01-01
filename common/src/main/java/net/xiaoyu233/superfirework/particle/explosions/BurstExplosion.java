package net.xiaoyu233.superfirework.particle.explosions;

import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class BurstExplosion extends SimpleExplosion{

    public BurstExplosion(ExplosionType<EmptyFireworkComponent, BurstExplosion> type) {
        super(type);
    }

    /**
     * Creates a burst type explosion effect.
     */
    private void createBurst(double speed, int size, double x, double y, double z, ParticleConfig config) {
        var velocityX = parentVec.x;
        var velocityZ = parentVec.z;
        var velocityY = parentVec.y;
        double d0 = this.random.nextGaussian() * 0.05D;
        double d1 = this.random.nextGaussian() * 0.05D;

        for (int i = 0; i < 70; ++i) {
            double d2 = velocityX * 0.5D + this.random.nextGaussian() * 0.15D + d0;
            double d3 = velocityZ * 0.5D + this.random.nextGaussian() * 0.15D + d1;
            double d4 = velocityY * 0.5D + this.random.nextDouble() * 0.5D;
            this.createParticle(speed, size, x, y, z, d2, d4, d3, config);
        }
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        createBurst(speed, size, x, y, z, config);
    }
}
