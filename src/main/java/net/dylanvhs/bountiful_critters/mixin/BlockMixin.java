package net.dylanvhs.bountiful_critters.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    //will see if it'll fix the cause
//    @Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
//    public void tickPots(BlockState state, CallbackInfoReturnable<Boolean> cir) {
//        if (state.is(Blocks.DECORATED_POT)) cir.setReturnValue(true);
//    }
}