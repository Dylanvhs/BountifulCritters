package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.LionEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LionModel extends GeoModel<LionEntity> {
    @Override
    public ResourceLocation getModelResource(LionEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/lion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LionEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion0.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LionEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/lion.animation.json");
    }

    @Override
    public void setCustomAnimations(LionEntity animatable, long instanceId, AnimationState<LionEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("Lion");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone mane = this.getAnimationProcessor().getBone("mane");
        if (animatable.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);

            mane.setScaleX(0.0F);
            mane.setScaleY(0.0F);
            mane.setScaleZ(0.0F);

            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        } else {
            root.setScaleX(1.0F);
            root.setScaleY(1.0F);
            root.setScaleZ(1.0F);

            mane.setScaleX(1.0F);
            mane.setScaleY(1.0F);
            mane.setScaleZ(1.0F);

            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        if (!animatable.isSprinting() && !animatable.isOrderedToSit()) {
            head.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
            head.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
        }
        if (!animatable.hasMane()) {
            mane.setScaleX(0.0F);
            mane.setScaleY(0.0F);
            mane.setScaleZ(0.0F);
        }
    }
}
