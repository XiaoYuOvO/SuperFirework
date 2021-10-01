package net.xiaoyu233.superfirework.item;

import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;


public class ItemLoader {
    public static final Item SUPER_FIREWORK = new SuperFireworkItem(new Item.Properties()).setRegistryName("super_firework");
    public static final Item CLONE_FIREWORK = new ItemCloneFirework(new Item.Properties()).setRegistryName("clone_firework");

    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent)  {
        itemRegistryEvent.getRegistry().registerAll(SUPER_FIREWORK,CLONE_FIREWORK);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenders(){
//        EntityRendererManager(SUPER_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"super_firework"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(CLONE_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"clone_firework"), "inventory"));
    }
}
