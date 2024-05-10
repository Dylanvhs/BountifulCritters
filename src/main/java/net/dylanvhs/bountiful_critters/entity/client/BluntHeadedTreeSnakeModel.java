package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BluntHeadedTreeSnakeModel extends GeoModel<BluntHeadedTreeSnakeEntity> {
    @Override
    public ResourceLocation getModelResource(BluntHeadedTreeSnakeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/blunt_headed_tree_snake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BluntHeadedTreeSnakeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/blunt_headed_tree_snake.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BluntHeadedTreeSnakeEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/blunt_headed_tree_snake.animation.json");
    }

    @Override
    public void setCustomAnimations(BluntHeadedTreeSnakeEntity animatable, long instanceId, AnimationState<BluntHeadedTreeSnakeEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone root = this.getAnimationProcessor().getBone("bluntheadedtreesnake");
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