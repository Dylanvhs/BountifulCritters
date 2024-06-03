package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.LionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LionRenderer extends GeoEntityRenderer<LionEntity> {
    public LionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LionModel());
    }
    private static final ResourceLocation DEFAULT = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion0.png");
    private static final ResourceLocation ARMORED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/lion/lion_armor.png");
    @Override
    public ResourceLocation getTextureLocation(LionEntity animatable) {
        if (animatable.isArmored()) {
            return ARMORED;
        } else return DEFAULT;
    }

    @Override
    public void render(LionEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
