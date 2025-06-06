package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.flying.HoopoeEntity;
import net.dylanvhs.bountiful_critters.entity.custom.land.PheasantEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HoopoeModel extends GeoModel<HoopoeEntity> {
    @Override
    public ResourceLocation getModelResource(HoopoeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/hoopoe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HoopoeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/hoopoe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HoopoeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/hoopoe.animation.json");
    }

    @Override
    public void setCustomAnimations(HoopoeEntity animatable, long instanceId, AnimationState<HoopoeEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("neck");
        CoreGeoBone root = this.getAnimationProcessor().getBone("Hoopoe");

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