package net.xiaoyu233.superfirework.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xiaoyu233.superfirework.util.Bitmap;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;

@SideOnly(Side.CLIENT)
public class ParticleSuperFirework extends ParticleFirework {
    public static final int TYPES = 7;
    @SideOnly(Side.CLIENT)
    public static class Overlay extends Particle
    {
        public Overlay(World p_i46466_1_, double p_i46466_2_, double p_i46466_4_, double p_i46466_6_)
        {
            super(p_i46466_1_, p_i46466_2_, p_i46466_4_, p_i46466_6_);
            this.particleMaxAge = 4;
        }

        /**
         * Renders the particle
         */
        public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
        {
            float f4 = 7.1F * MathHelper.sin(((float)this.particleAge + partialTicks - 1.0F) * 0.25F * (float)Math.PI);
            this.setAlphaF(0.6F - ((float)this.particleAge + partialTicks - 1.0F) * 0.25F * 0.5F);
            float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
            float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
            float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
            int i = this.getBrightnessForRender(partialTicks);
            int j = i >> 16 & 65535;
            int k = i & 65535;
            buffer.pos(f5 - rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 - rotationYZ * f4 - rotationXZ * f4).tex(0.5D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f5 - rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 - rotationYZ * f4 + rotationXZ * f4).tex(0.5D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f5 + rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 + rotationYZ * f4 + rotationXZ * f4).tex(0.25D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos(f5 + rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 + rotationYZ * f4 - rotationXZ * f4).tex(0.25D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }
    }
    @SideOnly(Side.CLIENT)
    public static class Starter extends Particle {
        private final ParticleManager manager;
        boolean twinkle;
        private int fireworkAge;
        private double speed;
        private NBTTagList fireworkExplosions;

        public Starter(World p_i46464_1_, double p_i46464_2_, double p_i46464_4_, double p_i46464_6_, double p_i46464_8_, double p_i46464_10_, double p_i46464_12_, ParticleManager p_i46464_14_, @Nullable NBTTagCompound p_i46464_15_) {
            super(p_i46464_1_, p_i46464_2_, p_i46464_4_, p_i46464_6_, 0.0D, 0.0D, 0.0D);
            this.motionX = p_i46464_8_;
            this.motionY = p_i46464_10_;
            this.motionZ = p_i46464_12_;
            this.manager = p_i46464_14_;
            this.particleMaxAge = 8;

            if (p_i46464_15_ != null) {
                this.fireworkExplosions = p_i46464_15_.getTagList("Explosions", 10);
                if (p_i46464_15_.hasKey("Speed")) {
                    this.speed = p_i46464_15_.getDouble("Speed");
                }
                if (this.fireworkExplosions.isEmpty()) {
                    this.fireworkExplosions = null;
                } else {
                    this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;

                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);

                        if (nbttagcompound.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.particleMaxAge += 15;
                            break;
                        }
                    }
                }
            }
        }

