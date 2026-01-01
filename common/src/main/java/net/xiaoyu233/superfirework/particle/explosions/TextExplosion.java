package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.util.math.MathHelper;
import net.xiaoyu233.superfirework.component.explosions.TextComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.util.Bitmap;

import java.awt.*;

public class TextExplosion extends FireworkExplosion<TextComponent>{
    public TextExplosion(ExplosionType<TextComponent, ? extends FireworkExplosion<TextComponent>> type, TextComponent component) {
        super(type, component);
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        createString(speed, size, x, y, z, config);
    }
    private void createString(double speed, int size, double x, double y, double z, ParticleConfig config) {
        boolean[][] bitmap = Bitmap.getStringPixels(this.component.font(), Font.PLAIN, Math.max(size,4), this.component.content());
        double radRotation = Math.toRadians(this.component.rotation()) - Math.PI/2;
        if (bitmap.length != 0){
            double yStep = speed * 2 / bitmap.length;
            double xStep = speed * 4 / bitmap[0].length;

            double vecY = speed;
            for (boolean[] aLine : bitmap) {
                double vecX = speed * 2;
                for (boolean bit : aLine) {
                    if (bit){
                        this.createParticle(speed, size, x, y, z, vecX * MathHelper.sin((float) radRotation), vecY, vecX * MathHelper.cos((float) radRotation), config);
                    }
                    vecX-=xStep;
                }
                vecY-=yStep;
            }
        }
    }
}
