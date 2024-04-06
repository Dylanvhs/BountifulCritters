package net.dylanvhs.bountiful_critters.entity.client;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

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


}