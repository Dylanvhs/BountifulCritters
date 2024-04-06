package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EmuModel extends GeoModel<EmuEntity> {
    private static final ResourceLocation MODEL_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "geo/emu.geo.json");
    private static final ResourceLocation MODEL_BABY_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "geo/baby_emu.geo.json");

    private static final ResourceLocation ANIMATION_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "animations/emu.animation.json");
    private static final ResourceLocation ANIMATION_BABY_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "animations/baby_emu.animation.json");
    @Override
    public ResourceLocation getModelResource(EmuEntity animatable) {
        return switch (animatable.getModel()) {
            case 1 -> MODEL_BABY_EMU;
            default -> MODEL_EMU;
        };
    }

    @Override
    public ResourceLocation getTextureResource(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EmuEntity animatable) {
        return switch (animatable.getModel()) {
            case 1 -> ANIMATION_BABY_EMU;
            default -> ANIMATION_EMU;
        };
    }


}