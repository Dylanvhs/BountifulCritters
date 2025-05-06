package net.dylanvhs.bountiful_critters.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {
    @Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
    public void tickPots(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(Blocks.DECORATED_POT)) cir.setReturnValue(true);
    }
}
