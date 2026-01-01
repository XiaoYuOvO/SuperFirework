package net.xiaoyu233.superfirework.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;

public class CloneFireworkItem extends SuperFireworkItem {

    public CloneFireworkItem(Item.Settings builder) {
        super(builder);
    }


    @Override
    protected void configureFireworkEntity(ItemUsageContext context, ItemStack itemStack, SuperFireworkEntity fireworkRocketEntity) {
        super.configureFireworkEntity(context, itemStack, fireworkRocketEntity);
        fireworkRocketEntity.setClone(true);
    }
}
