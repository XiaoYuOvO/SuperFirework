package net.xiaoyu233.superfirework.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.xiaoyu233.superfirework.Superfirework;

import java.util.function.Supplier;

public class SFEntityTypes {
    private static final Registrar<EntityType<?>> ENTITY_TYPES = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.ENTITY_TYPE);
    public static final Supplier<EntityType<SuperFireworkEntity>> SUPER_FIREWORK = ENTITY_TYPES.register(Superfirework.id("super_firework"), SFEntityTypes::createSuperFirework);

    //For trigger static init
    public static void registerEntities(){}

    //Client Only
    public static void registerEntityRenderers(){
        EntityRendererRegistry.register(SUPER_FIREWORK, FireworkRocketEntityRenderer::new);
    }

    private static EntityType<SuperFireworkEntity> createSuperFirework() {
        return EntityType.Builder.create((EntityType.EntityFactory<SuperFireworkEntity>) SuperFireworkEntity::new, SpawnGroup.MISC)
                .build("super_firework");
    }
}
