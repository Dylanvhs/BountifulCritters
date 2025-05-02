package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.AngelfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AngelfishRenderer extends GeoEntityRenderer<AngelfishEntity> {

    public AngelfishRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AngelfishModel());
    }

    protected void scale(AngelfishEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(AngelfishEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/angelfish.png");
    }
}