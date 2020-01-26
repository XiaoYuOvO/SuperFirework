package net.xiaoyu233.superfirework.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xiaoyu233.superfirework.entity.EntitySuperFirework;

import static net.xiaoyu233.superfirework.item.ItemSuperFirework.getRandomFireworkTag;

public class ItemCloneFirework extends ItemFirework {
    ItemCloneFirework() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer p_onItemUse_1_, World p_onItemUse_2_, BlockPos p_onItemUse_3_, EnumHand p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        if (!p_onItemUse_2_.isRemote) {
            ItemStack lvt_9_1_ = p_onItemUse_1_.getHeldItem(p_onItemUse_4_);

            EntitySuperFirework lvt_10_1_ = new EntitySuperFirework(p_onItemUse_2_,
                    (float) p_onItemUse_3_.getX() + p_onItemUse_6_, (float) p_onItemUse_3_.getY() + p_onItemUse_7_,
                    (float) p_onItemUse_3_.getZ() + p_onItemUse_8_, lvt_9_1_);
            if (!lvt_9_1_.hasTagCompound()) {
                lvt_10_1_.readEntityFromNBT(getRandomFireworkTag(p_onItemUse_2_.rand));
            }
            lvt_10_1_.setClone(true);
            p_onItemUse_2_.spawnEntity(lvt_10_1_);
            if (!p_onItemUse_1_.capabilities.isCreativeMode) {
                lvt_9_1_.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }
}
