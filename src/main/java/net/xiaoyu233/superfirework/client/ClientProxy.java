package net.xiaoyu233.superfirework.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.xiaoyu233.superfirework.entity.EntityLoader;
import net.xiaoyu233.superfirework.item.ItemLoader;

public class ClientProxy
{
    public static void clientInit(FMLClientSetupEvent event) {
        ItemLoader.registerRenders();
        EntityLoader.registerEntityRenderers();
    }
}