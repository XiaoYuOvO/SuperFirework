package net.xiaoyu233.superfirework.component.explosions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RandomBallComponent(double randomFactor) {
    public static final Codec<RandomBallComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.
                    group(
                            Codec.DOUBLE.optionalFieldOf("random_factor", 1.0d).forGetter(RandomBallComponent::randomFactor)
                    ).apply(instance, RandomBallComponent::new));
}
