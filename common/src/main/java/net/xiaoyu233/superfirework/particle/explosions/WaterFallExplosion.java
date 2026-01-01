package net.xiaoyu233.superfirework.particle.explosions;

import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class WaterFallExplosion extends SimpleExplosion {

    public WaterFallExplosion(ExplosionType<EmptyFireworkComponent, WaterFallExplosion> type) {
        super(type);
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        createWaterFall(speed, size, x, y, z, config);
    }

    private void createWaterFall(double speed, int size, double x, double y, double z, ParticleConfig config) {
        for (int i = 0; i < size; ++i) {
            double d2 = this.random.nextGaussian() * 0.15D * speed;
            double d3 = this.random.nextGaussian() * 0.15D * speed;
            double d4 = -0.5D + this.random.nextDouble() * -0.25D;
            this.createParticle(speed, size, x, y, z, d2, d4, d3, config);
        }
    }
}
