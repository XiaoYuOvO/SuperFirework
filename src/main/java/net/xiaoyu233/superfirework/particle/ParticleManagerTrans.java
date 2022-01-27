package net.xiaoyu233.superfirework.particle;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;

import java.util.List;

@SuppressWarnings("ALL")
public class ParticleManagerTrans extends ParticleManager {
    public ParticleManagerTrans(ClientWorld world, TextureManager textureManager) {
        super(world, textureManager);
    }
    public void tick() {
        this.byType.forEach((renderType, particleQueue) -> {
            this.world.getProfiler().startSection(renderType.toString());
            this.tickParticleList(particleQueue);
            this.world.getProfiler().endSection();
        });
        if (!this.particleEmitters.isEmpty()) {
            List<EmitterParticle> list = Lists.newArrayList();

            for(EmitterParticle emitterparticle : this.particleEmitters) {
                emitterparticle.tick();
                if (!emitterparticle.isAlive()) {
                    list.add(emitterparticle);
                }
            }

            this.particleEmitters.removeAll(list);
        }

        Particle particle;
        if (!this.queue.isEmpty()) {
            while((particle = this.queue.poll()) != null) {
                this.byType.computeIfAbsent(particle.getRenderType(), (renderType) -> EvictingQueue.create(32768)).add(particle);
            }
        }

    }

}
