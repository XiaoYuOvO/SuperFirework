package net.xiaoyu233.superfirework.particle;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKeys;
import net.xiaoyu233.superfirework.Superfirework;

public class SFParticleTypes {
    private static final Registrar<ParticleType<?>> PARTICLE_TYPE_REGISTRAR = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.PARTICLE_TYPE);
    public static final RegistrySupplier<SimpleParticleType> SUPER_FIREWORK = PARTICLE_TYPE_REGISTRAR.register(Superfirework.id("super_firework"), ()->new SimpleParticleType( false){});

    public static void registerParticle(){}
}
