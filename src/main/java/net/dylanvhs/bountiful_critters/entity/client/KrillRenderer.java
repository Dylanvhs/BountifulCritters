package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.KrillEntity;
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

public class KrillRenderer extends GeoEntityRenderer<KrillEntity> {
    private static final ResourceLocation DEFAULT = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill/krill.png");
    private static final ResourceLocation DEFAULT_LAYER = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill/krill_layer.png");
    private static final ResourceLocation GRAUS = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill/krill_graus.png");
    private static final ResourceLocation GRAUS_LAYER = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill/krill_graus_layer.png");
    public KrillRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KrillModel());
        addRenderLayer(new KrillTranslucentLayer(this));
    }

    protected void scale(KrillEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(KrillEntity entity) {
        if(entity.isName()){
            return GRAUS;
        }else{
            return DEFAULT;
        }
    }
    public static class KrillTranslucentLayer extends GeoRenderLayer<KrillEntity> {
        public KrillTranslucentLayer(GeoRenderer<KrillEntity> renderer) {
            super(renderer);
        }
        protected RenderType getRenderType(KrillEntity animatable) {
            if(animatable.isName()){
                return RenderType.entityTranslucent(GRAUS_LAYER);
            }else{
                return RenderType.entityTranslucent(DEFAULT_LAYER);
            }
        }
        @Override
        public void render(PoseStack poseStack, KrillEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = getRenderType(animatable);

            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                    bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }
}
