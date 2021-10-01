package net.xiaoyu233.superfirework.particle;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;

public class ParticleLoader {
    public static final BasicParticleType SUPER_FIREWORK = new BasicParticleType( false);
    public static void registerParticle(RegistryEvent.Register<ParticleType<?>> particleTypeRegisterEvent){
        particleTypeRegisterEvent.getRegistry().register(SUPER_FIREWORK.setRegistryName("super_firework"));

    }
}
