package net.xiaoyu233.superfirework.particle.explosions;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.math.MathHelper;
import net.xiaoyu233.superfirework.component.explosions.ImageComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.util.Bitmap;

import java.awt.image.BufferedImage;

public class ImageExplosion extends FireworkExplosion<ImageComponent>{
    public ImageExplosion(ExplosionType<ImageComponent, ? extends FireworkExplosion<ImageComponent>> type, ImageComponent component) {
        super(type, component);
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {
        createImage(speed, size, x, y, z, config);
    }

    private void createImage(double speed, int size, double x, double y, double z, ParticleConfig config){
        BufferedImage image = Bitmap.loadImage(this.component.name());
        int width = image.getWidth();
        int height = image.getHeight();
        if (this.component.zoom() != 1){
            width = (int) (width * this.component.zoom());
            height = (int) (height * this.component.zoom());
            image = Bitmap.zoomInImage(image,width,height);
        }
        IntArrayList color = new IntArrayList(1);
        color.add(0);
        config = config.withColorsClone(color, IntList.of());
        double radRotation = Math.toRadians(this.component.imageRotation()) - Math.PI/2;
        double yStep = speed * 2  / height;
        double xStep = speed * 2  / width;
        double vecX = speed;
        for (int wIndex = 0; wIndex < width; wIndex++) {
            double vecY = speed;
            for (int hIndex = 0; hIndex < height; hIndex++) {
                int rgb = image.getRGB(wIndex, hIndex);
                if (rgb != 0){
                    color.set(0, rgb);
                    this.createParticle(speed, size, x, y, z, vecX, vecY, vecX* MathHelper.cos((float) radRotation), config);
                }
                vecY-=yStep;

            }
            vecX-=xStep;
        }
    }
}
