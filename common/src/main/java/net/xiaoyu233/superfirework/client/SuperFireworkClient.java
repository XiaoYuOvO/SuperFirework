package net.xiaoyu233.superfirework.client;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Unit;
import net.xiaoyu233.superfirework.entity.SFEntityTypes;
import net.xiaoyu233.superfirework.item.SFItems;
import net.xiaoyu233.superfirework.network.SFPackets;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;
import net.xiaoyu233.superfirework.util.Bitmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class SuperFireworkClient
{
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperFirework Client");
    public static void clientInit() {
        SFItems.registerRenders();
        SFPackets.registerPacket();
        SuperFireworkClient.registerParticle();
        if (MinecraftClient.getInstance().getResourceManager() instanceof ReloadableResourceManagerImpl reloadableResourceManager) {
            reloadableResourceManager.registerReloader(
                    (synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor) ->
                            CompletableFuture.supplyAsync(() -> Unit.INSTANCE, prepareExecutor)
                            .thenCompose(synchronizer::whenPrepared)
                            .thenAcceptAsync((u) -> Bitmap.invalidateCaches(), applyExecutor));
        }
        setupMadParticleSupport();
    }

    @SuppressWarnings("unchecked")
    private static void setupMadParticleSupport() {
        if (Platform.isModLoaded("madparticle")) {
            try {
                Class<?> takeOver = Class.forName("cn.ussshenzhou.madparticle.particle.enums.TakeOver");
                ((HashSet<Class<? extends Particle>>) takeOver.getField("ASYNC_TICK_VANILLA_AND_MADPARTICLE").get(null)).add(SuperFireworkParticle.Explosion.class);
                Field renderVanillaTransOpaque = takeOver.getDeclaredField("RENDER_VANILLA_TRANS_OPAQUE");
                renderVanillaTransOpaque.setAccessible(true);
                ((HashSet<Class<? extends SpriteBillboardParticle>>) renderVanillaTransOpaque.get(null)).add(SuperFireworkParticle.Explosion.class);
                LOGGER.info("Successfully registered super firework particle as async rendered particle to MadParticle");
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("Failed to add super firework particle tick type to MadParticle, this will impact performance", e);
            }
        }
    }

    public static void registerEntityRenderer(){
        SFEntityTypes.registerEntityRenderers();
    }

    public static void registerParticle() {
        ParticleProviderRegistry.register(SFParticleTypes.SUPER_FIREWORK, SuperFireworkParticle.ExplosionFactory::new);
    }
}