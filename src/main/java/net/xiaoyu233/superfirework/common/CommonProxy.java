package net.xiaoyu233.superfirework.common;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.xiaoyu233.superfirework.entity.EntityLoader;
import net.xiaoyu233.superfirework.entity.EntitySuperFirework;
import net.xiaoyu233.superfirework.item.ItemLoader;

import static net.xiaoyu233.superfirework.item.ItemSuperFirework.getRandomFireworkTag;

public class CommonProxy
{

    public void preInit(FMLPreInitializationEvent event) {
        EntityLoader.registerEntities();
        ItemLoader.preInit();
        registerDispenser();
    }

    private void registerDispenser(){
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemLoader.SUPER_FIREWORK, new BehaviorDefaultDispenseItem()
        {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)source.getBlockPos().getY() + 0.2F;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                EntitySuperFirework entityfireworkrocket = new EntitySuperFirework(source.getWorld(), d0, d1, d2, stack);
                if (!stack.hasTagCompound()) {
                    entityfireworkrocket.readEntityFromNBT(getRandomFireworkTag(source.getWorld().rand));
                }
                source.getWorld().spawnEntity(entityfireworkrocket);
                stack.shrink(1);
                return stack;
            }
            /**
             * Play the dispense sound from the specified block.
             */
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playEvent(1004, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemLoader.CLONE_FIREWORK, new BehaviorDefaultDispenseItem()
        {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)source.getBlockPos().getY() + 0.2F;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                EntitySuperFirework entityfireworkrocket = new EntitySuperFirework(source.getWorld(), d0, d1, d2, stack);
                if (!stack.hasTagCompound()) {
                    entityfireworkrocket.readEntityFromNBT(getRandomFireworkTag(source.getWorld().rand));
                }
                entityfireworkrocket.setClone(true);
                source.getWorld().spawnEntity(entityfireworkrocket);
                stack.shrink(1);
                return stack;
            }
            /**
             * Play the dispense sound from the specified block.
             */
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playEvent(1004, source.getBlockPos(), 0);
            }
        });
    }


    public void init(FMLInitializationEvent event) {

    }


    public void postInit(FMLPostInitializationEvent event) {

    }
}