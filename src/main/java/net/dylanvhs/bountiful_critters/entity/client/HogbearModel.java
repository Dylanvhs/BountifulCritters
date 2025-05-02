package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.HogbearEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class HogbearModel extends GeoModel<HogbearEntity> {
    @Override
    public ResourceLocation getModelResource(HogbearEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/hogbear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HogbearEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/hogbear.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HogbearEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/hogbear.animation.json");
    }

    @Override
    public void setCustomAnimations(HogbearEntity animatable, long instanceId, AnimationState<HogbearEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        CoreGeoBone root = this.getAnimationProcessor().getBone("Hogbear");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        if (animatable.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);

            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);

        } else {
            root.setScaleX(1.0F);
            root.setScaleY(1.0F);
            root.setScaleZ(1.0F);

            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }
    }
}
