package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class BurstExplosion extends FireworkExplosion{
    private final double velocityX;
    private final double velocityZ;
    private final double velocityY;

    public BurstExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed, size, config, explosionTag);
        this.velocityX = parentVec.x;
        this.velocityZ = parentVec.z;
        this.velocityY = parentVec.y;
    }

    /**
     * Creates a burst type explosion effect.
     */
    private void createBurst(double x, double y, double z) {
        double d0 = this.random.nextGaussian() * 0.05D;
        double d1 = this.random.nextGaussian() * 0.05D;

        for (int i = 0; i < 70; ++i) {
            double d2 = this.velocityX * 0.5D + this.random.nextGaussian() * 0.15D + d0;
            double d3 = this.velocityZ * 0.5D + this.random.nextGaussian() * 0.15D + d1;
            double d4 = this.velocityY * 0.5D + this.random.nextDouble() * 0.5D;
            this.createParticle(x, y, z, d2, d4, d3);
        }
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        createBurst(x, y, z);
    }
}
