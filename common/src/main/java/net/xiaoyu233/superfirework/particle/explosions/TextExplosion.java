package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.util.Bitmap;

import java.awt.*;

public class TextExplosion extends FireworkExplosion{
    private final String font;
    private final String content;
    private final double rotation;
    public TextExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed, size, config, explosionTag);
        if (explosionTag.contains("Rotation", NbtElement.DOUBLE_TYPE)){
            rotation = explosionTag.getDouble("Rotation");
        }else this.rotation = 0;
        if (explosionTag.contains("Font")){
            font = explosionTag.getString("Font");
        } else this.font = "Default";
        if (explosionTag.contains("Content")){
            content = explosionTag.getString("Content");
        }else this.content = "?";
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        createString(x, y, z);
    }


    private void createString(double x, double y, double z) {
        boolean[][] bitmap = Bitmap.getStringPixels(font, Font.PLAIN, Math.max(size,4), content);
        double radRotation = Math.toRadians(rotation) - Math.PI/2;
        if (bitmap.length != 0){
            double yStep = speed * 2 / bitmap.length;
            double xStep = speed * 4 / bitmap[0].length;

            double vecY = speed;
            for (boolean[] aLine : bitmap) {
                double vecX = speed * 2;
                for (boolean bit : aLine) {
                    if (bit){
                        this.createParticle(x, y, z, vecX * MathHelper.sin((float) radRotation), vecY, vecX * MathHelper.cos((float) radRotation));
                    }
                    vecX-=xStep;
                }
                vecY-=yStep;
            }
        }
    }
}