        /**
         * Creates a small ball or large ball type explosion effect.
         */
        private void createBall(double speed, int size, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn)
        {
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;

            for (int i = -size; i <= size; i ++)
            {
                for (int j = -size; j <= size; j ++)
                {
                    for (int k = -size; k <= size; k ++)
                    {
                        //The larger the value, the mess the generated particles be
                        double randomizeAmount = 0.5D;

                        double d3 = ((double)j + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d4 = ((double)i + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d5 = ((double)k + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / speed + this.rand.nextGaussian() * 0.05d;
                        this.createParticle(d0, d1, d2, d3 / d6, d4 / d6, d5 / d6, colours, fadeColours, trail, twinkleIn);

                        if (i != -size && i != size && j != -size && j != size)
                        {
                            k += size * 2 - 1;
                        }
                    }
                }
            }
        }

        /**
         * Creates a burst type explosion effect.
         */
        private void createBurst(int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn) {
            double d0 = this.rand.nextGaussian() * 0.05D;
            double d1 = this.rand.nextGaussian() * 0.05D;

            for (int i = 0; i < 70; ++i) {
                double d2 = this.motionX * 0.5D + this.rand.nextGaussian() * 0.15D + d0;
                double d3 = this.motionZ * 0.5D + this.rand.nextGaussian() * 0.15D + d1;
                double d4 = this.motionY * 0.5D + this.rand.nextDouble() * 0.5D;
                this.createParticle(this.posX, this.posY, this.posZ, d2, d4, d3, colours, fadeColours, trail,
                        twinkleIn);
            }
        }

        private void createRandomBall(double speed, int size, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn)
        {
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;

            for (int i = -size; i <= size; i ++)
            {

                for (int j = -size; j <= size; j ++)
                {

                    for (int k = -size; k <= size; k ++)
                    {
                        double randSpeed = rand.nextInt(6)/5d;
                        //The larger the value, the mess the generated particles be
                        double randomizeAmount = 0.5D;
                        double d3 = ((double)j + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d4 = ((double)i + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d5 = ((double)k + (this.rand.nextDouble() - this.rand.nextDouble()) * randomizeAmount);
                        double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / (speed * randSpeed) + this.rand.nextGaussian() * 0.05d;
                        this.createParticle(d0, d1, d2, d3 / d6, d4 / d6, d5 / d6, colours, fadeColours, trail, twinkleIn);

                        if (i != -size && i != size && j != -size && j != size)
                        {
                            k += size * 2 - 1;
                        }
                    }
                }
            }
        }

        /**
         * Creates a single particle.
         */
        private void createParticle(double x, double y, double z, double motionX, double motionY, double motionZ, int[] p_92034_13_, int[] p_92034_14_, boolean p_92034_15_, boolean p_92034_16_) {
            ParticleFirework.Spark particlefirework$spark = new ParticleFirework.Spark(this.world, x,
                    y, z, motionX, motionY, motionZ, this.manager);
            particlefirework$spark.setAlphaF(0.99F);
            particlefirework$spark.setTrail(p_92034_15_);
            particlefirework$spark.setTwinkle(p_92034_16_);
            int i = this.rand.nextInt(p_92034_13_.length);
            particlefirework$spark.setColor(p_92034_13_[i]);

            if (p_92034_14_ != null && p_92034_14_.length > 0) {
                particlefirework$spark.setColorFade(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
            }

            this.manager.addEffect(particlefirework$spark);
        }

        /**
         * Creates a creeper-shaped or star-shaped explosion.
         */
        private void createMirroredShaped(double speed, int size,double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            double d0 = shape[0][0];
            double d1 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours,
                    trail, twinkleIn);
            float f = this.rand.nextFloat() * (float) Math.PI;
            double d2 = p_92038_8_ ? 0.034D : 0.34D;

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
                            this.createParticle(this.posX, this.posY, this.posZ, d9 * (-1), d10, d11 * (-1), colours,
                                    fadeColours, trail, twinkleIn);
                            this.createParticle(this.posX, this.posY, this.posZ, d9 * 1, d10, d11 * 1, colours,
                                    fadeColours, trail, twinkleIn);
                        }
                    }

                    d4 = d6;
                    d5 = d7;
                }
            }
        }

        private void createShaped(double speed, int size,double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            double d0 = shape[0][0];
            double d1 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours,
                    trail, twinkleIn);
            float f = 1 * (float) Math.PI;
            double d2 = p_92038_8_ ? 0.034D : 0.34D;

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
                            this.createParticle(this.posX, this.posY, this.posZ, d9, d10, d11, colours,
                                    fadeColours, trail, twinkleIn);
                            this.createParticle(this.posX, this.posY, this.posZ, d9 * 1, d10, d11 * 1, colours,
                                    fadeColours, trail, twinkleIn);
                        }
                    }

