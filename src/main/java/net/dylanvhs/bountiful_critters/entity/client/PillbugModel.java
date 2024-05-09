package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.PillbugEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PillbugModel extends GeoModel<PillbugEntity> {
    @Override
    public ResourceLocation getModelResource(PillbugEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/pillbug.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PillbugEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/pillbug.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PillbugEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/pillbug.animation.json");
    }

    @Override
    public void setCustomAnimations(PillbugEntity animatable, long instanceId, AnimationState<PillbugEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone root = this.getAnimationProcessor().getBone("pillbug");
        if (animatable.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);
        } else {
            root.setScaleX(1.0F);
            root.setScaleY(1.0F);
            root.setScaleZ(1.0F);
        }
    }



}
