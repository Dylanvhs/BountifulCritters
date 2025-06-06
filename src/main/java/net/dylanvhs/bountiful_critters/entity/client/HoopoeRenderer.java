package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.flying.HoopoeEntity;
import net.dylanvhs.bountiful_critters.entity.custom.land.PheasantEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HoopoeRenderer extends GeoEntityRenderer<HoopoeEntity> {
    public HoopoeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HoopoeModel());
    }
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/hoopoe.png");
    @Override
    public ResourceLocation getTextureLocation(HoopoeEntity animatable) {
        return TEXTURE;
    }

    @Override
    public void render(HoopoeEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

