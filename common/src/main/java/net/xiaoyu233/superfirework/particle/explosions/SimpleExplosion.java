package net.xiaoyu233.superfirework.particle.explosions;

import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;

public abstract class SimpleExplosion extends FireworkExplosion<EmptyFireworkComponent> {
    protected SimpleExplosion(ExplosionType<EmptyFireworkComponent, ? extends SimpleExplosion> type) {
        super(type, EmptyFireworkComponent.INSTANCE);
    }
}
