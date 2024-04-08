package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.SunfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SunfishModel extends GeoModel<SunfishEntity> {
    @Override
    public ResourceLocation getModelResource(SunfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/sunfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SunfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/sunfish_0.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SunfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/sunfish.animation.json");
    }
}

