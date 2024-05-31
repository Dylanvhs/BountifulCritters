package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(StingrayEntity animatable, long instanceId, AnimationState<StingrayEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        if (animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D) {
            root.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
            root.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
        }
    }
}
