package net.xiaoyu233.superfirework.component.explosions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ShapeComponent(List<DoubleArrayList> shapeVertexes, boolean keepShape, boolean mirrored) {
    public static final ShapeComponent CREEPER = fromArray(new double[][]{
            {0.0D, 0.2D},
            {0.2D, 0.2D},
            {0.2D, 0.6D},
            {0.6D, 0.6D},
            {0.6D, 0.2D},
            {0.2D, 0.2D},
            {0.2D, 0.0D},
            {0.4D, 0.0D},
            {0.4D, -0.6D},
            {0.2D, -0.6D},
            {0.2D, -0.4D},
            {0.0D, -0.4D}
    }, true, true);

    public static final ShapeComponent STAR = fromArray(new double[][]{
            {0.0D, 1.0D},
            {0.3455D, 0.309D},
            {0.9511D, 0.309D},
            {0.3795918367346939D, -0.12653061224489795D},
            {0.6122448979591837D, -0.8040816326530612D},
            {0.0D, -0.35918367346938773D}}, false, true);

    public static final ShapeComponent DEFAULT = new ShapeComponent(List.of(DoubleArrayList.of(0, 0)), false, false);
    public static final Codec<ShapeComponent> CODEC = RecordCodecBuilder.<ShapeComponent>create(
            instance -> instance.group(
                    Codec.list(Codec.DOUBLE.listOf().xmap(DoubleArrayList::new, ArrayList::new))
                            .fieldOf("shape")
                            .forGetter(ShapeComponent::shapeVertexes),
                    Codec.BOOL.fieldOf("keep_shape").forGetter(ShapeComponent::keepShape),
                    Codec.BOOL.fieldOf("mirrored").forGetter(ShapeComponent::mirrored)
            ).apply(instance, ShapeComponent::new)
    ).orElse(CREEPER);
//    public static final PacketCodec<ByteBuf, ShapeComponent> PACKET_CODEC = PacketCodecs.<ByteBuf, IntArrayList, List<IntArrayList>>collection(
//            ArrayList::new, PacketCodecs.INTEGER.collect(PacketCodecs.toList()).xmap(IntArrayList::new, ArrayList::new)
//    ).xmap(ShapeComponent::new, ShapeComponent::shapeVertexes);

    private static ShapeComponent fromArray(double[][] array, boolean keepShape, boolean mirrored){
        return new ShapeComponent(Arrays.stream(array).map(DoubleArrayList::new).toList(), keepShape, mirrored);
    }
}
