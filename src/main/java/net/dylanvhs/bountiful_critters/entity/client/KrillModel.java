package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.KrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class KrillModel extends GeoModel<KrillEntity> {
    @Override
    public ResourceLocation getModelResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/krill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KrillEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/krill.animation.json");
    }

    @Override
    public void setCustomAnimations(KrillEntity animatable, long instanceId, AnimationState<KrillEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("Krill");
        if (animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D && animatable.isInWater()) {
            root.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
            root.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
        }
    }
}

