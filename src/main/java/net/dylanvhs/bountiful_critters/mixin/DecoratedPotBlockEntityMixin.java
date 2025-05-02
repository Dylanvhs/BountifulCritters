package net.dylanvhs.bountiful_critters.mixin;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity {
    // Because else the mixin gets grumpy
    public DecoratedPotBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    public void saveSnake(CompoundTag tag, CallbackInfo ci) {
        if (PotAccess.SNAKES.containsKey(this.worldPosition)) {
            PotAccess.appendTag(this.worldPosition, tag);
            BountifulCritters.LOGGER.info(String.valueOf(tag));
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void loadSnake(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("snake_save") && tag.contains("snake_data")) {
            CompoundTag snaketag = new CompoundTag();
            snaketag.put("snake_save", tag.get("snake_save"));
            snaketag.put("snake_data", tag.get("snake_data"));
            PotAccess.setSnake(this.worldPosition, snaketag);
        }
    }
}
