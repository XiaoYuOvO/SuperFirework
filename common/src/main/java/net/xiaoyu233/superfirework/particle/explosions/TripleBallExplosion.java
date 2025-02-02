package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class TripleBallExplosion extends BallExplosion{
    public TripleBallExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 2d : speed, size, config, explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        this.createBall(x, y, z,speed);
        this.createBall(x, y, z,speed / 2);
        this.createBall(x, y, z,speed / 4);
    }
}
