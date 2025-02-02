package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ParticleConfig;
import net.xiaoyu233.superfirework.util.Bitmap;

import java.awt.image.BufferedImage;

public class ImageExplosion extends FireworkExplosion{
    private final String name;
    private final double zoom;
    private final double imageRotation;

    public ImageExplosion(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        super(particleManager, random, parentVec, speed, size, config, explosionTag);
        if (explosionTag.contains("Zoom", NbtElement.DOUBLE_TYPE)){
            zoom = MathHelper.clamp(0,explosionTag.getDouble("Zoom"),10);
        }else {
            this.zoom = 1;
        }
        if (explosionTag.contains("Rotation", NbtElement.DOUBLE_TYPE)){
            imageRotation = explosionTag.getDouble("Rotation");
        }else {
            this.imageRotation = 0;
        }
        if (explosionTag.contains("Name", NbtElement.STRING_TYPE)){
            this.name = explosionTag.getString("Name").toLowerCase();
        }else this.name = "-";
    }

    @Override
    public void spawnFireworkParticles(double x, double y, double z) {
        createImage(x, y, z);
    }

    private void createImage(double x, double y, double z){
        BufferedImage image = Bitmap.loadImage(name);
        int width = image.getWidth();
        int height = image.getHeight();
        if (zoom != 1){
            width = (int) (width * zoom);
            height = (int) (height * zoom);
            image = Bitmap.zoomInImage(image,width,height);
        }
        int[] color = new int[1];
        particleConfig.colors = color;
        particleConfig.fadeColor = new int[0];
        double radRotation = Math.toRadians(imageRotation) - Math.PI/2;
        double yStep = speed * 2  / height;
        double xStep = speed * 2  / width;
        double vecX = speed;
        for (int wIndex = 0; wIndex < width; wIndex++) {
            double vecY = speed;
            for (int hIndex = 0; hIndex < height; hIndex++) {
                int rgb = image.getRGB(wIndex, hIndex);
                if (rgb != 0){
                    color[0] = rgb;
                    this.createParticle(x, y, z, vecX, vecY, vecX* MathHelper.cos((float) radRotation));
                }
                vecY-=yStep;

            }
            vecX-=xStep;
        }
    }
}
