package net.xiaoyu233.superfirework.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class SuperFireworkItem extends FireworkRocketItem {
    public SuperFireworkItem(Properties builder) {
        super(builder);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            ItemStack itemstack = context.getItem();
            Vector3d vector3d = context.getHitVec();
            Direction direction = context.getFace();
            SuperFireworkEntity fireworkrocketentity = new SuperFireworkEntity(world, context.getPlayer(), vector3d.x + (double)direction.getXOffset() * 0.15D, vector3d.y + (double)direction.getYOffset() * 0.15D, vector3d.z + (double)direction.getZOffset() * 0.15D, itemstack);
            if (!itemstack.hasTag()) {
                fireworkrocketentity.readAdditional(getRandomFireworkTag(context.getWorld().rand));
            }
            world.addEntity(fireworkrocketentity);
            itemstack.shrink(1);
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }


    public static CompoundNBT getRandomFireworkTag(Random rand){
        CompoundNBT fireworkNBT = new CompoundNBT();
        fireworkNBT.putInt("LifeTime",30 + rand.nextInt(20));
        CompoundNBT fireworksItem = new CompoundNBT();
        fireworksItem.putString("id","firework_rocket");
        fireworksItem.putInt("Count",1);
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT fireworks = new CompoundNBT();
        fireworks.putInt("Speed", 1 + rand.nextInt(4));
        ListNBT explosions = new ListNBT();
        CompoundNBT singleExplosions = new CompoundNBT();
        singleExplosions.putInt("Size",rand.nextInt(15));
        singleExplosions.putInt("Type",rand.nextInt(Shape.values().length));
        IntArrayNBT colors = new IntArrayNBT(new int[]{getRandomColor(rand)});
        singleExplosions.put("Colors",colors);
        if (rand.nextBoolean()){
            IntArrayNBT fadeColors = new IntArrayNBT(new int[]{getRandomColor(rand)});
            singleExplosions.put("FadeColors",fadeColors);
        }
        explosions.add(singleExplosions);
        fireworks.put("Explosions",explosions);
        tag.put("Fireworks",fireworks);
        fireworksItem.put("tag",tag);
        fireworkNBT.put("FireworksItem",fireworksItem);
        return fireworkNBT;
    }

    public static int getRandomColor(Random rand){
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return (r << 16) + (g << 8) + b;
    }

    public enum Shape {
        SMALL_BALL(0, "small_ball"),
        LARGE_BALL(1, "large_ball"),
        STAR(2, "star"),
        CREEPER(3, "creeper"),
        BURST(4, "burst"),
        TRIPLE_BALL(5,"triple_ball"),
        RANDOM_BALL(6,"random_ball"),
        CUSTOM_SHAPE(7,"custom_shape"),
        TEXT(8,"text"),
        IMAGE(9,"image");

        private static final SuperFireworkItem.Shape[] VALUES = Arrays.stream(values()).filter(shape -> shape.ordinal() < CUSTOM_SHAPE.ordinal()).sorted(Comparator.comparingInt((p_199796_0_) -> p_199796_0_.index)).toArray(Shape[]::new);
        private final int index;
        private final String shapeName;

        Shape(int indexIn, String nameIn) {
            this.index = indexIn;
            this.shapeName = nameIn;
        }

        public int getIndex() {
            return this.index;
        }

        @OnlyIn(Dist.CLIENT)
        public String getShapeName() {
            return this.shapeName;
        }

        @OnlyIn(Dist.CLIENT)
        public static SuperFireworkItem.Shape get(int indexIn) {
            return indexIn >= 0 && indexIn < VALUES.length ? VALUES[indexIn] : SMALL_BALL;
        }
    }

}
