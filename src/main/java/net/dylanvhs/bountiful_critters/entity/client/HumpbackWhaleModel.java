package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.HumpbackWhaleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HumpbackWhaleModel extends GeoModel<HumpbackWhaleEntity> {
    @Override
    public ResourceLocation getModelResource(HumpbackWhaleEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/humpback_whale.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HumpbackWhaleEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/humpback_whale.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HumpbackWhaleEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/humpback_whale.animation.json");
    }

    @Override
    public void setCustomAnimations(HumpbackWhaleEntity animatable, long instanceId, AnimationState<HumpbackWhaleEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("HumpbackWhale");
        if (animatable.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);
        } else {
            root.setScaleX(1.5F);
            root.setScaleY(1.5F);
            root.setScaleZ(1.5F);
        }
    }
}


