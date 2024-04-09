package net.dylanvhs.bountiful_critters.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties BOILED_EMU_EGG = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.6f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200), 0.5f).build();

    public static final FoodProperties RAW_SUNFISH_MEAT = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.3f).build();

    public static final FoodProperties COOKED_SUNFISH_MEAT = new FoodProperties.Builder().nutrition(7)
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 600), 0.8f).build();

    public static final FoodProperties RAW_KRILL = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).build();

    public static final FoodProperties FRIED_KRILL = new FoodProperties.Builder().nutrition(4)
            .saturationMod(0.4f).build();
}
