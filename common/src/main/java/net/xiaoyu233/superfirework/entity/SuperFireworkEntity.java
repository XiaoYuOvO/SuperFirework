package net.xiaoyu233.superfirework.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;
import org.jetbrains.annotations.Nullable;

public class SuperFireworkEntity extends FireworkRocketEntity{
    private static final TrackedData<Boolean> CLONE = DataTracker.registerData(SuperFireworkEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public SuperFireworkEntity(EntityType<SuperFireworkEntity> type,World p_i1762_1_) {
        super(type,p_i1762_1_);
    }

    public SuperFireworkEntity(World world, double x, double y, double z, ItemStack stack) {
        super(SFEntityTypes.SUPER_FIREWORK.get(), world);
        this.setPosition(x, y, z);
        int i = 1;
        if (!stack.isEmpty() && stack.hasNbt()) {
            this.dataTracker.set(ITEM, stack.copy());
            i += stack.getOrCreateSubNbt("Fireworks").getByte("Flight");
        }

        this.setVelocity(this.random.nextTriangular((double)0.0F, 0.002297), 0.05, this.random.nextTriangular((double)0.0F, 0.002297));
        this.lifeTime = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
    }

    public SuperFireworkEntity(World world, @Nullable Entity entity, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z, stack);
        this.setOwner(entity);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleStatus(byte id) {
        if (id == EntityStatuses.EXPLODE_FIREWORK_CLIENT && this.getWorld().isClient) {
            if (!this.hasExplosionEffects()) {
                for(int i = 0; i < this.random.nextInt(3) + 2; ++i) {
                    this.getWorld().addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
                }
            } else {
                ItemStack itemStack = this.getStack();
                NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
                Vec3d velocity = this.getVelocity();
                ParticleManager particleManager = MinecraftClient.getInstance().particleManager;
                particleManager.addParticle(new SuperFireworkParticle.Starter((ClientWorld) this.getWorld(), this.getX(), this.getY(), this.getZ(), velocity.x, velocity.y, velocity.z, particleManager, nbtCompound));
            }
            return;
        }

        super.handleStatus(id);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CLONE,false);
    }

    @Override
    public void explode() {
        super.explode();
        if (this.isClone() && !this.getWorld().isClient) {
            int lifeTime = 10 + random.nextInt(5);
            double motionX = -0.5d;
            while (motionX < 0.6d) {
                double motionZ = -0.5d;
                while (motionZ < 0.6d) {
                    SuperFireworkEntity subFirework = new SuperFireworkEntity(this.getWorld(), this.getOwner(), this.getX(), this.getY(), this.getZ(), this.getStack()).setLifetime(lifeTime);
                    subFirework.getDataTracker().set(FireworkRocketEntity.SHOT_AT_ANGLE,true);
                    subFirework.setClone(false);
                    subFirework.setVelocity(motionX, 0.3, motionZ);
                    this.getWorld().spawnEntity(subFirework);
                    motionZ += 1d;
                }
                motionX += 1d;
            }
        }

    }

    public SuperFireworkEntity setLifetime(int lifetime) {
        this.lifeTime = lifetime;
        return this;
    }

    private boolean isClone() {
        return this.dataTracker.get(CLONE);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("Clone")){
            this.setClone(compound.getBoolean("Clone"));
        }
    }

    public void setClone(boolean b) {
        this.dataTracker.set(CLONE,b);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("Clone",this.isClone());
    }
}
