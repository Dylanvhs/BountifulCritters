package net.dylanvhs.bountiful_critters.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    public void tickPots(CallbackInfoReturnable<Boolean> cir) {
        BlockBehaviour.BlockStateBase base = (BlockBehaviour.BlockStateBase) ((Object) this);
        if (base.is(Blocks.DECORATED_POT)) cir.setReturnValue(true);
    }
}