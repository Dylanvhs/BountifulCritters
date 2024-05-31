package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;
import net.dylanvhs.bountiful_critters.entity.custom.SunfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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

