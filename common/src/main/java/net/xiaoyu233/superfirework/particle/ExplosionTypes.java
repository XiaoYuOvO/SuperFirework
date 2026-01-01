package net.xiaoyu233.superfirework.particle;


import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.component.explosions.*;
import net.xiaoyu233.superfirework.particle.explosions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExplosionTypes {
    private static final Map<String, ExplosionType<?, ?>> EXPLOSION_TYPE_MAP = new HashMap<>();
    public static final ExplosionType<EmptyFireworkComponent, BallExplosion> BALL = register(ExplosionType.simple(BallExplosion::new, true, false, "ball"));
    public static final ExplosionType<EmptyFireworkComponent, BallExplosion> LARGE_BALL = register(ExplosionType.simple(ExplosionTypes::createLargeBall, true, true, "large_ball"));
    public static final ExplosionType<RandomBallComponent, RandomBallExplosion> RANDOM_BALL = register(ExplosionType.of(RandomBallExplosion::new, RandomBallComponent.CODEC, true, true, "random_ball"));
    public static final ExplosionType<EmptyFireworkComponent, TripleBallExplosion> TRIPLE_BALL = register(ExplosionType.simple(TripleBallExplosion::new, true, true, "triple_ball"));
    public static final ExplosionType<EmptyFireworkComponent, BurstExplosion> BURST = register(ExplosionType.simple(BurstExplosion::new, true, false, "burst"));
    public static final ExplosionType<EmptyFireworkComponent, WaterFallExplosion> WATER_FALL = register(ExplosionType.simple(WaterFallExplosion::new, true, false, "waterfall"));
    public static final ExplosionType<ShapeComponent, ShapedExplosion> CREEPER = register(ExplosionType.given(ShapedExplosion::new, ShapeComponent.CREEPER, true, false, "creeper"));
    public static final ExplosionType<ShapeComponent, ShapedExplosion> STAR = register(ExplosionType.given(ShapedExplosion::new, ShapeComponent.STAR, true, false, "star"));
    public static final ExplosionType<ShapeComponent, ShapedExplosion> CUSTOM = register(ExplosionType.of(ShapedExplosion::new, ShapeComponent.CODEC, false, true, "custom"));
    public static final ExplosionType<ImageComponent, ImageExplosion> IMAGE = register(ExplosionType.of(ImageExplosion::new, ImageComponent.CODEC, false, true, "image"));
    public static final ExplosionType<TextComponent, TextExplosion> TEXT = register(ExplosionType.of(TextExplosion::new, TextComponent.CODEC, false, true, "text"));
    public static final List<ExplosionType<?, ?>> EXPLOSION_TYPES = EXPLOSION_TYPE_MAP.values().stream().toList();
    public static final List<ExplosionType<?, ?>> NORMAL_EXPLOSION_TYPES = EXPLOSION_TYPE_MAP.values().stream().filter(ExplosionType::isNormal).toList();

    private static <T extends ExplosionType<?, ?>> T register(T explosionType){
        EXPLOSION_TYPE_MAP.put(explosionType.getName(), explosionType);
        return explosionType;
    }

    public static Optional<ExplosionType<?, ?>> getById(String id){
        return Optional.ofNullable(EXPLOSION_TYPE_MAP.get(id));
    }

    public static ExplosionType<?, ?>[] getExplosionTypes(){
        return EXPLOSION_TYPES.toArray(new ExplosionType[0]);
    }

    private static BallExplosion createLargeBall(ExplosionType<EmptyFireworkComponent, BallExplosion> type) {
        return new BallExplosion(type, 2);
    }
}
