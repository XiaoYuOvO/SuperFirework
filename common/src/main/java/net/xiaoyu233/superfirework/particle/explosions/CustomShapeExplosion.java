package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

import java.util.Optional;

public class CustomShapeExplosion extends ShapedExplosion {
    private final Optional<double[][]> shape;
    public CustomShapeExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 2d : speed, size, config, explosionTag);
        this.shape = loadShape(explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        if (shape.isPresent()){
            createShaped(x,y,z,shape.get(),true,false);
        }else {
            createShaped(x,y,z,CREEPER_SHAPE,true,true);
        }
    }
}
