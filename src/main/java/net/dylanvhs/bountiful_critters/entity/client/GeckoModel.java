package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.dylanvhs.bountiful_critters.entity.custom.GeckoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GeckoModel extends GeoModel<GeckoEntity> {
    @Override
    public ResourceLocation getModelResource(GeckoEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/gecko.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckoEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/leopard_gecko.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeckoEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/gecko.animation.json");
    }

    @Override
    public void setCustomAnimations(GeckoEntity animatable, long instanceId, AnimationState<GeckoEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone root = this.getAnimationProcessor().getBone("gecko");
        if (animatable.isBaby()) {
            root.setScaleX(0.4F);
            root.setScaleY(0.4F);
            root.setScaleZ(0.4F);

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