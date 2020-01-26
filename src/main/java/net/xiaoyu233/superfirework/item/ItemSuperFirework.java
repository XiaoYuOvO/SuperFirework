package net.xiaoyu233.superfirework.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.xiaoyu233.superfirework.entity.EntitySuperFirework;
import net.xiaoyu233.superfirework.particle.ParticleSuperFirework;

import java.util.Random;

public class ItemSuperFirework extends ItemFirework {
    ItemSuperFirework(){
        this.setCreativeTab(CreativeTabs.MISC);
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer p_onItemUse_1_, World p_onItemUse_2_, BlockPos p_onItemUse_3_, EnumHand p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        if (!p_onItemUse_2_.isRemote) {
            ItemStack lvt_9_1_ = p_onItemUse_1_.getHeldItem(p_onItemUse_4_);

            EntitySuperFirework lvt_10_1_ = new EntitySuperFirework(p_onItemUse_2_,
                    (float)p_onItemUse_3_.getX() + p_onItemUse_6_,
                    (float)p_onItemUse_3_.getY() + p_onItemUse_7_,
                    (float)p_onItemUse_3_.getZ() + p_onItemUse_8_, lvt_9_1_);
            if (!lvt_9_1_.hasTagCompound()) {
                lvt_10_1_.readEntityFromNBT(getRandomFireworkTag(p_onItemUse_2_.rand));
            }
            p_onItemUse_2_.spawnEntity(lvt_10_1_);
            if (!p_onItemUse_1_.capabilities.isCreativeMode) {
                lvt_9_1_.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public static NBTTagCompound getRandomFireworkTag(Random rand){
        NBTTagCompound fireworkNBT = new NBTTagCompound();
        fireworkNBT.setInteger("LifeTime",30 + rand.nextInt(20));
        NBTTagCompound fireworksItem = new NBTTagCompound();
        fireworksItem.setString("id","fireworks");
        fireworksItem.setInteger("Count",1);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound fireworks = new NBTTagCompound();
        fireworks.setInteger("Speed", 1 + rand.nextInt(4));
        NBTTagList explosions = new NBTTagList();
        NBTTagCompound singleExplosions = new NBTTagCompound();
        singleExplosions.setInteger("Size",rand.nextInt(15));
        singleExplosions.setInteger("Type",rand.nextInt(ParticleSuperFirework.TYPES));
        NBTTagIntArray colors = new NBTTagIntArray(new int[]{getRandomColor(rand)});
        singleExplosions.setTag("Colors",colors);
        if (rand.nextBoolean()){
            NBTTagIntArray fadeColors = new NBTTagIntArray(new int[]{getRandomColor(rand)});
            singleExplosions.setTag("FadeColors",fadeColors);
        }
        explosions.appendTag(singleExplosions);
        fireworks.setTag("Explosions",explosions);
        tag.setTag("Fireworks",fireworks);
        fireworksItem.setTag("tag",tag);
        fireworkNBT.setTag("FireworksItem",fireworksItem);
        return fireworkNBT;
    }

    public static int getRandomColor(Random rand){
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return (r << 16) + (g << 8) + b;
    }

}
