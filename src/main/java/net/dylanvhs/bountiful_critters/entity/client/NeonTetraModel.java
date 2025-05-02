package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.NeonTetraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NeonTetraModel extends GeoModel<NeonTetraEntity> {
    @Override
    public ResourceLocation getModelResource(NeonTetraEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "geo/neon_tetra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NeonTetraEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/neon_tetra/neon_tetra.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NeonTetraEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "animations/neon_tetra.animation.json");
    }
}
