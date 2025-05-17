package net.dylanvhs.bountiful_critters.block;

import net.dylanvhs.bountiful_critters.RegistryHelper;
import net.dylanvhs.bountiful_critters.block.custom.RoastedPillbugBlock;
import net.dylanvhs.bountiful_critters.block.custom.SaltLampBlock;
import net.dylanvhs.bountiful_critters.block.custom.SeagrassBallBlock;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.ToIntFunction;

@SuppressWarnings("deprecation")
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = RegistryHelper.BLOCKS;
    public static final RegistryObject<Block> ROASTED_PILLBUG_BLOCK = RegistryHelper.ofBlock("roasted_pillbug_block", () ->
            new RoastedPillbugBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).noOcclusion()), false).build();

    public static final RegistryObject<Block> SALT_LAMP = RegistryHelper.ofBlock("salt_lamp", () ->
            new SaltLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.5F).sound(SoundType.GLASS).isValidSpawn(ModBlocks::always))).build();
    public static final RegistryObject<Block> SALT_BLOCK = RegistryHelper.ofBlock("salt_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> POLISHED_SALT_BLOCK = RegistryHelper.ofBlock("polished_salt_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.STONE))).build();


    public static final RegistryObject<Block> SEAGRASS_BALL_BLOCK = RegistryHelper.ofBlock("seagrass_ball_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WET_GRASS))).build();

    public static final RegistryObject<Block> DRIED_SEAGRASS_BALL_BLOCK = RegistryHelper.ofBlock("dried_seagrass_ball_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.GRASS))).build();

    public static final RegistryObject<Block> DRIED_SEAGRASS_BALL_CARPET = RegistryHelper.ofBlock("dried_seagrass_ball_carpet",
            () -> new CarpetBlock(BlockBehaviour.Properties.of().strength(0.1F).sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY))).build();


    public static final RegistryObject<Block> SEAGRASS_BALL_PLACED = RegistryHelper.ofBlock("seagrass_ball_placed",
            () -> new SeagrassBallBlock(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.WET_GRASS).replaceable().noCollission().noOcclusion()), false).build();


    public static final RegistryObject<Block> PILLBLOCK = RegistryHelper.ofBlock("pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> PILLBLOCK_SLAB = RegistryHelper.ofBlock("pillblock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK.get()))).build();
    public static final RegistryObject<Block> PILLBLOCK_STAIRS = RegistryHelper.ofBlock("pillblock_stairs",
            () -> new StairBlock(PILLBLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK.get()))).build();


    public static final RegistryObject<Block> PILLBLOCK_BRICKS = RegistryHelper.ofBlock("pillblock_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_SLAB = RegistryHelper.ofBlock("pillblock_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get()))).build();
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_STAIRS = RegistryHelper.ofBlock("pillblock_bricks_stairs",
            () -> new StairBlock(PILLBLOCK_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get()))).build();
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_WALL = RegistryHelper.ofBlock("pillblock_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get()).forceSolidOn())).build();


    public static final RegistryObject<Block> PILLBLOCK_SHINGLES = RegistryHelper.ofBlock("pillblock_shingles",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> PILLBLOCK_SHINGLES_SLAB = RegistryHelper.ofBlock("pillblock_shingles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_SHINGLES.get()))).build();
    public static final RegistryObject<Block> PILLBLOCK_SHINGLES_STAIRS = RegistryHelper.ofBlock("pillblock_shingles_stairs",
            () -> new StairBlock(PILLBLOCK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_SHINGLES.get()))).build();


    public static final RegistryObject<Block> CHISELED_PILLBLOCK = RegistryHelper.ofBlock("chiseled_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK = RegistryHelper.ofBlock("poisonous_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SLAB = RegistryHelper.ofBlock("poisonous_pillblock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK.get()))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_STAIRS = RegistryHelper.ofBlock("poisonous_pillblock_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK.get()))).build();


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS = RegistryHelper.ofBlock("poisonous_pillblock_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_SLAB = RegistryHelper.ofBlock("poisonous_pillblock_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_BRICKS.get()))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_STAIRS = RegistryHelper.ofBlock("poisonous_pillblock_bricks_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get()))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_WALL = RegistryHelper.ofBlock("poisonous_pillblock_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_BRICKS.get()).forceSolidOn())).build();


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES = RegistryHelper.ofBlock("poisonous_pillblock_shingles",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES_SLAB = RegistryHelper.ofBlock("poisonous_pillblock_shingles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_SHINGLES.get()))).build();
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES_STAIRS = RegistryHelper.ofBlock("poisonous_pillblock_shingles_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_SHINGLES.get()))).build();


    public static final RegistryObject<Block> CHISELED_POISONOUS_PILLBLOCK = RegistryHelper.ofBlock("chiseled_poisonous_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE))).build();


    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    private static Boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    private static Boolean always(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entityType) {
        return true;
    }

    public static void init() {
    }
}