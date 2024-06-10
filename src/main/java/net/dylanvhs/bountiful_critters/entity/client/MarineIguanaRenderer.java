package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.MarineIguanaEntity;
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

public class MarineIguanaRenderer extends GeoEntityRenderer<MarineIguanaEntity> {

    private static final ResourceLocation TEXTURE_STONY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana.png");
    private static final ResourceLocation TEXTURE_NEON = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana_neon.png");
    private static final ResourceLocation TEXTURE_WARM = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana_1.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana_2.png");
    private static final ResourceLocation TEXTURE_ASH = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana_3.png");
    private static final ResourceLocation TEXTURE_GOJIRA = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/marine_iguana/marine_iguana_gojira.png");
    public MarineIguanaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarineIguanaModel());
        addRenderLayer(new IguanaGlowingLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MarineIguanaEntity animatable) {
        if(animatable.isName()){
            return TEXTURE_GOJIRA;
        } else return switch (animatable.getVariant()) {
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

    public class IguanaGlowingLayer extends GeoRenderLayer<MarineIguanaEntity> {
        public IguanaGlowingLayer(GeoRenderer<MarineIguanaEntity> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(MarineIguanaEntity animatable) {
            return AutoGlowingTexture.getRenderType(getTextureResource(animatable));
        }

        @Override
        public void render(PoseStack poseStack, MarineIguanaEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (animatable.getVariant() == 1 || animatable.isName()) {
                renderType = getRenderType(animatable);
                getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                        bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                        1, 1, 1, 1);
            }
        }
    }
}
