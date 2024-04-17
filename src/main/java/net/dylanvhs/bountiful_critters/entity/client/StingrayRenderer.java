package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StingrayRenderer extends GeoEntityRenderer<StingrayEntity> {
    private static final ResourceLocation TEXTURE_GRAY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/stingray_0.png");
    private static final ResourceLocation TEXTURE_MUDDY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/stingray_1.png");
    private static final ResourceLocation TEXTURE_BLUE_SPOTTED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/stingray_2.png");


    public StingrayRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StingrayModel());
    }

    protected void scale(StingrayEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }


    public ResourceLocation getTextureLocation(StingrayEntity animatable) {
        return switch (animatable.getVariant()) {
            case 1 -> TEXTURE_MUDDY;
            case 2 -> TEXTURE_BLUE_SPOTTED;
            default -> TEXTURE_GRAY;
        };
    }
}
