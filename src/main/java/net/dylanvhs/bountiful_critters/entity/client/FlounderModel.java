package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.FlounderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlounderModel extends GeoModel<FlounderEntity> {
    @Override
    public ResourceLocation getModelResource(FlounderEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/flounder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FlounderEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/flounder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FlounderEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/flounder.animation.json");
    }
}
