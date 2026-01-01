package net.xiaoyu233.superfirework.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.function.Consumer;

public record SuperFireworkComponent(int flightDuration, List<SuperFireworkExplosionComponent> explosions, boolean isClone) implements TooltipAppender {
    public static final int MAX_EXPLOSIONS = 256;
    public static final Codec<SuperFireworkComponent> CODEC = RecordCodecBuilder.<SuperFireworkComponent>create(
            instance -> instance.group(
                            Codecs.UNSIGNED_BYTE.optionalFieldOf("flight_duration", 0).forGetter(SuperFireworkComponent::flightDuration),
                            SuperFireworkExplosionComponent.CODEC.sizeLimitedListOf(MAX_EXPLOSIONS).optionalFieldOf("explosions", List.of()).forGetter(SuperFireworkComponent::explosions),
                            Codec.BOOL.optionalFieldOf("is_clone", false).forGetter(SuperFireworkComponent::isClone)
                    )
                    .apply(instance, SuperFireworkComponent::new)
    ).mapResult(new Codec.ResultFunction<SuperFireworkComponent>() {
        @Override
        public <T> DataResult<Pair<SuperFireworkComponent, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<SuperFireworkComponent, T>> a) {
            return a;
        }

        @Override
        public <T> DataResult<T> coApply(DynamicOps<T> ops, SuperFireworkComponent input, DataResult<T> t) {
            return t;
        }
    });
    public static final PacketCodec<ByteBuf, SuperFireworkComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            SuperFireworkComponent::flightDuration,
            SuperFireworkExplosionComponent.PACKET_CODEC.collect(PacketCodecs.toList(MAX_EXPLOSIONS)),
            SuperFireworkComponent::explosions,
            PacketCodecs.BOOL,
            SuperFireworkComponent::isClone,
            SuperFireworkComponent::new
    );

    public SuperFireworkComponent(int flightDuration, List<SuperFireworkExplosionComponent> explosions, boolean isClone) {
        if (explosions.size() > MAX_EXPLOSIONS) {
            throw new IllegalArgumentException("Got " + explosions.size() + " explosions, but maximum is " + MAX_EXPLOSIONS);
        } else {
            this.flightDuration = flightDuration;
            this.explosions = explosions;
            this.isClone = isClone;
        }
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (this.flightDuration > 0) {
            tooltip.accept(
                    Text.translatable("item.minecraft.firework_rocket.flight").append(ScreenTexts.SPACE).append(String.valueOf(this.flightDuration)).formatted(Formatting.GRAY)
            );
        }

        for (SuperFireworkExplosionComponent fireworkExplosionComponent : this.explosions) {
            fireworkExplosionComponent.appendShapeTooltip(tooltip);
            fireworkExplosionComponent.appendOptionalTooltip(text -> tooltip.accept(Text.literal("  ").append(text)));
        }
    }
}
