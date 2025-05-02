package net.dylanvhs.bountiful_critters.entity.client;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.StickyArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class StickyArrowRenderer extends ArrowRenderer<StickyArrowEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID,"textures/entity/sticky_arrow.png");

    public StickyArrowRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(StickyArrowEntity entity) {
        return TEXTURE;
    }
}
