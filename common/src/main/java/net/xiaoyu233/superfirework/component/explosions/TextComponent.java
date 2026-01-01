package net.xiaoyu233.superfirework.component.explosions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record TextComponent(
        double rotation,
        String font,
        String content
) {
    private static final double DEFAULT_ROTATION = 0.0;
    private static final String DEFAULT_FONT = "Default";
    private static final String DEFAULT_CONTENT = "?";

    // TextComponent 的 CODEC 定义
    public static final Codec<TextComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE.optionalFieldOf("rotation", DEFAULT_ROTATION)
                            .forGetter(TextComponent::rotation),
                    Codec.STRING.optionalFieldOf("font", DEFAULT_FONT)
                            .forGetter(TextComponent::font),
                    Codec.STRING.optionalFieldOf("content", DEFAULT_CONTENT)
                            .forGetter(TextComponent::content)
            ).apply(instance, TextComponent::new)
    );

    // TextComponent 的 PACKET_CODEC 定义
    public static final PacketCodec<ByteBuf, TextComponent> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, // rotation
                    TextComponent::rotation,
                    PacketCodecs.STRING, // font
                    TextComponent::font,
                    PacketCodecs.STRING, // content
                    TextComponent::content,
                    TextComponent::new
            );

}
