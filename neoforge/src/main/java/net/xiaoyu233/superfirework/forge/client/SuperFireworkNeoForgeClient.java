package net.xiaoyu233.superfirework.forge.client;

import dev.architectury.registry.client.level.entity.forge.EntityRendererRegistryImpl;
import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.xiaoyu233.superfirework.Superfirework;
import net.xiaoyu233.superfirework.client.SuperFireworkClient;
import net.xiaoyu233.superfirework.entity.SFEntityTypes;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

@EventBusSubscriber(modid = Superfirework.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SuperFireworkNeoForgeClient {
    @SubscribeEvent
    static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SFEntityTypes.SUPER_FIREWORK.get(), FireworkRocketEntityRenderer::new);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    private static void doClientStuff(final FMLClientSetupEvent event) {
        SuperFireworkClient.clientInit();
    }

    @SubscribeEvent
    private static void registerParticle(final RegisterParticleProvidersEvent event){
        event.registerSpriteSet(SFParticleTypes.SUPER_FIREWORK.get(), SuperFireworkParticle.ExplosionFactory::new);
    }
}
