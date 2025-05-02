package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.FlounderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlounderRenderer extends GeoEntityRenderer<FlounderEntity> {

    public FlounderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new FlounderModel());
    }

    protected void scale(FlounderEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(FlounderEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/flounder.png");
    }
}
