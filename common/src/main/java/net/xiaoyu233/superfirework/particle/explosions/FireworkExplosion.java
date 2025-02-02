package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

public abstract class FireworkExplosion {
   protected final ParticleManager particleManager;
   protected final Random random;
   protected final double speed;
   protected final int size;
   protected final ParticleConfig particleConfig;
   protected final NbtCompound explosionTag;
   protected final Vec3d parentVec;

    protected FireworkExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        this.particleManager = particleManager;
        this.random = random;
        this.speed = speed;
        this.parentVec = parentVec;
        this.size = size;
        this.particleConfig = config;
        this.explosionTag = explosionTag;
    }

    public abstract void spawnFireworkParticles(double x, double y, double z);
    /**
     * Creates a single particle.
     */
    protected SuperFireworkParticle.Explosion createParticle(double x, double y, double z, double motionX, double motionY, double motionZ) {
        SuperFireworkParticle.Explosion explosion = (SuperFireworkParticle.Explosion) particleManager.addParticle(SFParticleTypes.SUPER_FIREWORK.get(), x, y, z, motionX, motionY, motionZ);
        explosion.setTrail(particleConfig.trail);
        explosion.setFlicker(particleConfig.flicker);
        explosion.setAlpha(0.99F);
        explosion.setMaxAge((int) particleConfig.maxAge.sample(random));
        explosion.setColor(Util.getRandom(particleConfig.colors, random));
        explosion.setGravityStrength((float) particleConfig.gravity.sample(random));
        if (particleConfig.fadeColor.length > 0) {
            explosion.setTargetColor(Util.getRandom(particleConfig.fadeColor, random));
        }
        if (particleConfig.explode){
            explosion.onDead((p)-> {
                this.particleConfig.explode = false;
                if (particleConfig.fadeColor.length > 0) {
                    int[] colors = this.particleConfig.colors;
                    this.particleConfig.colors = particleConfig.fadeColor;
                    this.particleConfig.fadeColor = colors;
                }
                for (int i = 0; i < this.size; i++) {
                    this.createParticle(p.getX(), p.getY(), p.getZ(), random.nextGaussian() * this.speed * 0.05d + p.getVecX(), random.nextGaussian() * this.speed  * 0.05d + p.getVecY(), random.nextGaussian() * this.speed * 0.05d + p.getVecZ());
                }
            });
        }
        return explosion;
    }
}
