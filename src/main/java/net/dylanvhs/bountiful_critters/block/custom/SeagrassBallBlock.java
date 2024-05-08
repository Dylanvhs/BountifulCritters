package net.dylanvhs.bountiful_critters.block.custom;

import net.dylanvhs.bountiful_critters.effect.ModMobEffects;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SeagrassBallBlock extends MultifaceBlock {

    public SeagrassBallBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.getItemInHand().is(ModItems.SEAGRASS_BALL.get()) || super.canBeReplaced(pState, pUseContext);
    }

    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    public MultifaceSpreader getSpreader() {
        return null;
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        Player player = pLevel.getNearestPlayer(pEntity, 1);
        player.addEffect(new MobEffectInstance(ModMobEffects.GLUED.get(), 60, 0, true, true));
    }
}



