package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.MarineIguanaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarineIguanaRenderer extends GeoEntityRenderer<MarineIguanaEntity> {

    private static final ResourceLocation TEXTURE_STONY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana.png");
    private static final ResourceLocation TEXTURE_NEON = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana_neon.png");
    private static final ResourceLocation TEXTURE_WARM = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana_1.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana_2.png");
    private static final ResourceLocation TEXTURE_ASH = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana_3.png");
    public MarineIguanaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarineIguanaModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MarineIguanaEntity animatable) {
        return switch (animatable.getVariant()) {
            case 1 -> TEXTURE_NEON;
            case 2 -> TEXTURE_WARM;
            case 3 -> TEXTURE_RED;
            case 4 -> TEXTURE_ASH;
            default -> TEXTURE_STONY;
        };
    }

    @Override
    public void render(MarineIguanaEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
