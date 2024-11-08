package net.dylanvhs.bountiful_critters.item.custom;

import java.util.function.Predicate;

import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ModProjectileWeaponItem extends Item {
    public static final Predicate<ItemStack> PILLBUG_ONLY = (p_43017_) -> {
        return p_43017_.is(ModItems.PILLBUG_THROWABLE.get());
    };

    public ModProjectileWeaponItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }


    public abstract Predicate<ItemStack> getAllSupportedProjectiles();

    public static ItemStack getHeldProjectile(LivingEntity pShooter, Predicate<ItemStack> pIsAmmo) {
        if (pIsAmmo.test(pShooter.getItemInHand(InteractionHand.OFF_HAND))) {
            return pShooter.getItemInHand(InteractionHand.OFF_HAND);
        } else {
            return pIsAmmo.test(pShooter.getItemInHand(InteractionHand.MAIN_HAND)) ? pShooter.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public abstract int getDefaultProjectileRange();
}