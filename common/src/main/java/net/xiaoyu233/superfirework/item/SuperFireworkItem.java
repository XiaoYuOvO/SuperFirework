package net.xiaoyu233.superfirework.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xiaoyu233.superfirework.component.SFComponents;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;
import net.xiaoyu233.superfirework.util.FireworkUtil;

public class SuperFireworkItem extends FireworkRocketItem {
    public SuperFireworkItem(Item.Settings builder) {
        super(builder);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            ItemStack itemStack = context.getStack();
            Vec3d vec3d = context.getHitPos();
            Direction direction = context.getSide();
            SuperFireworkEntity fireworkRocketEntity = new SuperFireworkEntity(world, context.getPlayer(), vec3d.x + (double)direction.getOffsetX() * 0.15, vec3d.y + (double)direction.getOffsetY() * 0.15, vec3d.z + (double)direction.getOffsetZ() * 0.15, itemStack);
            configureFireworkEntity(context, itemStack, fireworkRocketEntity);
            world.spawnEntity(fireworkRocketEntity);
            itemStack.decrement(1);
        }

        return ActionResult.success(world.isClient);
    }

    protected void configureFireworkEntity(ItemUsageContext context, ItemStack itemStack, SuperFireworkEntity fireworkRocketEntity) {
        if (itemStack.getComponents().get(SFComponents.SUPER_FIREWORK_COMPONENT.get()).explosions().isEmpty()) {
            fireworkRocketEntity.readCustomDataFromNbt(FireworkUtil.getRandomFireworkTag(context.getWorld().random));
        }
    }

}
