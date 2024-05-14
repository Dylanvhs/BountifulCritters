package net.dylanvhs.bountiful_critters.entity;

import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.WorldDimensions;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PotAccess {
    public static final Map<BlockPos, CompoundTag> SNAKES = new HashMap<>();

    // Returns the Snake hiding in the Pot at pos, or null if none is in
    public static BluntHeadedTreeSnakeEntity getSnake(Level world, BlockPos pos) {
        if (!SNAKES.containsKey(pos)) return null;

        CompoundTag tag = SNAKES.get(pos);

        BluntHeadedTreeSnakeEntity snake = new BluntHeadedTreeSnakeEntity(ModEntities.BLUNT_HEADED_TREE_SNAKE.get(), world);
        if (tag.contains("snake_save")) snake.load(tag.getCompound("snake_save"));
        if (tag.contains("snake_data")) snake.readAdditionalSaveData(tag.getCompound("snake_data"));
        snake.moveTo(pos, 0, 0);

        return snake;
    }

    // Adds the Snake's save data to the Pot's
    public static CompoundTag appendTag(BlockPos pos, CompoundTag tag) {
        if (SNAKES.containsKey(pos)) {
            CompoundTag snaketag = SNAKES.get(pos);
            if (snaketag.contains("snake_save")) tag.put("snake_save", snaketag.get("snake_save"));
            if (snaketag.contains("snake_data")) tag.put("snake_data", snaketag.get("snake_data"));
        }
        return tag;
    }

    // Saves the Snake to the Pot at pos
    public static void setSnake(BlockPos pos, @Nullable BluntHeadedTreeSnakeEntity snake) {
        if (snake == null) {
            SNAKES.remove(pos);
            return;
        }

        CompoundTag tag = new CompoundTag();
        CompoundTag snaketag = new CompoundTag();
        snake.addAdditionalSaveData(snaketag);
        tag.put("snake_save", snake.saveWithoutId(new CompoundTag()));
        tag.put("snake_data", snaketag);

        setSnake(pos, tag);
        snake.discard();
    }

    public static void setSnake(BlockPos pos, CompoundTag tag) {
        if (SNAKES.containsKey(pos)) SNAKES.replace(pos, tag);
        else SNAKES.put(pos, tag);
    }
}
