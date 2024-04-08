package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.SunfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SunfishRenderer extends GeoEntityRenderer<SunfishEntity> {
    private static final ResourceLocation TEXTURE_OCEAN = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/sunfish_0.png");
    private static final ResourceLocation TEXTURE_COLD = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/sunfish_1.png");

    public SunfishRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SunfishModel());
    }

    protected void scale(SunfishEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }


    public ResourceLocation getTextureLocation(SunfishEntity entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_COLD;
            default -> TEXTURE_OCEAN;
        };
    }
}
