package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class WaterFallExplosion extends FireworkExplosion {
    public WaterFallExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed, size, config, explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        createWaterFall(x, y, z);
    }

    private void createWaterFall(double x, double y, double z) {
        for (int i = 0; i < size; ++i) {
            double d2 = this.random.nextGaussian() * 0.15D * speed;
            double d3 = this.random.nextGaussian() * 0.15D * speed;
            double d4 = -0.5D + this.random.nextDouble() * -0.25D;
            this.createParticle(x, y, z, d2, d4, d3);
        }
    }
}
