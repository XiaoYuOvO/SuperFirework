package net.xiaoyu233.superfirework.item;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.xiaoyu233.superfirework.Superfirework;
import net.xiaoyu233.superfirework.component.SFComponents;
import net.xiaoyu233.superfirework.component.SuperFireworkComponent;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.xiaoyu233.superfirework.util.FireworkUtil.getRandomFireworkTag;


public class SFItems {
    private static final Registrar<Item> ITEM_REGISTRAR = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.ITEM);
    public static final RegistrySupplier<SuperFireworkItem> SUPER_FIREWORK = ITEM_REGISTRAR.register(Superfirework.id("super_firework"),
            () -> new SuperFireworkItem(new Item.Settings().
                    component(SFComponents.SUPER_FIREWORK_COMPONENT.get(), new SuperFireworkComponent(1, List.of(), false))));
    public static final RegistrySupplier<Item> CLONE_FIREWORK = ITEM_REGISTRAR.register(Superfirework.id("clone_firework"),
            () -> new CloneFireworkItem(new Item.Settings().
                    component(SFComponents.SUPER_FIREWORK_COMPONENT.get(), new SuperFireworkComponent(1, List.of(), true))));

    //Static trigger
    public static void registerItems()  {
        registerDispenser();
    }

    public static void addCreativeGroupEntries(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries){
        entries.add(SUPER_FIREWORK.get());
        entries.add(CLONE_FIREWORK.get());
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenders(){
//        EntityRendererManager(SUPER_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"super_firework"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(CLONE_FIREWORK, 0, new ModelResourceLocation(new ResourceLocation(SuperFirework.MODID,"clone_firework"), "inventory"));
    }

    private static void registerDispenser(){
        SFItems.SUPER_FIREWORK.listen(superFireworkItem ->
                DispenserBlock.registerBehavior(superFireworkItem, new DispenserBehavior() {
                    @Override
                    @NotNull
                    public ItemStack dispense(BlockPointer source, ItemStack stack) {
                        SuperFireworkEntity firework = createFireworkEntity(source, stack);
                        source.world().spawnEntity(firework);
                        stack.decrement(1);
                        return stack;
                    }
        }));
    }

    private static @NotNull SuperFireworkEntity createFireworkEntity(BlockPointer source, ItemStack stack) {
        Direction enumfacing = source.state().get(DispenserBlock.FACING);
        BlockPos pos = source.pos();
        double d0 = pos.getX() + (double)enumfacing.getOffsetX();
        double d1 = (float) pos.getY() + 0.2F;
        double d2 = pos.getZ() + (double)enumfacing.getOffsetZ();
        SuperFireworkEntity entityfireworkrocket = new SuperFireworkEntity(source.world(),null, d0, d1, d2, stack);
        if (stack.getComponents().get(SFComponents.SUPER_FIREWORK_COMPONENT.get()).explosions().isEmpty()) {
            entityfireworkrocket.readCustomDataFromNbt(getRandomFireworkTag(source.world().random));
        }
        return entityfireworkrocket;
    }
}
