package net.dylanvhs.bountiful_critters.block.custom;

import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SeagrassBallBlock extends MultifaceBlock {

    public SeagrassBallBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.getItemInHand().is(ModItems.SEAGRASS_BALL.get()) || super.canBeReplaced(pState, pUseContext);
    }

    public MultifaceSpreader getSpreader() {
        return null;
    }
}



