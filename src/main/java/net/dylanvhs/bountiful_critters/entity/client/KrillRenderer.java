package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.KrillEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KrillRenderer extends GeoEntityRenderer<KrillEntity> {
    private static final ResourceLocation DEFAULT = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill.png");
    private static final ResourceLocation GRAUS = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/krill_graus.png");
    public KrillRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KrillModel());
    }

    protected void scale(KrillEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(KrillEntity entity) {
        if(entity.isName()){
            return GRAUS;
        }else{
            return DEFAULT;
        }
    }
}
