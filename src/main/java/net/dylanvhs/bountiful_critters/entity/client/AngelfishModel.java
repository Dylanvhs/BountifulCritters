package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.AngelfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AngelfishModel extends GeoModel<AngelfishEntity> {
    @Override
    public ResourceLocation getModelResource(AngelfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/angelfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AngelfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/angelfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AngelfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/angelfish.animation.json");
    }
}
