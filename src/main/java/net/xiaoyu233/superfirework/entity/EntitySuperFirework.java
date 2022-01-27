package net.xiaoyu233.superfirework.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xiaoyu233.superfirework.item.ItemSuperFirework;
import net.xiaoyu233.superfirework.particle.ParticleSuperFirework;

import static net.xiaoyu233.superfirework.item.ItemSuperFirework.getRandomColor;

public class EntitySuperFirework extends EntityFireworkRocket {
    private static final DataParameter<Boolean> CLONE = EntityDataManager.createKey(EntitySuperFirework.class,
            DataSerializers.BOOLEAN);
    public EntitySuperFirework(World p_i1762_1_) {
        super(p_i1762_1_);
    }

    public EntitySuperFirework(World p_onItemUse_2_, double v, double v1, double v2, ItemStack lvt_9_1_) {
        super(p_onItemUse_2_, v, v1, v2, lvt_9_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLONE,false);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && this.world.isRemote)
        {


                ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
                NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
                Minecraft.getMinecraft().effectRenderer.addEffect(
                        new ParticleSuperFirework.Starter(this.world, this.posX, this.posY, this.posZ, motionX, motionY,
                                motionZ, Minecraft.getMinecraft().effectRenderer, nbttagcompound));

        }
    }

    private boolean isClone() {
        return this.dataManager.get(CLONE);
    }

    @Override
    public void dealExplosionDamage() {
        super.dealExplosionDamage();
        if (this.isClone()) {
            int lifeTime = 10 + rand.nextInt(5);
            double motionX = -0.5d;
            while (motionX < 0.6d) {
                double motionZ = -0.5d;
                while (motionZ < 0.6d) {
                    SubFireworkEntity subFirework = new SubFireworkEntity(this.world, this.posX, this.posY, this.posZ, this.dataManager.get(FIREWORK_ITEM)).setLifetime(lifeTime);

                    subFirework.setVelocity(motionX, 1, motionZ);
                    this.world.spawnEntity(subFirework);
                    motionZ += 1d;
                }
                motionX += 1d;
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.fillNBTTags(compound);
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Clone")){
            this.dataManager.set(CLONE, compound.getBoolean("Clone"));
        }
    }

    public void setClone(boolean b) {
        this.dataManager.set(CLONE,b);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Clone",this.isClone());
    }

    private void fillNBTTags(NBTTagCompound tag){
        if (!tag.hasKey("LifeTime")){
            tag.setInteger("LifeTime",30 + this.rand.nextInt(20));
        }
        if (!tag.hasKey("FireworksItem")){
            tag.setTag("FireworksItem",ItemSuperFirework.getRandomFireworkTag(rand).getTag("FireworksItem"));
        }else{
            NBTTagCompound fireworksItem = tag.getCompoundTag("FireworksItem");
            fireworksItem.setInteger("Count",1);
            fireworksItem.setString("id","fireworks");
            NBTTagCompound fireworks = fireworksItem.getCompoundTag("tag").getCompoundTag("Fireworks");
            if (!fireworks.hasKey("Speed")){
                fireworks.setInteger("Speed", 1 + rand.nextInt(4));
            }
            NBTTagList fireworksList = fireworks.getTagList("Explosions",10);
            for (int index = 0;index<fireworksList.tagCount();index++){
                NBTTagCompound fireworkTag = fireworksList.getCompoundTagAt(index);
                if (!fireworkTag.hasKey("Size")){
                    fireworkTag.setInteger("Size",rand.nextInt(15));
                }
                if (!fireworkTag.hasKey("Type")){
                    fireworkTag.setInteger("Type",rand.nextInt(ParticleSuperFirework.FireworkShape.values().length));
                }
                if (!fireworkTag.hasKey("Colors")){
                    fireworkTag.setTag("Colors",new NBTTagIntArray(new int[]{getRandomColor(rand)}));
                }
                if (!fireworkTag.hasKey("FadeColors") && !(fireworkTag.hasKey("NoFade") && fireworkTag.getBoolean("NoFade"))){
                    fireworkTag.setTag("Colors",new NBTTagIntArray(new int[]{getRandomColor(rand)}));
                }

            }
        }
    }

}
