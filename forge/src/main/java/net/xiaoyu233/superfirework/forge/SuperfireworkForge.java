package net.xiaoyu233.superfirework.forge;

import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xiaoyu233.superfirework.Superfirework;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xiaoyu233.superfirework.client.SuperFireworkClient;
import net.xiaoyu233.superfirework.entity.SFEntityTypes;
import net.xiaoyu233.superfirework.item.SFItems;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import net.xiaoyu233.superfirework.particle.SuperFireworkParticle;

@Mod(Superfirework.MOD_ID)
public final class SuperfireworkForge {
    public SuperfireworkForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Superfirework.MOD_ID, modEventBus);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::registerParticle);
        modEventBus.addListener(this::registerEntityRenderer);
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        SuperFireworkClient.clientInit();
    }

    private void registerEntityRenderer(final EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(SFEntityTypes.SUPER_FIREWORK.get(), FireworkRocketEntityRenderer::new);
    }

    private void registerParticle(final RegisterParticleProvidersEvent event){
        event.registerSpriteSet(SFParticleTypes.SUPER_FIREWORK.get(), SuperFireworkParticle.ExplosionFactory::new);
    }
}
