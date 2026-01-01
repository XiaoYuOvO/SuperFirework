package net.xiaoyu233.superfirework.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.xiaoyu233.superfirework.component.explosions.EmptyFireworkComponent;
import net.xiaoyu233.superfirework.network.SFPacketCodecs;
import net.xiaoyu233.superfirework.particle.explosions.FireworkExplosion;
import net.xiaoyu233.superfirework.particle.explosions.SimpleExplosion;

import java.util.function.Function;
import java.util.function.Supplier;

//TODO Use custom registry
public class ExplosionType<C, E extends FireworkExplosion<C>> implements StringIdentifiable {
    public static final PacketCodec<ByteBuf, ExplosionType<?, ?>> PACKET_CODEC = SFPacketCodecs.<ExplosionType<?, ?>>namedOptional(ExplosionTypes::getById, ExplosionType::asString, () -> ExplosionTypes.BALL);
    public static final Codec<ExplosionType<?, ?>> CODEC = StringIdentifiable.createBasicCodec(ExplosionTypes::getExplosionTypes);
    private final MapCodec<FireworkExplosion<C>> componentCodec;
    private final boolean normal;
    private final boolean largeBallSound;
    private final String name;

    public boolean largeBallSound(){
        return largeBallSound;
    }

    public ExplosionType(ExplosionCreator<C, E> creator, Codec<C> componentCodec, boolean normal, boolean largeBallSound, String name) {
        this.normal = normal;
        this.largeBallSound = largeBallSound;
        this.name = name;
        this.componentCodec = componentCodec.fieldOf("config").xmap(c -> creator.create(this, c), FireworkExplosion::getComponent);
    }

    public MapCodec<FireworkExplosion<C>> getCodec() {
        return componentCodec;
    }

    public boolean isNormal() {
        return normal;
    }

    public String getName() {
        return name;
    }

    public static <C, E extends FireworkExplosion<C>> ExplosionType<C, E> of(ExplosionCreator<C, E> creator, Codec<C> componentCodec, boolean normal, boolean largeBallSound, String name){
        return new ExplosionType<>(creator, componentCodec, normal, largeBallSound, name);
    }

    public static <E extends SimpleExplosion> ExplosionType<EmptyFireworkComponent, E> simple(Function<ExplosionType<EmptyFireworkComponent, E>, E> creator, boolean normal, boolean largeBallSound, String name){
        return new ExplosionType<>((t,c) -> creator.apply(t), EmptyFireworkComponent.CODEC, normal, largeBallSound, name);
    }

    public static <C, E extends FireworkExplosion<C>> ExplosionType<C, E> given(ExplosionCreator<C, E> creator, C component, boolean normal, boolean largeBallSound, String name){
        return new ExplosionType<>(creator, Codec.unit(component), normal, largeBallSound, name);
    }

    @Override
    public String asString() {
        return name;
    }

    public interface ExplosionCreator<C, E extends FireworkExplosion<C>>{
        E create(ExplosionType<C, E> type, C explosionComponent);
    }
}
