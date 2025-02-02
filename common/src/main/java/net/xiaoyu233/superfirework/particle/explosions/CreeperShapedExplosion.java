package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class CreeperShapedExplosion extends ShapedExplosion{
    public CreeperShapedExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 0.5d : speed, size, config, explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        this.createShaped(x, y, z, CREEPER_SHAPE, true, true);
    }
}
