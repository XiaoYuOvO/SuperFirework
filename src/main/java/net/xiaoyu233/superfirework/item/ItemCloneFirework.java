package net.xiaoyu233.superfirework.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;

import static net.xiaoyu233.superfirework.item.SuperFireworkItem.getRandomFireworkTag;

public class ItemCloneFirework extends FireworkRocketItem {

    public ItemCloneFirework(Properties builder) {
        super(builder);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            ItemStack itemstack = context.getItem();
            Vector3d vector3d = context.getHitVec();
            Direction direction = context.getFace();
            SuperFireworkEntity fireworkrocketentity = new SuperFireworkEntity(world, context.getPlayer(), vector3d.x + (double)direction.getXOffset() * 0.15D, vector3d.y + (double)direction.getYOffset() * 0.15D, vector3d.z + (double)direction.getZOffset() * 0.15D, itemstack);
            fireworkrocketentity.setClone(true);
            if (!itemstack.hasTag()) {
                fireworkrocketentity.read(getRandomFireworkTag(context.getWorld().rand));
            }
            world.addEntity(fireworkrocketentity);
            itemstack.shrink(1);
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }
}
