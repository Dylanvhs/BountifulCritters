package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EmuModel extends GeoModel<EmuEntity> {

    private static final ResourceLocation ADULT_MODEL = new ResourceLocation(BountifulCritters.MOD_ID, "geo/emu.geo.json");
    private static final ResourceLocation BABY_MODEL = new ResourceLocation(BountifulCritters.MOD_ID, "geo/baby_emu.geo.json");

    private static final ResourceLocation ADULT_ANIMATION = new ResourceLocation(BountifulCritters.MOD_ID, "animations/emu.animation.json");
    private static final ResourceLocation BABY_ANIMATION = new ResourceLocation(BountifulCritters.MOD_ID, "animations/baby_emu.animation.json");

    @Override
    public ResourceLocation getModelResource(EmuEntity animatable) {
        if (animatable.isBaby()) {
            return BABY_MODEL;
        } else return ADULT_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EmuEntity animatable) {
        if (animatable.isBaby()) {
            return BABY_ANIMATION;
        } else return ADULT_ANIMATION;
    }

    @Override
    public void setCustomAnimations(EmuEntity animatable, long instanceId, AnimationState<EmuEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        if (!animatable.isSprinting()) {
            head.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
            head.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
        }
    }



}