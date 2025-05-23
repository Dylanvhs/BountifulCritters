package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.land.GeckoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeckoRenderer extends GeoEntityRenderer<GeckoEntity> {
    private static final ResourceLocation TEXTURE_LEOPARD = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/gecko/leopard_gecko.png");
    private static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/gecko/green_gecko.png");
    private static final ResourceLocation TEXTURE_TOKAY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/gecko/tokay_gecko.png");
    private static final ResourceLocation TEXTURE_ELECTRIC_BLUE_DAY = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/gecko/electric_blue_day_gecko.png");
    private static final ResourceLocation TEXTURE_YELLOW_HEADED = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/gecko/yellow_headed_gecko.png");

    public GeckoRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GeckoModel());
    }

    protected void scale(GeckoEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }


    public ResourceLocation getTextureLocation(GeckoEntity entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_GREEN;
            case 2 -> TEXTURE_TOKAY;
            case 3 -> TEXTURE_ELECTRIC_BLUE_DAY;
            case 4 -> TEXTURE_YELLOW_HEADED;
            default -> TEXTURE_LEOPARD;
        };
    }
}
