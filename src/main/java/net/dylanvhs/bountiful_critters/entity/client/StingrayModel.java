package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StingrayModel extends GeoModel<StingrayEntity> {
    @Override
    public ResourceLocation getModelResource(StingrayEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/stingray.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StingrayEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/stingray_0.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StingrayEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/stingray.animation.json");
    }
}
