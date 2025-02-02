package net.xiaoyu233.superfirework.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.xiaoyu233.superfirework.Superfirework;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;
import org.jetbrains.annotations.NotNull;

import static net.xiaoyu233.superfirework.util.FireworkUtil.getRandomFireworkTag;


public class SFItems {
    private static final Registrar<Item> ITEM_REGISTRAR = Superfirework.REGISTRAR_MANAGER.get(RegistryKeys.ITEM);
    public static final RegistrySupplier<SuperFireworkItem> SUPER_FIREWORK = ITEM_REGISTRAR.register(Superfirework.id("super_firework"), ()-> new SuperFireworkItem(new Item.Settings()));
    public static final RegistrySupplier<Item> CLONE_FIREWORK = ITEM_REGISTRAR.register(Superfirework.id("clone_firework"), ()-> new CloneFireworkItem(new Item.Settings()));

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
                        source.getWorld().spawnEntity(firework);
                        stack.decrement(1);
                        return stack;
                    }
        }));
    }

    private static @NotNull SuperFireworkEntity createFireworkEntity(BlockPointer source, ItemStack stack) {
        Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
        double d0 = source.getX() + (double)enumfacing.getOffsetX();
        double d1 = (float) source.getY() + 0.2F;
        double d2 = source.getZ() + (double)enumfacing.getOffsetZ();
        SuperFireworkEntity entityfireworkrocket = new SuperFireworkEntity(source.getWorld(),null, d0, d1, d2, stack);
        if (!stack.hasNbt()) {
            entityfireworkrocket.readCustomDataFromNbt(getRandomFireworkTag(source.getWorld().random));
        }
        return entityfireworkrocket;
    }
}
