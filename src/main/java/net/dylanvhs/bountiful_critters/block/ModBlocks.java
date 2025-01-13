package net.dylanvhs.bountiful_critters.block;

import net.dylanvhs.bountiful_critters.BountifulCritters;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            BountifulCritters.MOD_ID);
    public static final RegistryObject<Block> ROASTED_PILLBUG_BLOCK = registerBlock("roasted_pillbug_block", () ->
            new RoastedPillbugBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).noOcclusion()));

    public static final RegistryObject<Block> SALT_LAMP = registerBlock("salt_lamp", () ->
            new SaltLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.5F).sound(SoundType.GLASS).isValidSpawn(ModBlocks::always)));
    public static final RegistryObject<Block> SALT_BLOCK = registerBlock("salt_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> POLISHED_SALT_BLOCK = registerBlock("polished_salt_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.STONE)));


    public static final RegistryObject<Block> SEAGRASS_BALL_BLOCK = registerBlock("seagrass_ball_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WET_GRASS)));

    public static final RegistryObject<Block> DRIED_SEAGRASS_BALL_BLOCK = registerBlock("dried_seagrass_ball_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> DRIED_SEAGRASS_BALL_CARPET = registerBlock("dried_seagrass_ball_carpet",
            () -> new   CarpetBlock(BlockBehaviour.Properties.of().strength(0.1F).sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));


    public static final RegistryObject<Block> SEAGRASS_BALL_PLACED = registerBlock("seagrass_ball_placed",
            () -> new SeagrassBallBlock(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.WET_GRASS).replaceable().noCollission().noOcclusion()));


    public static final RegistryObject<Block> PILLBLOCK = registerBlock("pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PILLBLOCK_SLAB = registerBlock("pillblock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK.get())));
    public static final RegistryObject<Block> PILLBLOCK_STAIRS = registerBlock("pillblock_stairs",
            () -> new StairBlock(PILLBLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK.get())));


    public static final RegistryObject<Block> PILLBLOCK_BRICKS = registerBlock("pillblock_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_SLAB = registerBlock("pillblock_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get())));
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_STAIRS = registerBlock("pillblock_bricks_stairs",
            () -> new StairBlock(PILLBLOCK_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get())));
    public static final RegistryObject<Block> PILLBLOCK_BRICKS_WALL = registerBlock("pillblock_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get()).forceSolidOn()));


    public static final RegistryObject<Block> PILLBLOCK_SHINGLES = registerBlock("pillblock_shingles",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PILLBLOCK_SHINGLES_SLAB = registerBlock("pillblock_shingles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_SHINGLES.get())));
    public static final RegistryObject<Block> PILLBLOCK_SHINGLES_STAIRS = registerBlock("pillblock_shingles_stairs",
            () -> new StairBlock(PILLBLOCK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_SHINGLES.get())));


    public static final RegistryObject<Block> CHISELED_PILLBLOCK = registerBlock("chiseled_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK = registerBlock("poisonous_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SLAB = registerBlock("poisonous_pillblock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK.get())));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_STAIRS = registerBlock("poisonous_pillblock_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK.get())));


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS = registerBlock("poisonous_pillblock_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_SLAB = registerBlock("poisonous_pillblock_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_BRICKS.get())));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_STAIRS = registerBlock("poisonous_pillblock_bricks_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.PILLBLOCK_BRICKS.get())));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_BRICKS_WALL = registerBlock("poisonous_pillblock_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_BRICKS.get()).forceSolidOn()));


    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES = registerBlock("poisonous_pillblock_shingles",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES_SLAB = registerBlock("poisonous_pillblock_shingles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_SHINGLES.get())));
    public static final RegistryObject<Block> POISONOUS_PILLBLOCK_SHINGLES_STAIRS = registerBlock("poisonous_pillblock_shingles_stairs",
            () -> new StairBlock(POISONOUS_PILLBLOCK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.POISONOUS_PILLBLOCK_SHINGLES.get())));



    public static final RegistryObject<Block> CHISELED_POISONOUS_PILLBLOCK = registerBlock("chiseled_poisonous_pillblock",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.75F).sound(SoundType.STONE)));


    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block, Function<Supplier<T>, Item> item) {
        Supplier<T> entry = create(key, block);
        ModItems.ITEMS.register(key, () -> item.apply(entry));
        return entry;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block, CreativeModeTab tab) {
        return create(key, block, entry -> new BlockItem(entry.get(), new Item.Properties()));
    }

    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block) {
        return BLOCKS.register(key, block);
    }

    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> {
            return p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
        };
    }

    private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return (boolean)false;
    }

    private static Boolean always(BlockState p_50810_, BlockGetter p_50811_, BlockPos p_50812_, EntityType<?> p_50813_) {
        return (boolean)true;
    }
}