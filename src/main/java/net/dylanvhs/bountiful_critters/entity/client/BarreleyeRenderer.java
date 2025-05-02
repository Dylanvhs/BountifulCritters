package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.BarreleyeEntity;
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

public class BarreleyeRenderer extends GeoEntityRenderer<BarreleyeEntity> {
    private static final ResourceLocation LAYER = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/barreleye/barreleye_fish_layer.png");

    public BarreleyeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BarreleyeModel());
        addRenderLayer(new BarreleyeTranslucentLayer(this));
    }

    protected void scale(BarreleyeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(BarreleyeEntity entity) {
       return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/barreleye/barreleye_fish.png");
    }

    public static class BarreleyeTranslucentLayer extends GeoRenderLayer<BarreleyeEntity> {
        public BarreleyeTranslucentLayer(GeoRenderer<BarreleyeEntity> renderer) {
            super(renderer);
        }
        protected RenderType getRenderType(BarreleyeEntity animatable) {
            return RenderType.entityTranslucent(LAYER);
        }
        @Override
        public void render(PoseStack poseStack, BarreleyeEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = getRenderType(animatable);

            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                    bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }
}
