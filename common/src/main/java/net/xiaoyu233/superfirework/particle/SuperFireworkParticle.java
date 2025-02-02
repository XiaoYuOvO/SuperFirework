package net.xiaoyu233.superfirework.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import net.minecraft.util.math.Vec3d;
import net.xiaoyu233.superfirework.util.NbtUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SuperFireworkParticle extends FireworksSparkParticle {
    @Environment(EnvType.CLIENT)
    public static class Explosion extends AnimatedParticle {
        private boolean trail;
        private boolean flicker;
        private final ParticleManager particleManager;
        private Optional<Consumer<Explosion>> deadAction = Optional.empty();

        Explosion(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ParticleManager particleManager, SpriteProvider spriteProvider) {
            super(world, x, y, z, spriteProvider, 0.1F);
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.velocityZ = velocityZ;
            this.particleManager = particleManager;
            this.scale *= 0.75F;
            this.maxAge = 48 + this.random.nextInt(12);
            this.setSpriteForAge(spriteProvider);
        }

        public void setGravityStrength(float gravityStrength){
            this.gravityStrength = gravityStrength;
        }


        public void onDead(Consumer<Explosion> action){
            this.deadAction = Optional.of(action);
        }

        public void setTrail(boolean trail) {
            this.trail = trail;
        }

        public void setFlicker(boolean flicker) {
            this.flicker = flicker;
        }

        public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
            if (!this.flicker || this.age < this.maxAge / 3 || (this.age + this.maxAge) / 3 % 2 == 0) {
                super.buildGeometry(vertexConsumer, camera, tickDelta);
            }

        }

        @Override
        public void setAlpha(float alpha) {
            super.setAlpha(alpha);
        }
        public double getX(){
            return this.x;
        }
        public double getY(){
            return this.y;
        }
        public double getZ(){
            return this.z;
        }
        public double getVecX(){
            return this.velocityX;
        }
        public double getVecY(){
            return this.velocityY;
        }
        public double getVecZ(){
            return this.velocityZ;
        }
        @Override
        public void markDead() {
            super.markDead();
        }

        public void tick() {
            super.tick();
            if (this.age == this.maxAge / 2)  this.deadAction.ifPresent(t -> t.accept(this));
            if (this.trail && this.age < this.maxAge / 2 && (this.age + this.maxAge) % 2 == 0) {
                SuperFireworkParticle.Explosion explosion = new SuperFireworkParticle.Explosion(this.world, this.x, this.y, this.z, 0.0, 0.0, 0.0, this.particleManager, this.spriteProvider);
                explosion.setAlpha(0.99F);
                explosion.setColor(this.red, this.green, this.blue);
                explosion.age = explosion.maxAge / 2;
                if (this.changesColor) {
                    explosion.changesColor = true;
                    explosion.targetRed = this.targetRed;
                    explosion.targetBlue = this.targetBlue;
                    explosion.targetGreen = this.targetGreen;
                }

                explosion.flicker = this.flicker;
                this.particleManager.addParticle(explosion);
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public static class ExplosionFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public ExplosionFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            SuperFireworkParticle.Explosion explosion = new SuperFireworkParticle.Explosion(clientWorld, d, e, f, g, h, i, MinecraftClient.getInstance().particleManager, this.spriteProvider);
            explosion.setAlpha(0.99F);
            return explosion;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Starter extends NoRenderParticle {
        private int fireworkAge;
        private final ParticleManager manager;
        private NbtList fireworkExplosions;
        private boolean twinkle;

        public Starter(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleManager particleManager, @Nullable NbtCompound fireworkCompound) {
            super(world, x, y, z);
            this.velocityX = motionX;
            this.velocityY = motionY;
            this.velocityZ = motionZ;
            this.manager = particleManager;
            this.maxAge = 8;
            if (fireworkCompound != null) {
                this.fireworkExplosions = fireworkCompound.getList("Explosions", 10);
                if (this.fireworkExplosions.isEmpty()) {
                    this.fireworkExplosions = null;
                } else {
                    this.maxAge = this.fireworkExplosions.size() * 2 - 1;

                    for(int i = 0; i < this.fireworkExplosions.size(); ++i) {
                        NbtCompound compoundnbt = this.fireworkExplosions.getCompound(i);
                        if (compoundnbt.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.maxAge += 15;
                            break;
                        }
                    }
                }
            }

        }

        public void tick() {
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                boolean far = this.isFarFromCamera();
                boolean hasLargeBall;
                if (this.fireworkExplosions.size() >= 3) {
                    hasLargeBall = true;
                } else {
                    hasLargeBall = this.fireworkExplosions.stream()
                            .filter(nbtElement -> nbtElement instanceof NbtCompound compound && compound.contains("Type", NbtElement.STRING_TYPE))
                            .anyMatch(e -> ExplosionTypes.getById(((NbtCompound) e).getString("Type"))
                                    .map(ExplosionType::largeBallSound)
                                    .orElse(false));
                }

                SoundEvent soundEvent;
                if (hasLargeBall) {
                    soundEvent = far ? SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
                } else {
                    soundEvent = far ? SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST;
                }

                this.world.playSound(this.x, this.y, this.z, soundEvent, SoundCategory.AMBIENT, 20.0F, 0.95F + this.random.nextFloat() * 0.1F, true);
            }

            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.size()) {
                int explosionIndex = this.fireworkAge / 2;
                NbtCompound explosionCompound = this.fireworkExplosions.getCompound(explosionIndex);
                Optional<ExplosionType<?>> type = ExplosionTypes.getById(explosionCompound.getString("Type"));
                int size = NbtUtil.getInt(explosionCompound, "Size").orElse(2);
                double speed = NbtUtil.getDouble(explosionCompound,"Speed").orElse(2.0d);
                ParticleConfig particleConfig = new ParticleConfig(explosionCompound, random);
                type.orElse(ExplosionTypes.BALL)
                        .create(this.manager, this.random, new Vec3d(this.velocityX, this.velocityY, this.velocityZ), speed, Math.abs(size), particleConfig, explosionCompound)
                        .spawnFireworkParticles(x, y, z);

                int j = particleConfig.colors[0];
                float f = (float)((j & 0xff0000) >> 16) / 255.0F;
                float f1 = (float)((j & 0xff00) >> 8) / 255.0F;
                float f2 = (float)((j & 0xff)) / 255.0F;
                Particle particle = this.manager.addParticle(ParticleTypes.FLASH, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                Objects.requireNonNull(particle).setColor(f, f1, f2);
            }

            ++this.fireworkAge;
            if (this.fireworkAge > this.maxAge) {
                if (this.twinkle) {
                    boolean flag3 = this.isFarFromCamera();
                    SoundEvent soundevent = flag3 ? SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE;
                    this.world.playSound(this.x, this.y, this.z, soundevent, SoundCategory.AMBIENT, 20.0F, 0.9F + this.random.nextFloat() * 0.15F, true);
                }

                this.markDead();
            }

        }

        private boolean isFarFromCamera() {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            return minecraft.gameRenderer.getCamera().getPos().squaredDistanceTo(this.x, this.y, this.z) >= 256.0;
        }
    }
}
