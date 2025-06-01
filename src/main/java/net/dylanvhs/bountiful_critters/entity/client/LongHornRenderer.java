package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.LionEntity;
import net.dylanvhs.bountiful_critters.entity.custom.land.LonghornEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class LongHornRenderer extends GeoEntityRenderer<LonghornEntity> {
    public LongHornRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LongHornModel());
        addRenderLayer(new LongHornRenderer.TameOverlay(this));
    }
    private static final ResourceLocation TEXTURE_TEMPERATE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_temperate.png");
    private static final ResourceLocation TEXTURE_WARM = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_warm.png");
    private static final ResourceLocation TEXTURE_COLD = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/longhorn_cold.png");

    private static final ResourceLocation TAME_OVERLAY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/tamed_overlay.png");
    private static final ResourceLocation TAME_OVERLAY_COLD = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/longhorn/tamed_overlay_cold.png");


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

    public static class TameOverlay extends GeoRenderLayer<LonghornEntity> {
        public TameOverlay(GeoRenderer<LonghornEntity> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(LonghornEntity animatable) {
            if (animatable.getVariant() == 2) {
                return RenderType.entityCutout(TAME_OVERLAY_COLD);
            } else return RenderType.entityCutout(TAME_OVERLAY);

        }
        @Override
        public void render(PoseStack poseStack, LonghornEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (animatable.isTame()) {
                renderType = getRenderType(animatable);
                getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                        bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                        1, 1, 1, 1);
            }
        }
    }
}