                    d4 = d6;
                    d5 = d7;
                }
            }
        }

        private void createString(double speed, int size,String content, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            boolean[][] bitmap = Bitmap.getStringPixels("Default", 0, Math.max(size,4), content);
            if (bitmap.length != 0){
                double yStep = speed * 2 / bitmap.length;
                double xStep = speed * 2 / bitmap[0].length;

                double vecY = speed;
                for (boolean[] booleans : bitmap) {
                    double vecX = speed;
                    for (boolean aBoolean : booleans) {
                        if (aBoolean){
                            this.createParticle(this.posX, this.posY, this.posZ, vecX, vecY, 0, colours,
                                    fadeColours, trail, twinkleIn);
                        }
                        vecX-=xStep;
                    }
                    vecY-=yStep;
                }
            }
        }


        private boolean isFarFromCamera() {
            Minecraft minecraft = Minecraft.getMinecraft();
            return minecraft == null || minecraft.getRenderViewEntity() == null || minecraft.getRenderViewEntity().getDistanceSq(
                    this.posX, this.posY, this.posZ) >= 256.0D;
        }

        public void onUpdate() {
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                boolean flag = this.isFarFromCamera();
                boolean flag1 = false;

                if (this.fireworkExplosions.tagCount() >= 3) {
                    flag1 = true;
                }
                else {
                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);

                        if (nbttagcompound.getByte("Type") == 1) {
                            flag1 = true;
                            break;
                        }
                    }
                }

                SoundEvent soundevent1;

                if (flag1) {
                    soundevent1 = flag ? SoundEvents.ENTITY_FIREWORK_LARGE_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_LARGE_BLAST;
                }
                else {
                    soundevent1 = flag ? SoundEvents.ENTITY_FIREWORK_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_BLAST;
                }

                this.world.playSound(this.posX, this.posY, this.posZ, soundevent1, SoundCategory.AMBIENT, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
            }

            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
                int k = this.fireworkAge / 2;
                NBTTagCompound nbttagcompound1 = this.fireworkExplosions.getCompoundTagAt(k);
                int l = nbttagcompound1.getByte("Type");
                int size;
                if (nbttagcompound1.hasKey("Size")) {
                    size = nbttagcompound1.getInteger("Size");
                }else{
                    size = l == 1? 4 : (l > 4 ? 2 : 1);
                }
                size = Math.abs(size);
                boolean flag4 = nbttagcompound1.getBoolean("Trail");
                boolean flag2 = nbttagcompound1.getBoolean("Flicker");
                int[] aint = nbttagcompound1.getIntArray("Colors");
                int[] aint1 = nbttagcompound1.getIntArray("FadeColors");

                if (aint.length == 0) {
                    aint = new int[] {ItemDye.DYE_COLORS[0]};
                }
                FireworkShape shape = FireworkShape.values()[MathHelper.clamp(0,l,FireworkShape.values().length)];
                switch (shape) {
                    case LARGE_BALL:
                        if ((this.speed == 0d)) {
                            this.speed = 2d;
                        }
                        this.createBall(speed, size, aint, aint1, flag4, flag2);
                        break;
                    case STAR:
                        if ((this.speed == 0d)) {
                            this.speed = 0.5d;
                        }
                        this.createMirroredShaped(speed, size, new double[][]{
                                        {0.0D, 1.0D},
                                        {0.3455D, 0.309D},
                                        {0.9511D, 0.309D},
                                        {0.3795918367346939D, -0.12653061224489795D},
                                        {0.6122448979591837D, -0.8040816326530612D},
                                        {0.0D, -0.35918367346938773D}},
                                aint, aint1, flag4, flag2, false);
                        break;
                    case CREEPER:
                        if ((this.speed == 0d)) {
                            this.speed = 0.5d;
                        }
                        this.createMirroredShaped(speed, size, new double[][]{
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
                                {0.0D, -0.4D}}, aint, aint1, flag4, flag2, true);
                        break;
                    case BURST:
                        this.createBurst(aint, aint1, flag4, flag2);
                        break;
                    case TRIPLE_BALL:
                        if ((this.speed == 0d)) {
                            this.speed = 2d;
                        }
                        this.createBall(speed, size, aint, aint1, flag4, flag2);
                        this.createBall(speed / 2, size, aint, aint1, flag4, flag2);
                        this.createBall(speed / 4, size, aint, aint1, flag4, flag2);
                        break;
                    case RANDOM_BALL:
                        if ((this.speed == 0d)) {
                            this.speed = 2d;
                        }
                        this.createRandomBall(speed, size, aint, aint1, flag4, flag2);
                        break;
                    case CUSTOM_SHAPE:
                        if ((this.speed == 0d)) {
                            this.speed = 2d;
                        }
                        double[][] shapeArray = null;
                        if(nbttagcompound1.hasKey("Shape")){
                            NBTTagList shapeList = nbttagcompound1.getTagList("Shape", 9);
                            shapeArray = new double[shapeList.tagCount()][];
                            int indexA = 0;
                            for (NBTBase nbtBase : shapeList) {
                                if (nbtBase instanceof NBTTagList){
                                    NBTTagList shape2 = (NBTTagList) nbtBase;
                                    double[] shape2Array = new double[shape2.tagCount()];
                                    shapeArray[indexA] = shape2Array;
                                    int indexB = 0;
                                    for (NBTBase base : shape2) {
                                        if (base instanceof NBTTagDouble){
                                            shape2Array[indexB] = ((NBTTagDouble) base).getDouble();
                                        }
                                        indexB++;
                                    }
                                    indexA++;
                                }
                            }
                        }
                        if (shapeArray != null){
                            this.createShaped(speed, size, shapeArray, aint, aint1, flag4, flag2, true);
                        }else {
                            this.createShaped(speed, size, new double[][]{
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
                                    {0.0D, -0.4D}}, aint, aint1, flag4, flag2, true);
                        }
                        break;
                    case FONT:
                        if (nbttagcompound1.hasKey("Content")){
                            String content = nbttagcompound1.getString("Content");
                            this.createString(speed,size,content,aint,aint1,flag4,flag2,true);
                        }else {
                            this.createString(speed,size,"?",aint,aint1,flag4,flag2,true);
                        }
                        break;
                    default:
                        if ((this.speed == 0d)) {
                            this.speed = 0.25d;
                        }
                        this.createBall(speed, size, aint, aint1, flag4, flag2);
                        break;
                }

