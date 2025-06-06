package net.dylanvhs.bountiful_critters.world.gen.features;

import net.dylanvhs.bountiful_critters.block.ModBlockEntities;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import com.mojang.serialization.Codec;

import java.util.List;

public class OakNestLogDecorator extends TreeDecorator {

    public static final OakNestLogDecorator INSTANCE = new OakNestLogDecorator();
    public static final Codec<OakNestLogDecorator> CODEC = Codec.unit(() -> INSTANCE);

    private boolean placeBirtDwelling(Context context, List<BlockPos> list, int index, RandomSource random) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos pos = list.get(index);
        if (!context.level().isStateAtPosition(pos.above(), this::isOakNest) && !context.level().isStateAtPosition(pos, this::isOakNest) && context.level().isStateAtPosition(pos.relative(direction), BlockBehaviour.BlockStateBase::isAir)) {
            context.setBlock(pos, ModBlocks.OAK_NEST.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(context.random())));
            context.level().getBlockEntity(pos, ModBlockEntities.OAK_NEST.get()).ifPresent((blockEntity) -> {
                int i = 2 + random.nextInt(2);
                for (int j = 0; j < i; ++j) {
                    CompoundTag nbtCompound = new CompoundTag();
                    nbtCompound.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(ModEntities.HOOPOE.get()).toString());
                    blockEntity.addHoopoe(nbtCompound, random.nextInt(599));
                }

            });
            return true;
        } else {
            return index != 1 && this.placeBirtDwelling(context, list, index - 1, random);
        }
    }

    private boolean isOakNest(BlockState blockState) {
        return blockState.is(ModBlocks.OAK_NEST.get());
    }


    @Override
    protected TreeDecoratorType<?> type() {
        return null;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        int count = context.leaves().size() > 6 ? 3 : 2;
        for (int i = 0; i < count; i++) {
            this.placeBirtDwelling(context, context.logs(), Mth.nextInt(random, 3, context.logs().size() - 1), random);
        }
    }
}
