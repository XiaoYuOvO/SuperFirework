package net.xiaoyu233.superfirework.particle.explosions;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

public abstract class FireworkExplosion<C> {
    public static final Codec<FireworkExplosion<?>> CODEC = ExplosionType.CODEC.dispatch(FireworkExplosion::getType, ExplosionType::getCodec);
    public static final PacketCodec<ByteBuf,FireworkExplosion<?>> PACKET_CODEC = PacketCodecs.codec(CODEC);
    protected final ExplosionType<C, ? extends FireworkExplosion<C>> type;
    protected final C component;

    protected ParticleManager particleManager;
    protected Random random;
    protected Vec3d parentVec;

    protected FireworkExplosion(ExplosionType<C, ? extends FireworkExplosion<C>> type, C component) {
        this.type = type;
        this.component = component;
    }
    public void doExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, double x, double y, double z, ParticleConfig config){
        this.particleManager = particleManager;
        this.random = random;
        this.parentVec = parentVec;
        this.spawnExplosionParticles(speed, size, x, y, z, config);
    }

    protected abstract void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config);
    /**
     * Creates a single particle.
     */
    protected SuperFireworkParticle.Explosion createParticle(double speed, int size, double x, double y, double z, double motionX, double motionY, double motionZ,
                                                             ParticleConfig particleConfig) {
        SuperFireworkParticle.Explosion explosion = (SuperFireworkParticle.Explosion) particleManager.addParticle(SFParticleTypes.SUPER_FIREWORK.get(), x, y, z, motionX, motionY, motionZ);
        explosion.setTrail(particleConfig.trail());
        explosion.setFlicker(particleConfig.flicker());
        explosion.setAlpha(0.99F);
        explosion.setMaxAge(Math.abs((int) particleConfig.maxAge().sample(random)));
        explosion.setColor(Util.getRandom(particleConfig.colors(), random));
        explosion.setGravityStrength(Math.abs((float) particleConfig.gravity().sample(random)));
        if (!particleConfig.fadeColors().isEmpty()) {
            explosion.setTargetColor(Util.getRandom(particleConfig.fadeColors(), random));
        }
        if (particleConfig.explode()){
            explosion.onDead((p)-> {
                for (int i = 0; i < size; i++) {
                    this.createParticle(speed, size, p.getX(), p.getY(), p.getZ(),
                            random.nextGaussian() * speed * 0.05d + p.getVecX(),
                            random.nextGaussian() * speed * 0.05d + p.getVecY(),
                            random.nextGaussian() * speed * 0.05d + p.getVecZ(),
                            particleConfig.cancelExplodeClone());
                }
            });
        }
        return explosion;
    }

    public C getComponent() {
        return this.component;
    }

    public ExplosionType<?, ?> getType() {
        return this.type;
    }
}
