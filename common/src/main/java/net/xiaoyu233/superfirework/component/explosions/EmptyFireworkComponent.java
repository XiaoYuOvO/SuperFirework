package net.xiaoyu233.superfirework.component.explosions;

import com.mojang.serialization.Codec;

public record EmptyFireworkComponent() {
    public static final Codec<EmptyFireworkComponent> CODEC = Codec.unit(EmptyFireworkComponent::new);
    public static final EmptyFireworkComponent INSTANCE = new EmptyFireworkComponent();
}
