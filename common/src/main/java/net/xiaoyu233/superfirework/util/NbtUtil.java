package net.xiaoyu233.superfirework.util;

import it.unimi.dsi.fastutil.ints.IntList;
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

    public static Optional<IntList> getColor(NbtCompound compound, String name){
        if (compound.contains(name, NbtElement.INT_ARRAY_TYPE)){
            return Optional.of(IntList.of(compound.getIntArray(name)));
        }
        return Optional.empty();
    }

    public static IntList ensureColor(IntList color, Random random){
        if (color.isEmpty()){
            return FireworkUtil.getRandomSingleColor(random);
        }
        return color;
    }
}
