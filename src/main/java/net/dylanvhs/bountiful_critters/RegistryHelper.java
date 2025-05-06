package net.dylanvhs.bountiful_critters;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryHelper {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BountifulCritters.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BountifulCritters.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BountifulCritters.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        ENTITIES.register(bus);
    }
    public static class BlockBuilder {
        public BlockBuilder(String name, Supplier<? extends Block> block, boolean doubleBlock, boolean item) {
            this.name = name;
            this.block = doubleBlock ? registerDoubleBlock(name, block, item) : registerBlock(name, block, item);
        }

        public RegistryObject<? extends Block> build() {
            return this.block;
        }

        public String name;
        public RegistryObject<? extends Block> block;

        public BlockBuilder drop() {
            return this.drop(this.block.get());
        }

        public BlockBuilder drop(ItemLike loot) {
            BLOCK_DROPS.putIfAbsent(this.block, loot);
            return this;
        }

        public BlockBuilder tag(TagKey<Block> tagname) {
            BLOCK_TAGS.putIfAbsent(tagname, new ArrayList<>());
            BLOCK_TAGS.get(tagname).add(() -> this.block.get());
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tag(TagKey<Block>... tags) {
            for (TagKey<Block> tagname : tags) {
                this.tag(tagname);
            }
            return this;
        }

        public BlockBuilder tagitem(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.block.get().asItem());
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tagitem(TagKey<Item>... tags) {
            for (TagKey<Item> tagname : tags) {
                this.tagitem(tagname);
            }
            return this;
        }

        public BlockBuilder tool(String tool_material) {
            String[] needed = tool_material.split("_");

            if (needed[0].equals("stone")) this.tag(BlockTags.NEEDS_STONE_TOOL);
            if (needed[0].equals("iron")) this.tag(BlockTags.NEEDS_IRON_TOOL);
            if (needed[0].equals("diamond")) this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

            if (needed[1].equals("pickaxe")) this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
            if (needed[1].equals("axe")) this.tag(BlockTags.MINEABLE_WITH_AXE);
            if (needed[1].equals("shovel")) this.tag(BlockTags.MINEABLE_WITH_SHOVEL);
            if (needed[1].equals("hoe")) this.tag(BlockTags.MINEABLE_WITH_HOE);
            if (needed[1].equals("sword")) this.tag(BlockTags.SWORD_EFFICIENT);

            return this;
        }

        public BlockBuilder model() {
            return this.model(Models.CUBE);
        }

        public BlockBuilder model(Models model) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(this.block);
            return this;
        }

//        public BlockBuilder itemModel(ModelTemplate model) {
//            ITEM_MODELS.put(this.block, model);
//            return this;
//        }

        public BlockBuilder cutout() {
            BLOCK_CUTOUT.add(this.block);
            return this;
        }

        public BlockBuilder translucent() {
            BLOCK_TRANSLUCENT.add(this.block);
            return this;
        }

        public BlockBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.block.get(), duration);
            return this;
        }

        public BlockBuilder flammable(int duration, int spread) {
            BLOCK_FLAMMABLE.put(this.block, new Pair<>(duration, spread));
            return this;
        }

        public BlockBuilder strip(Block stripped) {
            BLOCK_STRIPPED.putIfAbsent(this.block, stripped);
            return this;
        }
    }

    public static class ItemBuilder {
        protected ItemBuilder(String name, Supplier<? extends Item> item) {
            this.name = name;
            this.item = ITEMS.register(name, item);
        }

        public RegistryObject<Item> build() {
            return this.item;
        }

        protected String name;
        protected RegistryObject<Item> item;

        public ItemBuilder tag(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.item.get());
            return this;
        }

        @SafeVarargs
        public final ItemBuilder tag(TagKey<Item>... tags) {
            for (TagKey<Item> tagName : tags) {
                this.tag(tagName);
            }
            return this;
        }

        public ItemBuilder model() {
            return this.model(ModelTemplates.FLAT_ITEM);
        }

        public ItemBuilder model(ModelTemplate model) {
            ITEM_MODELS.put(this.item, model);
            return this;
        }

        public ItemBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.item.get(), duration);
            return this;
        }
    }

    public static BlockBuilder ofBlock(String id, Supplier<? extends Block> block) {
        return RegistryHelper.ofBlock(id, block, true);
    }

    public static BlockBuilder ofBlock(String id, Supplier<? extends Block> block, boolean doubleBlock, boolean item) {
        return new BlockBuilder(id, block, doubleBlock, item);
    }

    public static BlockBuilder ofBlock(String id, Supplier<? extends Block> block, boolean item) {
        return new BlockBuilder(id, block, false, item);
    }

    public static ItemBuilder ofItem(String id, Supplier<? extends Item> item) {
        return new ItemBuilder(id, item);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean registerItem) {
        RegistryObject<T> toReturn = RegistryHelper.BLOCKS.register(name, block);
        if (registerItem)
            registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<T> registerDoubleBlock(String name, Supplier<T> block, boolean registerItem) {
        RegistryObject<T> toReturn = RegistryHelper.BLOCKS.register(name, block);
        if (registerItem)
            registerDoubleBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return RegistryHelper.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<Item> registerDoubleBlockItem(String name, RegistryObject<T> block) {
        return RegistryHelper.ITEMS.register(name, () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
    }

    public static List<RegistryObject<?>> getModelList(Models key) {
        return BLOCK_MODELS.getOrDefault(key, new ArrayList<>());
    }


    public static void registerStairsAndSlab(RegistryObject<?> parent, RegistryObject<?> stairs, RegistryObject<?> slab) {
        registerSet(parent, Map.of(
                Models.STAIRS, stairs,
                Models.SLAB, slab
        ));
    }

    public static void registerGlass(RegistryObject<?> parent, RegistryObject<?> pane) {
        registerSet(parent, Map.of(
                Models.PANE, pane
        ));
    }

    public static void registerSet(RegistryObject<?> parent, Map<Models, RegistryObject<?>> set) {
        BLOCK_SETS.putIfAbsent(parent, set);
        for (Models model : set.keySet()) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(set.get(model));
        }
    }

    public static void addDrop(RegistryObject<?> block, ItemLike loot) {
        BLOCK_DROPS.putIfAbsent(block, loot);
    }

    public static Map<TagKey<Block>, List<Supplier<Block>>> BLOCK_TAGS = new HashMap<>();

    public static Map<RegistryObject<?>, ItemLike> BLOCK_DROPS = new HashMap<>();
    public static Map<Block, ItemLike> BLOCK_SILK_DROPS = new HashMap<>();

    public static Map<RegistryObject<?>, Block> BLOCK_STRIPPED = new HashMap<>();
    public static Map<RegistryObject<?>, Map<Models, RegistryObject<?>>> BLOCK_SETS = new HashMap<>();

    public static Map<Models, List<RegistryObject<?>>> BLOCK_MODELS = new HashMap<>();
    public static List<RegistryObject<?>> BLOCK_CUTOUT = new ArrayList<>();
    public static List<RegistryObject<?>> BLOCK_TRANSLUCENT = new ArrayList<>();

    public static Map<RegistryObject<?>, Pair<Integer, Integer>> BLOCK_FLAMMABLE = new HashMap<>();

    public static Map<TagKey<Item>, List<Item>> ITEM_TAGS = new HashMap<>();
    public static Map<RegistryObject<Item>, ModelTemplate> ITEM_MODELS = new HashMap<>();
    public static Map<ItemLike, Integer> ITEM_BURNABLE = new HashMap<>();

    public enum Models {
        CUBE,
        CROSS,
        DIRECTIONAL_CROSS,
        PILLAR,
        WOOD,
        STAIRS,
        SLAB,
        BUTTON,
        PRESSURE_PLATE,
        FENCE,
        FENCE_GATE,
        DOOR,
        TRAPDOOR,
        WALL,
        SIGN,
        WALL_SIGN,
        HANGING_SIGN,
        WALL_HANGING_SIGN,
        PANE,
        GRASS,
        CROSS_POTTED,
        ROTATABLE
    }

    public static <T extends Entity> EntityBuilder ofEntity(String id, EntityType.Builder<T> tBuilder) {
        return new EntityBuilder(id, tBuilder);
    }

    public static class EntityBuilder<T extends Entity> {
        protected final String name;
        protected final RegistryObject<EntityType<?>> entity;

        protected EntityBuilder(String name, EntityType.Builder<T> entityBuilder) {
            this.name = name;
            this.entity = register(name, entityBuilder);
        }

        public RegistryObject<EntityType<?>> build() {
            return this.entity;
        }

        private static <T extends Entity> RegistryObject<EntityType<?>> register(
                String registryName, EntityType.Builder<T> entityTypeBuilder) {
            return ENTITIES.register(registryName, () -> entityTypeBuilder.build(registryName));
        }

        public EntityBuilder<T> drop(ItemLike loot) {
            ENTITY_DROPS.putIfAbsent(this.entity, loot);
            return this;
        }

        public EntityBuilder<T> tag(TagKey<EntityType<?>> tagName) {
            ENTITY_TAGS.putIfAbsent(tagName, new ArrayList<>());
            ENTITY_TAGS.get(tagName).add(this.entity);
            return this;
        }
        @SafeVarargs
        public final EntityBuilder<T> tag(TagKey<EntityType<?>>... tagName) {
            for (TagKey<EntityType<?>> tag : tagName) {
                ENTITY_TAGS.putIfAbsent(tag, new ArrayList<>());
                ENTITY_TAGS.get(tag).add(this.entity);
            }
            return this;
        }
    }
    public static Map<TagKey<EntityType<?>>, List<RegistryObject<EntityType<?>>>> ENTITY_TAGS = new HashMap<>();

    public static Map<RegistryObject<EntityType<?>>, ItemLike> ENTITY_DROPS = new HashMap<>();

}