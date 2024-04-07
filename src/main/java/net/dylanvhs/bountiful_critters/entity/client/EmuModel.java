package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EmuModel extends GeoModel<EmuEntity> {
    @Override
    public ResourceLocation getModelResource(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/emu.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/emu.animation.json");
    }

    @Override
    public void setCustomAnimations(EmuEntity animatable, long instanceId, AnimationState<EmuEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone root = this.getAnimationProcessor().getBone("Emu");
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

            head.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
            head.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
        }
    }



}