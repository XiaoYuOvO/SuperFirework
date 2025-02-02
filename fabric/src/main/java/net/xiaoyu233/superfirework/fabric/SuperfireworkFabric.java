package net.xiaoyu233.superfirework.fabric;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupEventsImpl;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.xiaoyu233.superfirework.Superfirework;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.superfirework.item.SFItems;

public final class SuperfireworkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Registry.register(Registries.ITEM_GROUP, Superfirework.id(Superfirework.MOD_ID), FabricItemGroup.builder().displayName(Text.translatable("item_group." + Superfirework.MOD_ID+ ".name")).icon(()-> SFItems.SUPER_FIREWORK.get().getDefaultStack()).entries(SFItems::addCreativeGroupEntries).build());
        Superfirework.init();
    }
}
