package net.xiaoyu233.superfirework.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.xiaoyu233.superfirework.client.SubFireworkRenderer;
import net.xiaoyu233.superfirework.client.SuperFireworkRenderer;

public class EntityLoader {
    public static final EntityType<SuperFireworkEntity> SUPER_FIREWORK = EntityType.Builder.create((EntityType.IFactory<SuperFireworkEntity>)SuperFireworkEntity::new, EntityClassification.MISC ).build("super_firework");
    public static final EntityType<SubFireworkEntity> SUB_FIREWORK = EntityType.Builder.create((EntityType.IFactory<SubFireworkEntity>) SubFireworkEntity::new, EntityClassification.MISC ).build("sub_firework");

    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> entityRegistryEvent){
        entityRegistryEvent.getRegistry().registerAll(SUPER_FIREWORK.setRegistryName("super_firework"),SUB_FIREWORK.setRegistryName("sub_firework"));
//        registerEntity(SuperFireworkEntity.class, "super_firework", 80, 3, true);
//        registerEntity(SubFireworkEntity.class,"sub_firework",80,3,true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderers(){
        RenderingRegistry.registerEntityRenderingHandler(SUB_FIREWORK, manager -> new SubFireworkRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(SUPER_FIREWORK, manager -> new SuperFireworkRenderer(manager, Minecraft.getInstance().getItemRenderer()));
    }

}
