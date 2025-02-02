package net.xiaoyu233.superfirework.particle;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.RegistryKeys;
import net.xiaoyu233.superfirework.Superfirework;

public class SFParticleTypes {
    private static final Registrar<ParticleType<?>> PARTICLE_TYPE_REGISTRAR = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.PARTICLE_TYPE);
    public static final RegistrySupplier<DefaultParticleType> SUPER_FIREWORK = PARTICLE_TYPE_REGISTRAR.register(Superfirework.id("super_firework"), ()->new DefaultParticleType( false){});

    public static void registerParticle(){}
}