//                int j = aint[0];
//                float f = (float)((j & 16711680) >> 16) / 255.0F;
//                float f1 = (float)((j & 65280) >> 8) / 255.0F;
//                float f2 = (float)((j & 255)) / 255.0F;
//                ParticleSuperFirework.Overlay particlesuperfirework$overlay = new ParticleSuperFirework.Overlay(this.world, this.posX, this.posY, this.posZ);
//                particlesuperfirework$overlay.setRBGColorF(f, f1, f2);
//                this.manager.addEffect(particlesuperfirework$overlay);
            }

            ++this.fireworkAge;

            if (this.fireworkAge > this.particleMaxAge)
            {
                if (this.twinkle)
                {
                    boolean flag3 = this.isFarFromCamera();
                    SoundEvent soundevent = flag3 ? SoundEvents.ENTITY_FIREWORK_TWINKLE_FAR : SoundEvents.ENTITY_FIREWORK_TWINKLE;
                    this.world.playSound(this.posX, this.posY, this.posZ, soundevent, SoundCategory.AMBIENT, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
                }

                this.setExpired();
            }
        }

        /**
         * Renders the particle
         */
        public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        }

        /**
         * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite
         * sheet, 1 for the main Texture atlas, and 3 for a custom texture
         */
        public int getFXLayer() {
            return 0;
        }
    }

    public enum FireworkShape{
        SMALL_BALL,
        LARGE_BALL,
        STAR,
        CREEPER,
        BURST,
        TRIPLE_BALL,
        RANDOM_BALL,
        CUSTOM_SHAPE,
        FONT
    }
}