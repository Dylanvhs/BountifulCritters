package net.dylanvhs.bountiful_critters.item.custom;

import net.dylanvhs.bountiful_critters.entity.custom.projectile.StickyArrowEntity;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ModItemArrow extends ArrowItem {
    public ModItemArrow(Item.Properties group) {
        super(group);
    }

    public @NotNull AbstractArrow createArrow(@NotNull Level worldIn, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        if(this == ModItems.STICKY_ARROW.get()){
            Arrow arrowentity = new StickyArrowEntity(worldIn, shooter);
            arrowentity.setEffectsFromItem(stack);
            return arrowentity;
        }else {
            return super.createArrow(worldIn, stack, shooter);
        }
    }

}
