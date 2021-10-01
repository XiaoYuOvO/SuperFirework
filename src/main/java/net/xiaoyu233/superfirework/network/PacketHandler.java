package net.xiaoyu233.superfirework.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.xiaoyu233.superfirework.SuperFirework;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("superfirework","channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        CHANNEL.messageBuilder(SPacketSuperFireworkSpawn.class, id++).encoder(SPacketSuperFireworkSpawn::writePacketData).decoder(SPacketSuperFireworkSpawn::new).consumer(SPacketSuperFireworkSpawn.Handler::onMessage).add();
    }
}
