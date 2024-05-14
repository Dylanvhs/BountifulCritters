package net.dylanvhs.bountiful_critters.mixin;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void saveSnake(CompoundTag tag, CallbackInfo ci) {
        if (PotAccess.hasSnake(this.level, this.worldPosition)) {
            CompoundTag snaketag = new CompoundTag();
            PotAccess.getSnake(this.level, this.worldPosition).addAdditionalSaveData(snaketag);
            tag.put("snake_save", PotAccess.getSnake(this.level, this.worldPosition).saveWithoutId(new CompoundTag()));
            tag.put("snake_data", snaketag);
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void loadSnake(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("snake")) {
            BluntHeadedTreeSnakeEntity snake = new BluntHeadedTreeSnakeEntity(ModEntities.BLUNT_HEADED_TREE_SNAKE.get(),
                    this.level);
            snake.load(tag.getCompound("snake_save"));
            snake.readAdditionalSaveData(tag.getCompound("snake_data"));
            PotAccess.setSnake(this.level, this.worldPosition, snake);
        }
    }
}
