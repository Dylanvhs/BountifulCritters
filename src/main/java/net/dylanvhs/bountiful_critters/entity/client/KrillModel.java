package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KrillModel extends GeoModel<KrillEntity> {
    @Override
    public ResourceLocation getModelResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/krill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/krill.animation.json");
    }
}

