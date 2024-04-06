package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.EmuEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmuRenderer extends GeoEntityRenderer<EmuEntity> {
    public EmuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmuModel());
    }
    @Override
    public ResourceLocation getTextureLocation(EmuEntity animatable) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    }

    @Override
    public void render(EmuEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}