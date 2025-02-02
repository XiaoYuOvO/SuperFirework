package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class StarShapedExplosion extends ShapedExplosion{
    private static final double[][] STAR_SHAPE = new double[][]{
            {0.0D, 1.0D},
            {0.3455D, 0.309D},
            {0.9511D, 0.309D},
            {0.3795918367346939D, -0.12653061224489795D},
            {0.6122448979591837D, -0.8040816326530612D},
            {0.0D, -0.35918367346938773D}};
    public StarShapedExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed == 0d ? 0.5d : speed, size, config, explosionTag);
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        this.createShaped(x, y, z, STAR_SHAPE, false, true);
    }
}
