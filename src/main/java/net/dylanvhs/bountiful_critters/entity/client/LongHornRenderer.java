package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.LonghornEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LongHornRenderer extends GeoEntityRenderer<LonghornEntity> {
    public LongHornRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LongHornModel());
    }
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/long_horn.png");
    private static final ResourceLocation TEXTURE_TAMED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/long_horn_tamed.png");
    @Override
    public ResourceLocation getTextureLocation(LonghornEntity animatable) {
        if (animatable.isTame()) {
            return TEXTURE_TAMED;
        } else return TEXTURE;
    }

    @Override
    public void render(LonghornEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

