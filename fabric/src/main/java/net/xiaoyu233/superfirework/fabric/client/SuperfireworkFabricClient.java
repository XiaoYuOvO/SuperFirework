package net.xiaoyu233.superfirework.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.xiaoyu233.superfirework.client.SuperFireworkClient;

public final class SuperfireworkFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SuperFireworkClient.clientInit();
        SuperFireworkClient.registerParticle();
        SuperFireworkClient.registerEntityRenderer();
    }
}
