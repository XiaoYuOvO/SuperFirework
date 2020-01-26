package net.xiaoyu233.superfirework.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opencl.CL;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ItemLoader {
    private static final ArrayList<Item> ITEMS = Lists.newArrayList();
    public static final Item SUPER_FIREWORK = new ItemSuperFirework();
    public static final Item CLONE_FIREWORK = new ItemCloneFirework();

    static {
        register("super_firework",SUPER_FIREWORK);
        register("clone_firework",CLONE_FIREWORK);
    }

    private static void register(String registerName,Item item){
        item.setRegistryName("superfirework",registerName);
        item.setTranslationKey(registerName);
        ITEMS.add(item);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders(){
        registerRender(SUPER_FIREWORK);
        registerRender(CLONE_FIREWORK);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        if (item.getRegistryName() != null) {
            ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, model);
        }
    }
}
