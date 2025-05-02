package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.HogbearEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HogbearRenderer extends GeoEntityRenderer<HogbearEntity> {
    public HogbearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HogbearModel());
    }

    @Override
    public ResourceLocation getTextureLocation(HogbearEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/hogbear.png");
    }

    @Override
    public void render(HogbearEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

