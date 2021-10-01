package net.xiaoyu233.superfirework;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xiaoyu233.superfirework.client.ClientProxy;
import net.xiaoyu233.superfirework.common.CommonProxy;
import net.xiaoyu233.superfirework.entity.EntityLoader;
import net.xiaoyu233.superfirework.item.ItemLoader;
import net.xiaoyu233.superfirework.particle.ParticleLoader;
import net.xiaoyu233.superfirework.particle.ParticleManagerTrans;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

import static net.minecraft.particles.ParticleTypes.FLASH;
import static net.xiaoyu233.superfirework.particle.ParticleLoader.SUPER_FIREWORK;

@Mod(SuperFirework.MODID)
public class SuperFirework {
    public static final String MODID = "superfirework";
    private static final Logger LOGGER = LogManager.getLogger();
    public SuperFirework() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("SuperFirework preinit");

        CommonProxy.preInit(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientProxy.clientInit(event);
    }

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents{
        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> entityRegistryEvent){
            EntityLoader.registerEntities(entityRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)  {
            ItemLoader.registerItems(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onParticleRegistry(final RegistryEvent.Register<ParticleType<?>> particleTypeRegisterEvent){
            ParticleLoader.registerParticle(particleTypeRegisterEvent);
        }

        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void onParticleFactoryRegistry(final ParticleFactoryRegisterEvent particleFactoryRegisterEvent){
            Minecraft minecraft = Minecraft.getInstance();
            try {
                Field field_71452_i = ObfuscationReflectionHelper.findField(Minecraft.class,"field_71452_i");
                field_71452_i.setAccessible(true);
                field_71452_i.set(minecraft,new ParticleManagerTrans(minecraft.world,minecraft.getTextureManager()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            minecraft.particles.registerFactory(SUPER_FIREWORK, SuperFireworkParticle.SparkFactory::new);
            minecraft.particles.registerFactory(FLASH,SuperFireworkParticle.OverlayFactory::new);
        }
    }
}
