package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.PheasantEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PheasantModel extends GeoModel<PheasantEntity> {
    @Override
    public ResourceLocation getModelResource(PheasantEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/pheasant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PheasantEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/pheasant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PheasantEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/pheasant.animation.json");
    }

    @Override
    public void setCustomAnimations(PheasantEntity animatable, long instanceId, AnimationState<PheasantEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone root = this.getAnimationProcessor().getBone("pheasant");

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

        if (!animatable.isSprinting()) {

            head.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
            head.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
        }
    }


}