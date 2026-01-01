package net.xiaoyu233.superfirework.component;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.RegistryKeys;
import net.xiaoyu233.superfirework.Superfirework;

public class SFComponents {
    private static final Registrar<ComponentType<?>> SUPER_FIREWORK = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.DATA_COMPONENT_TYPE);
    public static final RegistrySupplier<ComponentType<SuperFireworkComponent>> SUPER_FIREWORK_COMPONENT = SUPER_FIREWORK.register(
            Superfirework.id("super_firework"), () -> ComponentType.<SuperFireworkComponent>builder()
                    .codec(SuperFireworkComponent.CODEC)
                    .packetCodec(SuperFireworkComponent.PACKET_CODEC)
                    .build()
    );

    public static void registerComponents() {
    }
}
