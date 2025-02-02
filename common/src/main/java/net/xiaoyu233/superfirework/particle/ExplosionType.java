package net.xiaoyu233.superfirework.particle;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.explosions.FireworkExplosion;

public class ExplosionType<E extends FireworkExplosion> {
    private final ExplosionCreator<E> creator;
    private final boolean normal;
    private final boolean largeBallSound;
    private final String name;

    public boolean largeBallSound(){
        return largeBallSound;
    }

    public ExplosionType(ExplosionCreator<E> creator, boolean normal, boolean largeBallSound, String name) {
        this.creator = creator;
        this.normal = normal;
        this.largeBallSound = largeBallSound;
        this.name = name;
    }

    public boolean isNormal() {
        return normal;
    }

    public String getName() {
        return name;
    }

    public static <E extends FireworkExplosion> ExplosionType<E> of(ExplosionCreator<E> creator, boolean normal, boolean largeBallSound, String name){
        return new ExplosionType<>(creator, normal, largeBallSound, name);
    }

    public E create(ParticleManager particleManager, Random random,Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        return this.creator.create(particleManager, random, parentVec, speed, size, config, explosionTag);
    }

    public interface ExplosionCreator<E extends FireworkExplosion>{
        E create(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag);
    }
}
