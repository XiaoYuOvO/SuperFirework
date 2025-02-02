package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

import java.util.Optional;

public abstract class ShapedExplosion extends FireworkExplosion {
    protected static final double[][] CREEPER_SHAPE = new double[][]{
            {0.0D, 0.2D},
            {0.2D, 0.2D},
            {0.2D, 0.6D},
            {0.6D, 0.6D},
            {0.6D, 0.2D},
            {0.2D, 0.2D},
            {0.2D, 0.0D},
            {0.4D, 0.0D},
            {0.4D, -0.6D},
            {0.2D, -0.6D},
            {0.2D, -0.4D},
            {0.0D, -0.4D}};

    protected ShapedExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed, size, config, explosionTag);
    }

    protected Optional<double[][]> loadShape(NbtCompound nbtCompound){
        double[][] shapeArray = null;
        if(nbtCompound.contains("Shape", NbtElement.LIST_TYPE)){
            NbtList shapeList = nbtCompound.getList("Shape", 9);
            shapeArray = new double[shapeList.size()][];
            int indexA = 0;
            for (NbtElement nbtBase : shapeList) {
                if (nbtBase instanceof NbtList shape2){
                    double[] shape2Array = new double[shape2.size()];
                    shapeArray[indexA] = shape2Array;
                    int indexB = 0;
                    for (NbtElement base : shape2) {
                        if (base instanceof NbtDouble nbtDouble){
                            shape2Array[indexB] = nbtDouble.doubleValue();
                        }
                        indexB++;
                    }
                    indexA++;
                }else return Optional.empty();
            }
        }
        return Optional.ofNullable(shapeArray);
    }

    protected void createShaped(double x, double y, double z, double[][] shape, boolean keepShape, boolean mirrored) {
        double d0 = shape[0][0];
        double d1 = shape[0][1];
        this.createParticle(x, y, z, d0 * speed, d1 * speed, 0.0D);
        float f = random.nextFloat() * (float)Math.PI;
        double d2 = keepShape ? 0.034D : 0.34D;

        for (int i = 0; i < 3; ++i) {
            double d3 = (double) f + (double) ((float) i * (float) Math.PI) * d2;
            double d4 = d0;
            double d5 = d1;

            for (int j = 1; j < shape.length; ++j) {
                double d6 = shape[j][0];
                double d7 = shape[j][1];

                for (double d8 = 0.25D; d8 <= 1.0D; d8 += 0.25D) {
                    double d9 = (d4 + (d6 - d4) * d8) * speed;
                    double d10 = (d5 + (d7 - d5) * d8) * speed;
                    double d11 = d9 * Math.sin(d3);
                    d9 = d9 * Math.cos(d3);

                    double plus = 2.0d/size;
                    for (double d12 = -1.0D; d12 <= 1.0D; d12 += plus) {
                        if (mirrored) this.createParticle(x, y, z, d9 * (-1), d10, d11 * (-1));
                        this.createParticle(x, y, z, d9, d10, d11);
                    }
                }

                d4 = d6;
                d5 = d7;
            }
        }
    }
}
