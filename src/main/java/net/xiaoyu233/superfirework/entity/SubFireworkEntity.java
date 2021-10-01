package net.xiaoyu233.superfirework.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import net.xiaoyu233.superfirework.network.PacketHandler;
import net.xiaoyu233.superfirework.network.SPacketSuperFireworkSpawn;

import java.util.OptionalInt;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class SubFireworkEntity extends ProjectileEntity implements IRendersAsItem
{
    public static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(SubFireworkEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<OptionalInt> BOOSTED_ENTITY_ID = EntityDataManager.createKey(SubFireworkEntity.class, DataSerializers.OPTIONAL_VARINT);
    private static final DataParameter<Boolean> SHOT_AT_ANGLE = EntityDataManager.createKey(SubFireworkEntity.class, DataSerializers.BOOLEAN);
    private int fireworkAge;
    private int lifetime;
    public LivingEntity boostedEntity;

    public SubFireworkEntity(EntityType<? extends SubFireworkEntity> p_i50164_1_, World p_i50164_2_) {
        super(p_i50164_1_, p_i50164_2_);
    }

    public SubFireworkEntity(World worldIn, double x, double y, double z, ItemStack givenItem)
    {
        super(EntityLoader.SUB_FIREWORK,worldIn);
        this.fireworkAge = 0;
        this.setPosition(x, y, z);
        int i = 1;

        if (!givenItem.isEmpty() && givenItem.hasTag())
        {
            this.dataManager.set(FIREWORK_ITEM, givenItem.copy());
            CompoundNBT nbttagcompound = givenItem.getTag();
            CompoundNBT nbttagcompound1 = nbttagcompound.getCompound("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    public SubFireworkEntity setLifetime(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    protected void registerData() {
        this.dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
        this.dataManager.register(BOOSTED_ENTITY_ID, OptionalInt.empty());
        this.dataManager.register(SHOT_AT_ANGLE, false);
    }

    /**
     * Checks if the entity is in range to render.
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 4096.0D && !this.isAttachedToEntity();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return super.isInRangeToRender3d(x, y, z) && !this.isAttachedToEntity();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.isAttachedToEntity()) {
            if (this.boostedEntity == null) {
                this.dataManager.get(BOOSTED_ENTITY_ID).ifPresent((p_213891_1_) -> {
                    Entity entity = this.world.getEntityByID(p_213891_1_);
                    if (entity instanceof LivingEntity) {
                        this.boostedEntity = (LivingEntity)entity;
                    }

                });
            }

            if (this.boostedEntity != null) {
                if (this.boostedEntity.isElytraFlying()) {
                    Vector3d vector3d = this.boostedEntity.getLookVec();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    Vector3d vector3d1 = this.boostedEntity.getMotion();
                    this.boostedEntity.setMotion(vector3d1.add(vector3d.x * 0.1D + (vector3d.x * 1.5D - vector3d1.x) * 0.5D, vector3d.y * 0.1D + (vector3d.y * 1.5D - vector3d1.y) * 0.5D, vector3d.z * 0.1D + (vector3d.z * 1.5D - vector3d1.z) * 0.5D));
                }

                this.setPosition(this.boostedEntity.getPosX(), this.boostedEntity.getPosY(), this.boostedEntity.getPosZ());
                this.setMotion(this.boostedEntity.getMotion());
            }
        } else {
            if (!this.shotAtAngle()) {
                double d2 = this.collidedHorizontally ? 1.0D : 1.15D;
                this.setMotion(this.getMotion().mul(d2, 1.0D, d2).add(0.0D, 0.04D, 0.0D));
            }

            Vector3d vector3d2 = this.getMotion();
            this.move(MoverType.SELF, vector3d2);
            this.setMotion(vector3d2);
        }

        RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
        if (!this.noClip) {
            this.onImpact(raytraceresult);
            this.isAirBorne = true;
        }

        this.func_234617_x_();
        if (this.fireworkAge == 0 && !this.isSilent()) {
            this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.fireworkAge;
        if (this.world.isRemote && this.fireworkAge % 2 < 2) {
            this.world.addParticle(ParticleTypes.FIREWORK, this.getPosX(), this.getPosY() - 0.3D, this.getPosZ(), this.rand.nextGaussian() * 0.05D, -this.getMotion().y * 0.5D, this.rand.nextGaussian() * 0.05D);
        }

        if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
            this.func_213893_k();
        }

    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.MISS || !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
            super.onImpact(result);
        }
    }

    private void func_213893_k() {
        this.world.setEntityState(this, (byte)17);
        this.dealExplosionDamage();
        this.remove();
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        super.onEntityHit(p_213868_1_);
        if (!this.world.isRemote) {
            this.func_213893_k();
        }
    }

    protected void func_230299_a_(BlockRayTraceResult p_230299_1_) {
        BlockPos blockpos = new BlockPos(p_230299_1_.getPos());
        this.world.getBlockState(blockpos).onEntityCollision(this.world, blockpos, this);
        if (!this.world.isRemote() && this.func_213894_l()) {
            this.func_213893_k();
        }

        super.func_230299_a_(p_230299_1_);
    }

    private boolean func_213894_l() {
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
        ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
        return listnbt != null && !listnbt.isEmpty();
    }

    private void dealExplosionDamage() {
        float f = 0.0F;
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
        ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
        if (listnbt != null && !listnbt.isEmpty()) {
            f = 5.0F + (float)(listnbt.size() * 2);
        }

        if (f > 0.0F) {
            if (this.boostedEntity != null) {
                this.boostedEntity.attackEntityFrom((new IndirectEntityDamageSource("fireworks", this, this.func_234616_v_())), 5.0F + (float)(listnbt.size() * 2));
            }

            double d0 = 5.0D;
            Vector3d vector3d = this.getPositionVec();

            for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(5.0D))) {
                if (livingentity != this.boostedEntity && !(this.getDistanceSq(livingentity) > 25.0D)) {
                    boolean flag = false;

                    for(int i = 0; i < 2; ++i) {
                        Vector3d vector3d1 = new Vector3d(livingentity.getPosX(), livingentity.getPosYHeight(0.5D * (double)i), livingentity.getPosZ());
                        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)this.getDistance(livingentity)) / 5.0D);
                        livingentity.attackEntityFrom((new IndirectEntityDamageSource("fireworks", this, this.func_234616_v_())).setExplosion(), f1);
                    }
                }
            }
        }

    }

    private boolean isAttachedToEntity() {
        return this.dataManager.get(BOOSTED_ENTITY_ID).isPresent();
    }

    public boolean shotAtAngle() {
        return this.dataManager.get(SHOT_AT_ANGLE);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 17 && this.world.isRemote) {
            if (!this.func_213894_l()) {
                for(int i = 0; i < this.rand.nextInt(3) + 2; ++i) {
                    this.world.addParticle(ParticleTypes.POOF, this.getPosX(), this.getPosY(), this.getPosZ(), this.rand.nextGaussian() * 0.05D, 0.005D, this.rand.nextGaussian() * 0.05D);
                }
            } else {
                ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
                CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
                Vector3d vector3d = this.getMotion();
                this.world.makeFireworks(this.getPosX(), this.getPosY(), this.getPosZ(), vector3d.x, vector3d.y, vector3d.z, compoundnbt);
            }
        }

        super.handleStatusUpdate(id);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Life", this.fireworkAge);
        compound.putInt("LifeTime", this.lifetime);
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        if (!itemstack.isEmpty()) {
            compound.put("FireworksItem", itemstack.write(new CompoundNBT()));
        }

        compound.putBoolean("ShotAtAngle", this.dataManager.get(SHOT_AT_ANGLE));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.fireworkAge = compound.getInt("Life");
        this.lifetime = compound.getInt("LifeTime");
        ItemStack itemstack = ItemStack.read(compound.getCompound("FireworksItem"));
        if (!itemstack.isEmpty()) {
            this.dataManager.set(FIREWORK_ITEM, itemstack);
        }

        if (compound.contains("ShotAtAngle")) {
            this.dataManager.set(SHOT_AT_ANGLE, compound.getBoolean("ShotAtAngle"));
        }

    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        return itemstack.isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : itemstack;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem() {
        return false;
    }

    public IPacket<?> createSpawnPacket() {
//        PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),new SPacketSuperFireworkSpawn(this));
        return new SSpawnObjectPacket(this);
    }
}
