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
    private static final ResourceLocation TEXTURE_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    private static final ResourceLocation TEXTURE_BABY_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/baby_emu.png");
    @Override
    public ResourceLocation getTextureLocation(EmuEntity animatable) {
        return switch (animatable.getModel()) {
            case 1 -> TEXTURE_BABY_EMU;
            default -> TEXTURE_EMU;
        };
    }

    @Override
    public void render(EmuEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}