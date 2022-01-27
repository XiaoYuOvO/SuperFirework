package net.xiaoyu233.superfirework.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xiaoyu233.superfirework.item.SuperFireworkItem;
import net.xiaoyu233.superfirework.util.Bitmap;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

@OnlyIn(Dist.CLIENT)
public class SuperFireworkParticle extends FireworkParticle {
    @OnlyIn(Dist.CLIENT)
    public static class Overlay extends SpriteTexturedParticle {
        private Overlay(ClientWorld world, double x, double y, double z) {
            super(world, x, y, z);
            this.maxAge = 4;
        }

        public IParticleRenderType getRenderType() {
            return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
            this.setAlphaF(0.6F - ((float)this.age + partialTicks - 1.0F) * 0.25F * 0.5F);
            super.renderParticle(buffer, renderInfo, partialTicks);
        }

        public float getScale(float scaleFactor) {
            return 7.1F * MathHelper.sin(((float)this.age + scaleFactor - 1.0F) * 0.25F * (float)Math.PI);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class OverlayFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public OverlayFactory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SuperFireworkParticle.Overlay SuperFireworkParticle$overlay = new SuperFireworkParticle.Overlay(worldIn, x, y, z);
            SuperFireworkParticle$overlay.selectSpriteRandomly(this.spriteSet);
            return SuperFireworkParticle$overlay;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Spark extends SimpleAnimatedParticle {
        private boolean trail;
        private boolean twinkle;
        private final ParticleManager effectRenderer;
        private float fadeColourRed;
        private float fadeColourGreen;
        private float fadeColourBlue;
        private boolean hasFadeColour;

        private Spark(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleManager particleManager, IAnimatedSprite spriteWithAge) {
            super(world, x, y, z, spriteWithAge, -0.004F);
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.effectRenderer = particleManager;
            this.particleScale *= 0.75F;
            this.maxAge = 48 + this.rand.nextInt(12);
            this.selectSpriteWithAge(spriteWithAge);
        }

        public void setTrail(boolean trailIn) {
            this.trail = trailIn;
        }

        public void setTwinkle(boolean twinkleIn) {
            this.twinkle = twinkleIn;
        }

        public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
            if (!this.twinkle || this.age < this.maxAge / 3 || (this.age + this.maxAge) / 3 % 2 == 0) {
                super.renderParticle(buffer, renderInfo, partialTicks);
            }

        }

        public void tick() {
            super.tick();
            if (this.trail && this.age < this.maxAge / 2 && (this.age + this.maxAge) % 2 == 0) {
                SuperFireworkParticle.Spark SuperFireworkParticle$spark = new SuperFireworkParticle.Spark(this.world, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.effectRenderer, this.spriteWithAge);
                SuperFireworkParticle$spark.setAlphaF(0.99F);
                SuperFireworkParticle$spark.setColor(this.particleRed, this.particleGreen, this.particleBlue);
                SuperFireworkParticle$spark.age = SuperFireworkParticle$spark.maxAge / 2;
                if (this.hasFadeColour) {
                    SuperFireworkParticle$spark.hasFadeColour = true;
                    SuperFireworkParticle$spark.fadeColourRed = this.fadeColourRed;
                    SuperFireworkParticle$spark.fadeColourGreen = this.fadeColourGreen;
                    SuperFireworkParticle$spark.fadeColourBlue = this.fadeColourBlue;
                }

                SuperFireworkParticle$spark.twinkle = this.twinkle;
                this.effectRenderer.addEffect(SuperFireworkParticle$spark);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SparkFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public SparkFactory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SuperFireworkParticle.Spark SuperFireworkParticle$spark = new SuperFireworkParticle.Spark(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, Minecraft.getInstance().particles, this.spriteSet);
            SuperFireworkParticle$spark.setAlphaF(0.99F);
            return SuperFireworkParticle$spark;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Starter extends MetaParticle {
        private int fireworkAge;
        private final ParticleManager manager;
        private ListNBT fireworkExplosions;
        private double speed;
        private boolean twinkle;

        public Starter(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleManager particleManager, @Nullable CompoundNBT p_i232391_15_) {
            super(world, x, y, z);
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.manager = particleManager;
            this.maxAge = 8;
            if (p_i232391_15_ != null) {
                this.fireworkExplosions = p_i232391_15_.getList("Explosions", 10);
                if (p_i232391_15_.contains("Speed")) {
                    this.speed = p_i232391_15_.getDouble("Speed");
                }
                if (this.fireworkExplosions.isEmpty()) {
                    this.fireworkExplosions = null;
                } else {
                    this.maxAge = this.fireworkExplosions.size() * 2 - 1;

                    for(int i = 0; i < this.fireworkExplosions.size(); ++i) {
                        CompoundNBT compoundnbt = this.fireworkExplosions.getCompound(i);
                        if (compoundnbt.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.maxAge += 15;
                            break;
                        }
                    }
                }
            }

        }

        public void tick() {
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                boolean flag = this.isFarFromCamera();
                boolean flag1 = false;
                if (this.fireworkExplosions.size() >= 3) {
                    flag1 = true;
                } else {
                    for(int i = 0; i < this.fireworkExplosions.size(); ++i) {
                        CompoundNBT compoundnbt = this.fireworkExplosions.getCompound(i);
                        if (FireworkRocketItem.Shape.get(compoundnbt.getByte("Type")) == FireworkRocketItem.Shape.LARGE_BALL) {
                            flag1 = true;
                            break;
                        }
                    }
                }

                SoundEvent soundevent1;
                if (flag1) {
                    soundevent1 = flag ? SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
                } else {
                    soundevent1 = flag ? SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST;
                }

                this.world.playSound(this.posX, this.posY, this.posZ, soundevent1, SoundCategory.AMBIENT, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
            }

            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.size()) {
                int k = this.fireworkAge / 2;
                CompoundNBT compoundnbt1 = this.fireworkExplosions.getCompound(k);
                int l = MathHelper.clamp(0,compoundnbt1.getByte("Type"),SuperFireworkItem.Shape.values().length);
                int size;
                if (compoundnbt1.contains("Size")) {
                    size = compoundnbt1.getInt("Size");
                }else{
                    size = l == 1? 4 : (l > 4 ? 2 : 1);
                }
                size = Math.abs(size);
                SuperFireworkItem.Shape fireworkrocketitem$shape = SuperFireworkItem.Shape.get(l);
                boolean flag4 = compoundnbt1.getBoolean("Trail");
                boolean flag2 = compoundnbt1.getBoolean("Flicker");
                int[] aint = compoundnbt1.getIntArray("Colors");
                int[] aint1 = compoundnbt1.getIntArray("FadeColors");
                if (aint.length == 0) {
                    aint = new int[]{DyeColor.BLACK.getFireworkColor()};
                }

                switch (fireworkrocketitem$shape) {
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
                        if (compoundnbt1.contains("RandomFactor")){
                            this.createRandomBall(speed, size,compoundnbt1.getDouble("RandomFactor"), aint, aint1, flag4, flag2);
                        }
                        this.createRandomBall(speed, size,1, aint, aint1, flag4, flag2);
                        break;
                    case CUSTOM_SHAPE:
                        if ((this.speed == 0d)) {
                            this.speed = 2d;
                        }
                        double[][] shapeArray = null;
                        if(compoundnbt1.contains("Shape")){
                            ListNBT shapeList = compoundnbt1.getList("Shape", 9);
                            shapeArray = new double[shapeList.size()][];
                            int indexA = 0;
                            for (INBT nbtBase : shapeList) {
                                if (nbtBase instanceof ListNBT){
                                    ListNBT shape2 = (ListNBT) nbtBase;
                                    double[] shape2Array = new double[shape2.size()];
                                    shapeArray[indexA] = shape2Array;
                                    int indexB = 0;
                                    for (INBT base : shape2) {
                                        if (base instanceof DoubleNBT){
                                            shape2Array[indexB] = ((DoubleNBT) base).getDouble();
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
                        }
                        break;
                    case TEXT:
                        double rotation = 0;
                        String font = "Default";
                        if (compoundnbt1.contains("Rotation")){
                            rotation = compoundnbt1.getDouble("Rotation");
                        }
                        if (compoundnbt1.contains("Font")){
                            font = compoundnbt1.getString("Font");
                        }
                        if (compoundnbt1.contains("Content")){
                            String content = compoundnbt1.getString("Content");
                            this.createString(speed,size,content,font,aint,aint1,rotation,flag4,flag2,true);
                        }else {
                            this.createString(speed,size,"?",font,aint,aint1,0,flag4,flag2,true);
                        }
                        break;
                    case IMAGE:
                        double imgRotation = 0;
                        double zoom = 1;
                        if (compoundnbt1.contains("Zoom")){
                            zoom = MathHelper.clamp(0,compoundnbt1.getDouble("Zoom"),10);
                        }
                        if (compoundnbt1.contains("Rotation")){
                            imgRotation = compoundnbt1.getDouble("Rotation");
                        }
                        if (compoundnbt1.contains("Name")){
                            String name = compoundnbt1.getString("Name");
                            this.createImage(speed,zoom,name.toLowerCase(),imgRotation,flag4,flag2,true);
                        }else {
                            this.createImage(speed,zoom,"-",0,flag4,flag2,true);
                        }
                        break;
                    default:
                        if ((this.speed == 0d)) {
                            this.speed = 0.25d;
                        }
                        this.createBall(speed, size, aint, aint1, flag4, flag2);
                        break;
                }

                int j = aint[0];
                float f = (float)((j & 16711680) >> 16) / 255.0F;
                float f1 = (float)((j & '\uff00') >> 8) / 255.0F;
                float f2 = (float)((j & 255)) / 255.0F;
                Particle particle = this.manager.addParticle(ParticleTypes.FLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                particle.setColor(f, f1, f2);
            }

            ++this.fireworkAge;
            if (this.fireworkAge > this.maxAge) {
                if (this.twinkle) {
                    boolean flag3 = this.isFarFromCamera();
                    SoundEvent soundevent = flag3 ? SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE;
                    this.world.playSound(this.posX, this.posY, this.posZ, soundevent, SoundCategory.AMBIENT, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
                }

                this.setExpired();
            }

        }

        private void createMirroredShaped(double speed, double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            double d0 = shape[0][0];
            double d1 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours, trail, twinkleIn);
            float f = this.rand.nextFloat() * (float)Math.PI;
            double d2 = p_92038_8_ ? 0.034D : 0.34D;

            for(int i = 0; i < 3; ++i) {
                double d3 = (double)f + (double)((float)i * (float)Math.PI) * d2;
                double d4 = d0;
                double d5 = d1;

                for(int j = 1; j < shape.length; ++j) {
                    double d6 = shape[j][0];
                    double d7 = shape[j][1];

                    for(double d8 = 0.25D; d8 <= 1.0D; d8 += 0.25D) {
                        double d9 = MathHelper.lerp(d8, d4, d6) * speed;
                        double d10 = MathHelper.lerp(d8, d5, d7) * speed;
                        double d11 = d9 * Math.sin(d3);
                        d9 = d9 * Math.cos(d3);

                        for(double d12 = -1.0D; d12 <= 1.0D; d12 += 2.0D) {
                            this.createParticle(this.posX, this.posY, this.posZ, d9 * d12, d10, d11 * d12, colours, fadeColours, trail, twinkleIn);
                        }
                    }

                    d4 = d6;
                    d5 = d7;
                }
            }
        }

        private boolean isFarFromCamera() {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft.gameRenderer.getActiveRenderInfo().getProjectedView().squareDistanceTo(this.posX, this.posY, this.posZ) >= 256.0D;
        }

        /**
         * Creates a single particle.
         */
        private void createParticle(double x, double y, double z, double motionX, double motionY, double motionZ, int[] sparkColors, int[] sparkColorFades, boolean hasTrail, boolean hasTwinkle) {
            SuperFireworkParticle.Spark SuperFireworkParticle$spark = (SuperFireworkParticle.Spark)this.manager.addParticle(ParticleLoader.SUPER_FIREWORK, x, y, z, motionX, motionY, motionZ);
            SuperFireworkParticle$spark.setTrail(hasTrail);
            SuperFireworkParticle$spark.setTwinkle(hasTwinkle);
            SuperFireworkParticle$spark.setAlphaF(0.99F);
            int i = this.rand.nextInt(sparkColors.length);
            SuperFireworkParticle$spark.setColor(sparkColors[i]);
            if (sparkColorFades.length > 0) {
                SuperFireworkParticle$spark.setColorFade(Util.getRandomInt(sparkColorFades, this.rand));
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

        private void createRandomBall(double speed, int size,double randomFactor, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn)
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
                        double randSpeed = rand.nextInt(6)/5d * randomFactor;
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
         * Creates a creeper-shaped or star-shaped explosion.
         */
        private void createMirroredShaped(double speed, int size,double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            double d0 = shape[0][0];
            double d1 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours,
                    trail, twinkleIn);
            float f = 0;
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
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours, trail, twinkleIn);
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
        private void createImage(double speed, double zoom,String name,double rotation, boolean trail, boolean twinkleIn, boolean p_92038_8_){
            BufferedImage image = Bitmap.loadImage(name);
            int width = image.getWidth();
            int height = image.getHeight();
            if (zoom != 1){
                width *= zoom;
                height *= zoom;
                image = Bitmap.zoomInImage(image,width,height);
            }
            double radRotation = Math.toRadians(rotation) - Math.PI/2;
            double yStep = speed * 2  / height;
            double xStep = speed * 2  / width;
            double vecX = speed;
            for (int wIndex = 0; wIndex < width; wIndex++) {
                double vecY = speed;
                for (int hIndex = 0; hIndex < height; hIndex++) {
                    int rgb = image.getRGB(wIndex, hIndex);
                    if (rgb != 0){
                        this.createParticle(this.posX, this.posY, this.posZ, vecX, vecY, vecX*MathHelper.cos((float) radRotation), new int[]{rgb}, new int[]{} , trail, twinkleIn);
                    }
                    vecY-=yStep;

                }
                vecX-=xStep;
            }
        }

        private void createString(double speed, int size,String content,String font, int[] colours, int[] fadeColours,double rotation, boolean trail, boolean twinkleIn, boolean p_92038_8_) {
            boolean[][] bitmap = Bitmap.getStringPixels(font, 0, Math.max(size,4), content);
            double radRotation = Math.toRadians(rotation) - Math.PI/2;
            if (bitmap.length != 0){
                double yStep = speed * 2 / bitmap.length;
                double xStep = speed * 2 / bitmap[0].length;

                double vecY = speed;
                for (boolean[] aLine : bitmap) {
                    double vecX = speed;
                    for (boolean bit : aLine) {
                        if (bit){
                            this.createParticle(this.posX, this.posY, this.posZ, vecX * 2, vecY, vecX*MathHelper.cos((float) radRotation), colours, fadeColours, trail, twinkleIn);
                        }
                        vecX-=xStep;
                    }
                    vecY-=yStep;
                }
            }
        }

    }
}
