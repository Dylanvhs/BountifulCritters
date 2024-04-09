package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;
import net.dylanvhs.bountiful_critters.entity.custom.SunfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KrillRenderer extends GeoEntityRenderer<KrillEntity> {

    public KrillRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KrillModel());
    }

    protected void scale(SunfishEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(SunfishEntity entity) {
        return new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill.png");
    }
}
