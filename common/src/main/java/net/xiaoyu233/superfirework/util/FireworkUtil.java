package net.xiaoyu233.superfirework.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.ExplosionTypes;

public class FireworkUtil {
    public static NbtCompound getRandomFireworkTag(Random rand){
        NbtCompound fireworkNBT = new NbtCompound();
        fireworkNBT.putInt("LifeTime",30 + rand.nextInt(20));
        NbtCompound fireworksItem = new NbtCompound();
        fireworksItem.putString("id","firework_rocket");
        fireworksItem.putInt("Count",1);
        NbtCompound tag = new NbtCompound();
        NbtCompound fireworks = new NbtCompound();
        NbtList explosions = new NbtList();
        NbtCompound singleExplosions = new NbtCompound();
        singleExplosions.putInt("Speed", 1 + rand.nextInt(4));
        singleExplosions.putString("Type", Util.getRandom(ExplosionTypes.NORMAL_EXPLOSION_TYPES, rand).getName());
        NbtIntArray colors = new NbtIntArray(new int[]{getRandomColor(rand)});
        singleExplosions.put("Colors",colors);
        singleExplosions.putBoolean("Explode",rand.nextBoolean());
        boolean trail = rand.nextBoolean();
        singleExplosions.putBoolean("Trail", trail);
        int size = rand.nextInt(15);
        if (trail) size /= 2;
        singleExplosions.putInt("Size", size);
        if (rand.nextBoolean()){
            NbtIntArray fadeColors = new NbtIntArray(new int[]{getRandomColor(rand)});
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

    public static int[] getRandomSingleColor(Random random){
        return new int[]{getRandomColor(random)};
    }
}
