package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;
import net.dylanvhs.bountiful_critters.entity.custom.NeonTetraEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class NeonTetraRenderer extends GeoEntityRenderer<NeonTetraEntity> {

    private static final ResourceLocation LAYER = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/neon_tetra/neon_tetra_layer.png");

    public NeonTetraRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new NeonTetraModel());
        addRenderLayer(new NeonTetraRenderer.TetraTranslucentLayer(this));
        addRenderLayer(new NeonTetraRenderer.TetraGlowingLayer(this));
    }

    protected void scale(NeonTetraEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(NeonTetraEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/neon_tetra/neon_tetra.png");
    }

    public static class TetraTranslucentLayer extends GeoRenderLayer<NeonTetraEntity> {
        public TetraTranslucentLayer(GeoRenderer<NeonTetraEntity> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(NeonTetraEntity animatable) {
            return RenderType.entityTranslucent(LAYER);
        }

        @Override
        public void render(PoseStack poseStack, NeonTetraEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = getRenderType(animatable);

            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                    bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }

    public class TetraGlowingLayer extends GeoRenderLayer<NeonTetraEntity> {
        public TetraGlowingLayer(GeoRenderer<NeonTetraEntity> renderer) {
            super(renderer);
        }
        protected RenderType getRenderType(NeonTetraEntity animatable) {
            return AutoGlowingTexture.getRenderType(getTextureResource(animatable));
        }
        @Override
        public void render(PoseStack poseStack, NeonTetraEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = getRenderType(animatable);

            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                    bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }
}