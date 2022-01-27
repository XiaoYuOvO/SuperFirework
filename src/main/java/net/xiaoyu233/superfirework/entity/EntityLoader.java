package net.xiaoyu233.superfirework.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.xiaoyu233.superfirework.SuperFirework;

public class EntityLoader {
    private static int nextID = 0;

    public static void registerEntities(){
        registerEntity(EntitySuperFirework.class, "super_firework", 80, 3, true);
        registerEntity(SubFireworkEntity.class,"sub_firework",80,3,true);
    }

    public static void registerEntityRenderers(){
        RenderingRegistry.registerEntityRenderingHandler(SubFireworkEntity.class, manager -> new RenderSnowball<>(manager, Items.FIREWORKS, Minecraft.getMinecraft().getRenderItem()));
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation("superfirework",name),entityClass,name, nextID++, SuperFirework.INSTANCE, trackingRange, updateFrequency,
                sendsVelocityUpdates);
    }
}
