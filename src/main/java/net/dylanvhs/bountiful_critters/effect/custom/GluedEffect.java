package net.dylanvhs.bountiful_critters.effect.custom;

import net.dylanvhs.bountiful_critters.effect.ModMobEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Objects;

public class GluedEffect extends MobEffect {
    public GluedEffect() {
        super(MobEffectCategory.HARMFUL, 5882118);
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    public String getDescriptionId() {
        return "bountiful_critters.potion.glued";
    }

}
