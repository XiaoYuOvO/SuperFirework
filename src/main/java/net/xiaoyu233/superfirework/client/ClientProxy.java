package net.xiaoyu233.superfirework.client;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.xiaoyu233.superfirework.common.CommonProxy;
import net.xiaoyu233.superfirework.entity.EntityLoader;
import net.xiaoyu233.superfirework.item.ItemLoader;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ItemLoader.registerRenders();
        EntityLoader.registerEntityRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}