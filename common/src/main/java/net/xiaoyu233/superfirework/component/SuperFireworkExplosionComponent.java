package net.xiaoyu233.superfirework.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.particle.explosions.FireworkExplosion;

import java.util.function.Consumer;

public record SuperFireworkExplosionComponent(FireworkExplosion<?> shape, int size, double speed, boolean flash, ParticleConfig config) implements TooltipAppender {
    public static final Codec<SuperFireworkExplosionComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            FireworkExplosion.CODEC.fieldOf("shape").forGetter(SuperFireworkExplosionComponent::shape),
                            Codecs.POSITIVE_INT.fieldOf("size").forGetter(SuperFireworkExplosionComponent::size),
                            Codec.DOUBLE.fieldOf("speed").forGetter(SuperFireworkExplosionComponent::speed),
                            Codec.BOOL.optionalFieldOf("flash", true).forGetter(SuperFireworkExplosionComponent::flash),
                            ParticleConfig.CODEC.fieldOf("particle").forGetter(SuperFireworkExplosionComponent::config)
                    )
                    .apply(instance, SuperFireworkExplosionComponent::new)
    );
    public static final PacketCodec<ByteBuf, SuperFireworkExplosionComponent> PACKET_CODEC = PacketCodec.tuple(
            FireworkExplosion.PACKET_CODEC,
            SuperFireworkExplosionComponent::shape,
            PacketCodecs.INTEGER,
            SuperFireworkExplosionComponent::size,
            PacketCodecs.DOUBLE,
            SuperFireworkExplosionComponent::speed,
            PacketCodecs.BOOL,
            SuperFireworkExplosionComponent::flash,
            ParticleConfig.PACKET_CODEC,
            SuperFireworkExplosionComponent::config,
            SuperFireworkExplosionComponent::new
    );
    private static final Text CUSTOM_COLOR_TEXT = Text.translatable("item.minecraft.firework_star.custom_color");

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        this.appendShapeTooltip(tooltip);
        this.appendOptionalTooltip(tooltip);
    }

    public void appendShapeTooltip(Consumer<Text> textConsumer) {
        textConsumer.accept(Text.literal(this.shape.getType().getName()).formatted(Formatting.GRAY));
    }

    public void appendOptionalTooltip(Consumer<Text> textConsumer) {
        if (!this.config.colors().isEmpty()) {
            textConsumer.accept(appendColorsTooltipText(Text.empty().formatted(Formatting.GRAY), this.config.colors()));
        }

        if (!this.config.fadeColors().isEmpty()) {
            textConsumer.accept(
                    appendColorsTooltipText(Text.translatable("item.minecraft.firework_star.fade_to").append(ScreenTexts.SPACE).formatted(Formatting.GRAY), this.config.fadeColors())
            );
        }

        if (this.config.trail()) {
            textConsumer.accept(Text.translatable("item.minecraft.firework_star.trail").formatted(Formatting.GRAY));
        }

        if (this.config.flicker()) {
            textConsumer.accept(Text.translatable("item.minecraft.firework_star.flicker").formatted(Formatting.GRAY));
        }
    }

    private static Text appendColorsTooltipText(MutableText text, IntList colors) {
        for (int i = 0; i < colors.size(); i++) {
            if (i > 0) {
                text.append(", ");
            }

            text.append(getColorText(colors.getInt(i)));
        }

        return text;
    }

    private static Text getColorText(int color) {
        DyeColor dyeColor = DyeColor.byFireworkColor(color);
        return (Text)(dyeColor == null ? CUSTOM_COLOR_TEXT : Text.translatable("item.minecraft.firework_star." + dyeColor.getName()));
    }
}
