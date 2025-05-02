package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.PheasantEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PheasantRenderer extends GeoEntityRenderer<PheasantEntity> {
    public PheasantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PheasantModel());
    }
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/pheasant.png");
    @Override
    public ResourceLocation getTextureLocation(PheasantEntity animatable) {
         return TEXTURE;
    }

    @Override
    public void render(PheasantEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

