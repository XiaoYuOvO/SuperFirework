package net.xiaoyu233.superfirework;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xiaoyu233.superfirework.common.ServerProxy;
import org.apache.logging.log4j.Logger;

@Mod(modid = SuperFirework.MODID, name = SuperFirework.NAME, version = SuperFirework.VERSION,acceptedMinecraftVersions = "1.12.2")
public class SuperFirework
{
    public static final String MODID = "superfirework";
    public static final String NAME = "Super Firework";
    public static final String VERSION = "1.2";
    @SidedProxy(clientSide = "net.xiaoyu233.superfirework.client.ClientProxy",
            serverSide = "net.xiaoyu233.superfirework.common.ServerProxy")
    public static ServerProxy proxy;
    @Mod.Instance(SuperFirework.MODID)
    public static SuperFirework INSTANCE;
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }
}

