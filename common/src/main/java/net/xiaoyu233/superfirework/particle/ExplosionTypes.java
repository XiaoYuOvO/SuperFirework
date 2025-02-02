package net.xiaoyu233.superfirework.particle;


import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.particle.explosions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExplosionTypes {
    private static final Map<String, ExplosionType<?>> EXPLOSION_TYPE_MAP = new HashMap<>();
    public static final ExplosionType<BallExplosion> BALL = register(ExplosionType.of(BallExplosion::new, true, false, "ball"));
    public static final ExplosionType<BallExplosion> LARGE_BALL = register(ExplosionType.of(ExplosionTypes::createLargeBall, true, true, "large_ball"));
    public static final ExplosionType<RandomBallExplosion> RANDOM_BALL = register(ExplosionType.of(RandomBallExplosion::new, true, true, "random_ball"));
    public static final ExplosionType<TripleBallExplosion> TRIPLE_BALL = register(ExplosionType.of(TripleBallExplosion::new, true, true, "triple_ball"));
    public static final ExplosionType<BurstExplosion> BURST = register(ExplosionType.of(BurstExplosion::new, true, false, "burst"));
    public static final ExplosionType<WaterFallExplosion> WATER_FALL = register(ExplosionType.of(WaterFallExplosion::new, true, false, "waterfall"));
    public static final ExplosionType<CreeperShapedExplosion> CREEPER = register(ExplosionType.of(CreeperShapedExplosion::new, true, false, "creeper"));
    public static final ExplosionType<StarShapedExplosion> STAR = register(ExplosionType.of(StarShapedExplosion::new, true, false, "star"));
    public static final ExplosionType<ImageExplosion> IMAGE = register(ExplosionType.of(ImageExplosion::new, false, true, "image"));
    public static final ExplosionType<TextExplosion> TEXT = register(ExplosionType.of(TextExplosion::new, false, true, "text"));
    public static final ExplosionType<CustomShapeExplosion> CUSTOM = register(ExplosionType.of(CustomShapeExplosion::new, false, true, "custom"));
    public static final List<ExplosionType<?>> EXPLOSION_TYPES = EXPLOSION_TYPE_MAP.values().stream().toList();
    public static final List<ExplosionType<?>> NORMAL_EXPLOSION_TYPES = EXPLOSION_TYPE_MAP.values().stream().filter(ExplosionType::isNormal).toList();

    private static <T extends ExplosionType<?>> T register(T explosionType){
        EXPLOSION_TYPE_MAP.put(explosionType.getName(), explosionType);
        return explosionType;
    }

    public static Optional<ExplosionType<?>> getById(String id){
        return Optional.ofNullable(EXPLOSION_TYPE_MAP.get(id));
    }

    private static BallExplosion createLargeBall(ParticleManager particleManager, Random random, Vec3d parentVec, double speed, int size, ParticleConfig config, NbtCompound explosionTag) {
        return new BallExplosion(particleManager, random, parentVec, speed * 2, size, config, explosionTag);
    }
}
