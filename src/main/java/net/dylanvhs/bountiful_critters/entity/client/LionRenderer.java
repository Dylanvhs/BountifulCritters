package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.LionEntity;
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

import java.util.Map;

public class LionRenderer extends GeoEntityRenderer<LionEntity> {
    public LionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LionModel());
        addRenderLayer(new LionArmorLayer(this));
    }
    private static final ResourceLocation DEFAULT = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion0.png");
    private static final ResourceLocation WHITE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion1.png");
    private static final ResourceLocation DEFAULT_ARMORED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion0_armor.png");
    private static final ResourceLocation WHITE_ARMORED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion1_armor.png");
    private static final ResourceLocation ARMOR_LAYER = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion_armor_layer.png");
    private static final ResourceLocation ARMOR_LAYER_SLIGHTLY_DAMAGED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion_armor_layer_slightly_damaged.png");
    private static final ResourceLocation ARMOR_LAYER_DAMAGED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion_armor_layer_damaged.png");

    @Override
    public ResourceLocation getTextureLocation(LionEntity animatable) {
        if (animatable.isArmored() && animatable.getVariant() == 0) {
            return DEFAULT_ARMORED;
        }
        if (animatable.isArmored() && animatable.getVariant() == 1) {
            return WHITE_ARMORED;
        }
        return switch (animatable.getVariant()) {
            case 1 -> WHITE;
            default -> DEFAULT;
        };
    }

    @Override
    public void render(LionEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public static class LionArmorLayer extends GeoRenderLayer<LionEntity> {
        public LionArmorLayer(GeoRenderer<LionEntity> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(LionEntity animatable) {
            return RenderType.entityCutout(ARMOR_LAYER);
        }
        @Override
        public void render(PoseStack poseStack, LionEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
           if (animatable.isArmored()) {
               renderType = getRenderType(animatable);
               getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                       bufferSource.getBuffer(renderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                       1, 1, 1, 1);
           }
        }
    }
}
