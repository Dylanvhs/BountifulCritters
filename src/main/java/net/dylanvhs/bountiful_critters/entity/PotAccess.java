package net.dylanvhs.bountiful_critters.entity;

import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.WorldDimensions;

import java.util.HashMap;
import java.util.Map;

public class PotAccess {
    private static final Map<ResourceKey<DimensionType>, Map<BlockPos, BluntHeadedTreeSnakeEntity>> SNAKES = new HashMap<>();

    public static boolean hasSnake(Level world, BlockPos pos) {
        return getSnake(world, pos) != null;
    }

    public static BluntHeadedTreeSnakeEntity getSnake(Level world, BlockPos pos) {
        ResourceKey<DimensionType> dim = world.dimensionTypeId();
        if (!SNAKES.containsKey(dim)) return null;
        if (!SNAKES.get(dim).containsKey(pos)) return null;
        return SNAKES.get(dim).get(pos);
    }

    public static void setSnake(Level world, BlockPos pos, BluntHeadedTreeSnakeEntity snake) {
        ResourceKey<DimensionType> dim = world.dimensionTypeId();
        SNAKES.putIfAbsent(dim, new HashMap<>());
        if (SNAKES.get(dim).containsKey(pos)) SNAKES.get(dim).replace(pos, snake);
        else SNAKES.get(dim).put(pos, snake);
    }
}
