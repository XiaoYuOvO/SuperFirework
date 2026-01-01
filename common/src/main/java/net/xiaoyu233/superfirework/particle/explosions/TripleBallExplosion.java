package net.xiaoyu233.superfirework.particle.explosions;

import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class TripleBallExplosion extends BallExplosion{
    public TripleBallExplosion(ExplosionType<EmptyFireworkComponent, TripleBallExplosion> type) {
        super(type);
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        this.createBall(x, y, z, size,speed, config);
        this.createBall(x, y, z, size,speed / 2, config);
        this.createBall(x, y, z, size,speed / 4, config);
    }
}
