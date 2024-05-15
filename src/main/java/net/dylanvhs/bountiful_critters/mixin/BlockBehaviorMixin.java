package net.dylanvhs.bountiful_critters.mixin;

import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.dylanvhs.bountiful_critters.event.PotEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin {
    @Inject(method = "randomTick", at = @At("HEAD"))
    public void tickPot(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.is(Blocks.DECORATED_POT) && PotAccess.hasSnake(pos)) PotEvents.potSnakeTick(world, pos);
    }
}
