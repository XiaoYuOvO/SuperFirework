package net.xiaoyu233.superfirework.forge;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xiaoyu233.superfirework.Superfirework;
import net.xiaoyu233.superfirework.item.SFItems;

@Mod(Superfirework.MOD_ID)
public final class SuperfireworkNeoForge {
    public SuperfireworkNeoForge(IEventBus modEventBus) {
        // Run our common setup.
        Superfirework.init();

        DeferredRegister<ItemGroup> itemGroupDeferredRegister = DeferredRegister.create(RegistryKeys.ITEM_GROUP, Superfirework.MOD_ID);
        itemGroupDeferredRegister
                .register(Superfirework.MOD_ID, () -> ItemGroup.builder()
                        // Set name of tab to display
                        .displayName(Text.translatable("item_group." + Superfirework.MOD_ID+ ".name"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(SFItems.SUPER_FIREWORK.get()))
                        // Add default items to tab
                        .entries(SFItems::addCreativeGroupEntries)
                        .build());
        itemGroupDeferredRegister.register(modEventBus);
    }
}
