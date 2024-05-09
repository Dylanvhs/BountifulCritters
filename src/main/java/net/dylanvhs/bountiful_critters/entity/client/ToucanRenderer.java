package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.ToucanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ToucanRenderer extends GeoEntityRenderer<ToucanEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/toucan.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/toucan_red.png");
    public ToucanRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ToucanModel());
    }

    protected void scale(ToucanEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }


    public ResourceLocation getTextureLocation(ToucanEntity entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_RED;
            default -> TEXTURE;
        };
    }
}
