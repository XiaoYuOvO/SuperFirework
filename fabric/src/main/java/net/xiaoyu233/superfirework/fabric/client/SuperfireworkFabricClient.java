package net.xiaoyu233.superfirework.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.impl.client.particle.ParticleFactoryRegistryImpl;
import net.xiaoyu233.superfirework.client.SuperFireworkClient;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

public final class SuperfireworkFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SuperFireworkClient.clientInit();
        ParticleFactoryRegistry.getInstance().register(SFParticleTypes.SUPER_FIREWORK.get(), SuperFireworkParticle.ExplosionFactory::new);
        SuperFireworkClient.registerEntityRenderer();
    }
}
