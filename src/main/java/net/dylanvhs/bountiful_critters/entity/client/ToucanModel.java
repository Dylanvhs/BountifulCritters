package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.ToucanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ToucanModel extends GeoModel<ToucanEntity> {
    @Override
    public ResourceLocation getModelResource(ToucanEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/toucan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ToucanEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/toucan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ToucanEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/toucan.animation.json");
    }
}
