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
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 600), 0.5f).build();

    public static final FoodProperties RAW_GOLDEN_SUNFISH_MEAT = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.3f).build();

    public static final FoodProperties COOKED_GOLDEN_SUNFISH_MEAT = new FoodProperties.Builder().nutrition(8)
            .saturationMod(0.9f).effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 600), 1f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600), 1f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600), 1f).effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 600), 1f).effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 600), 1f).build();

    public static final FoodProperties SUNFISH_SUSHI = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.85f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200), 0.75f).effect(() -> new MobEffectInstance(MobEffects.LUCK, 2500), 0.5f).build();

    public static final FoodProperties RAW_KRILL = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).build();

    public static final FoodProperties FRIED_KRILL = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.4f).build();

    public static final FoodProperties KRILL_COCKTAIL = drink(6).build();

    public static final FoodProperties RAW_PILLBUG = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200), 0.5f).build();

    public static final FoodProperties POISONOUS_PILLBUG = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.POISON, 200), 1f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200), 1f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1f).build();

    public static final FoodProperties SALTED_KELP = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.5f).effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600), 0.75f).build();

    public static final FoodProperties RAW_ANGELFISH = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).build();

    public static final FoodProperties RAW_BARRELEYE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 400), 0.25f).build();

    public static final FoodProperties RAW_NEON_TETRA = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.15f).build();

    public static final FoodProperties RAW_FLOUNDER = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.35f).build();

    public static final FoodProperties RAW_PHEASANT = new FoodProperties.Builder().nutrition(4)
            .saturationMod(0.25f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200), 0.4f).build();

    public static final FoodProperties COOKED_PHEASANT = new FoodProperties.Builder().nutrition(7)
            .saturationMod(0.7f).build();


    private static FoodProperties.Builder drink(int pNutrition) {
        return (new FoodProperties.Builder()).nutrition(pNutrition).saturationMod(0.5F);
    }
}
