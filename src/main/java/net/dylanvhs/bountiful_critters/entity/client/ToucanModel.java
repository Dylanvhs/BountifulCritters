package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.HumpbackWhaleEntity;
import net.dylanvhs.bountiful_critters.entity.custom.flying.ToucanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(ToucanEntity animatable, long instanceId, AnimationState<ToucanEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        CoreGeoBone root = this.getAnimationProcessor().getBone("Toucan");
        if (animatable.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);
        } else {
            root.setScaleX(1.0F);
            root.setScaleY(1.0F);
            root.setScaleZ(1.0F);
        }
    }

}
