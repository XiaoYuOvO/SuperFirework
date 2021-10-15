package net.xiaoyu233.superfirework.common;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.xiaoyu233.superfirework.entity.EntityLoader;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;
import net.xiaoyu233.superfirework.item.ItemLoader;
import net.xiaoyu233.superfirework.network.PacketHandler;

import javax.annotation.Nonnull;

import static net.xiaoyu233.superfirework.item.SuperFireworkItem.getRandomFireworkTag;

public class CommonProxy
{

    public static void preInit(FMLCommonSetupEvent event) {
        PacketHandler.init();
        registerDispenser();
    }

    private static void registerDispenser(){
        DispenserBlock.registerDispenseBehavior(ItemLoader.SUPER_FIREWORK, new IDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack dispense(IBlockSource source, ItemStack stack) {
                Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)source.getBlockPos().getY() + 0.2F;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                SuperFireworkEntity entityfireworkrocket = new SuperFireworkEntity(source.getWorld(),null, d0, d1, d2, stack);
                if (!stack.hasTag()) {
                    entityfireworkrocket.readAdditional(getRandomFireworkTag(source.getWorld().rand));
                }
                source.getWorld().addEntity(entityfireworkrocket);
                stack.shrink(1);
                return stack;
            }
        });
        DispenserBlock.registerDispenseBehavior(ItemLoader.SUPER_FIREWORK, new IDispenseItemBehavior() {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            @Override
            @Nonnull
            public ItemStack dispense(IBlockSource source, ItemStack stack) {
                Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)source.getBlockPos().getY() + 0.2F;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                SuperFireworkEntity entityfireworkrocket = new SuperFireworkEntity(source.getWorld(),null, d0, d1, d2, stack);
                if (!stack.hasTag()) {
                    entityfireworkrocket.readAdditional(getRandomFireworkTag(source.getWorld().rand));
                }
                entityfireworkrocket.setClone(true);
                source.getWorld().addEntity(entityfireworkrocket);
                stack.shrink(1);
                return stack;
            }
        });
    }
}