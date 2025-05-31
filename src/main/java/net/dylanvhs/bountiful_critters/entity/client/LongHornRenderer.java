package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.LonghornEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LongHornRenderer extends GeoEntityRenderer<LonghornEntity> {
    public LongHornRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LongHornModel());
    }
    private static final ResourceLocation TEXTURE_TEMPERATE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_temperate.png");
    private static final ResourceLocation TEXTURE_WARM = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_warm.png");
    private static final ResourceLocation TEXTURE_COLD = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_cold.png");

    @Override
    public ResourceLocation getTextureLocation(LonghornEntity animatable) {
        return switch (animatable.getVariant()) {
            case 1 -> TEXTURE_TEMPERATE;
            case 2 -> TEXTURE_COLD;
            default -> TEXTURE_WARM;
        };
    }

    @Override
    public void render(LonghornEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

