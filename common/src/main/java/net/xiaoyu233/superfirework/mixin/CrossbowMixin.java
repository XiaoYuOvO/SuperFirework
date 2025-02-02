package net.xiaoyu233.superfirework.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    @Redirect(method = "loadProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private static Item redirectApplyInfinityToFirework(ItemStack instance){
        return instance.getItem() == Items.ARROW || instance.getItem() == Items.FIREWORK_ROCKET ? Items.ARROW : instance.getItem();
    }

    @Redirect(method = "loadProjectiles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"))
    private static boolean isCreativeMode(PlayerAbilities instance, LivingEntity entityIn, ItemStack stack){
        return EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 || instance.creativeMode;
    }
}
