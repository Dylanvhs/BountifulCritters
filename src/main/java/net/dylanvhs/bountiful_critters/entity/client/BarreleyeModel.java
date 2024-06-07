package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.BarreleyeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BarreleyeModel extends GeoModel<BarreleyeEntity> {
    @Override
    public ResourceLocation getModelResource(BarreleyeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/barreleye_fish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BarreleyeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/barreleye/barreleye_fish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BarreleyeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/barreleye_fish.animation.json");
    }

    @Override
    public void setCustomAnimations(BarreleyeEntity animatable, long instanceId, AnimationState<BarreleyeEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        if (animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D && animatable.isInWater()) {
            root.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
            root.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
        }
    }
}
