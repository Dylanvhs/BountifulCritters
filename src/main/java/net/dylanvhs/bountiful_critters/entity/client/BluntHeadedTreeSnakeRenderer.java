package net.dylanvhs.bountiful_critters.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.dylanvhs.bountiful_critters.entity.custom.ToucanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BluntHeadedTreeSnakeRenderer extends GeoEntityRenderer<BluntHeadedTreeSnakeEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/blunt_headed_tree_snake/blunt_headed_tree_snake.png");
    private static final ResourceLocation TEXTURE_MELINDA= new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/blunt_headed_tree_snake/blunt_headed_tree_snake_melinda.png");
    private static final ResourceLocation TEXTURE_BROWN_WHITE = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/blunt_headed_tree_snake/blunt_headed_tree_snake_1.png");
    private static final ResourceLocation TEXTURE_ORANGE_YELLOW = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/blunt_headed_tree_snake/blunt_headed_tree_snake_2.png");
    public BluntHeadedTreeSnakeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BluntHeadedTreeSnakeModel());
    }

    protected void scale(BluntHeadedTreeSnakeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }


    public ResourceLocation getTextureLocation(BluntHeadedTreeSnakeEntity entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_BROWN_WHITE;
            case 2 -> TEXTURE_ORANGE_YELLOW;
            case 3 -> TEXTURE_MELINDA;
            default -> TEXTURE;
        };
    }
}
