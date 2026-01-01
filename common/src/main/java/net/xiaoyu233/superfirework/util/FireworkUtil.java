package net.xiaoyu233.superfirework.util;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.Superfirework;
import net.xiaoyu233.superfirework.particle.ExplosionTypes;

public class FireworkUtil {
    public static NbtCompound getRandomFireworkTag(Random rand){
        NbtCompound fireworkNBT = new NbtCompound();
        fireworkNBT.putInt("LifeTime",30 + rand.nextInt(20));
        NbtCompound fireworksItem = new NbtCompound();
        fireworksItem.putString("id", Superfirework.MOD_ID + ":super_firework");
        fireworksItem.putInt("Count",1);
        NbtCompound tag = new NbtCompound();
        NbtCompound fireworks = new NbtCompound();
        NbtList explosions = new NbtList();
        NbtCompound singleExplosions = new NbtCompound();
        NbtCompound particleConfig = new NbtCompound();
        singleExplosions.putInt("speed", 1 + rand.nextInt(4));
        NbtCompound shape = new NbtCompound();
        shape.putString("type", Util.getRandom(ExplosionTypes.NORMAL_EXPLOSION_TYPES, rand).getName());
        shape.put("config", new NbtCompound());
        singleExplosions.put("shape", shape);
        NbtIntArray colors = new NbtIntArray(new int[]{getRandomColor(rand)});
        particleConfig.put("colors",colors);
        particleConfig.putBoolean("explode",rand.nextBoolean());
        boolean trail = rand.nextBoolean();
        particleConfig.putBoolean("trail", trail);
        int size = 1 + rand.nextInt(15);
        if (trail) size /= 2;
        singleExplosions.putInt("size", size);
        if (rand.nextBoolean()){
            NbtIntArray fadeColors = new NbtIntArray(new int[]{getRandomColor(rand)});
            particleConfig.put("fade_colors",fadeColors);
        }
        singleExplosions.put("particle",particleConfig);
        explosions.add(singleExplosions);
        fireworks.put("explosions",explosions);
        tag.put(Superfirework.MOD_ID + ":super_firework",fireworks);
        fireworksItem.put("components",tag);
        fireworkNBT.put("FireworksItem",fireworksItem);
        return fireworkNBT;
    }

    public static int getRandomColor(Random rand){
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return (r << 16) + (g << 8) + b;
    }

    public static IntList getRandomSingleColor(Random random){
        return IntList.of(getRandomColor(random));
    }
}
