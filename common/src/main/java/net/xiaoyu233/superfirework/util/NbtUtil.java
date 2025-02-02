package net.xiaoyu233.superfirework.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public class NbtUtil {
    public static Optional<Double> getDouble(NbtCompound compound, String name){
        if (compound.contains(name, NbtElement.DOUBLE_TYPE)){
            return Optional.of(compound.getDouble(name));
        }
        return Optional.empty();
    }

    public static Optional<Integer> getInt(NbtCompound compound, String name){
        if (compound.contains(name, NbtElement.INT_TYPE)){
            return Optional.of(compound.getInt(name));
        }
        return Optional.empty();
    }

    public static Optional<int[]> getColor(NbtCompound compound, String name){
        if (compound.contains(name, NbtElement.INT_ARRAY_TYPE)){
            return Optional.of(compound.getIntArray(name));
        }
        return Optional.empty();
    }

    public static int[] ensureColor(int[] color, Random random){
        if (color.length == 0){
            return FireworkUtil.getRandomSingleColor(random);
        }
        return color;
    }
}
