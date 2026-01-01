package net.xiaoyu233.superfirework.component.explosions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.MathHelper;

public record ImageComponent(
        double zoom,
        double imageRotation,
        String name
) {
    // 默认值常量
    private static final double DEFAULT_ZOOM = 1.0;
    private static final double MAX_ZOOM = 10.0;
    private static final double DEFAULT_ROTATION = 0.0;
    private static final String DEFAULT_NAME = "-";

    // ImageExplosionConfig 的 CODEC 定义
    public static final Codec<ImageComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE.optionalFieldOf("zoom", DEFAULT_ZOOM)
                            .xmap(
                                    zoom -> MathHelper.clamp(zoom, 0, MAX_ZOOM),
                                    zoom -> zoom
                            )
                            .forGetter(ImageComponent::zoom),
                    Codec.DOUBLE.optionalFieldOf("rotation", DEFAULT_ROTATION)
                            .forGetter(ImageComponent::imageRotation),
                    Codec.STRING.optionalFieldOf("name", DEFAULT_NAME)
                            .xmap(String::toLowerCase, String::toLowerCase)
                            .forGetter(ImageComponent::name)
            ).apply(instance, ImageComponent::new)
    );

    // ImageExplosionConfig 的 PACKET_CODEC 定义
    public static final PacketCodec<ByteBuf, ImageComponent> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, // zoom
                    ImageComponent::zoom,
                    PacketCodecs.DOUBLE, // imageRotation
                    ImageComponent::imageRotation,
                    PacketCodecs.STRING, // name
                    config -> config.name().toLowerCase(), // 确保名称为小写
                    ImageComponent::new
            );
}
