package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;

import net.dylanvhs.bountiful_critters.entity.custom.HumpbackWhaleEntity;
import net.dylanvhs.bountiful_critters.entity.custom.MarineIguanaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HumpbackWhaleRenderer extends GeoEntityRenderer<HumpbackWhaleEntity> {
    public HumpbackWhaleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HumpbackWhaleModel());
    }

    @Override
    public ResourceLocation getTextureLocation(HumpbackWhaleEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/humpback_whale.png");
    }

    @Override
    public void render(HumpbackWhaleEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
