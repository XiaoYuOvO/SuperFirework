package net.xiaoyu233.superfirework.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xiaoyu233.superfirework.SuperFirework;

import java.util.ArrayList;

public class ItemLoader {
    public static final ItemSuperFirework SUPER_FIREWORK = new ItemSuperFirework();
    public static final ItemCloneFirework CLONE_FIREWORK = new ItemCloneFirework();

    public static void preInit() {
        register("clone_firework",CLONE_FIREWORK);
        register("super_firework",SUPER_FIREWORK);
    }

    private static <T extends Item> void register(String registerName,T item){
        ForgeRegistries.ITEMS.register(item.setRegistryName(registerName).setTranslationKey(registerName));
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders(){
        ModelLoader.setCustomModelResourceLocation(SUPER_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"super_firework"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(CLONE_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"clone_firework"), "inventory"));
    }




}
