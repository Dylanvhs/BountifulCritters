package net.dylanvhs.bountiful_critters.item;

import net.dylanvhs.bountiful_critters.RegistryHelper;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.EmuEggEntity;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.StickyArrowEntity;
import net.dylanvhs.bountiful_critters.item.custom.*;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = RegistryHelper.ITEMS;
    public static final RegistryObject<Item> STINGRAY_BUCKET = RegistryHelper.ofItem("stingray_bucket",
            () -> new ItemModFishBucket(ModEntities.STINGRAY, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> SUNFISH_BUCKET = RegistryHelper.ofItem("sunfish_bucket",
            () -> new ItemModFishBucket(ModEntities.SUNFISH, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> KRILL_BUCKET = RegistryHelper.ofItem("krill_bucket",
            () -> new ItemModFishBucket(ModEntities.KRILL, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> ANGELFISH_BUCKET = RegistryHelper.ofItem("angelfish_bucket",
            () -> new ItemModFishBucket(ModEntities.ANGELFISH, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> BARRELEYE_BUCKET = RegistryHelper.ofItem("barreleye_bucket",
            () -> new ItemModFishBucket(ModEntities.BARRELEYE, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> NEON_TETRA_BUCKET = RegistryHelper.ofItem("neon_tetra_bucket",
            () -> new ItemModFishBucket(ModEntities.NEON_TETRA, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> FLOUNDER_BUCKET = RegistryHelper.ofItem("flounder_bucket",
            () -> new ItemModFishBucket(ModEntities.FLOUNDER, Fluids.WATER, new Item.Properties())).model().build();
    public static final RegistryObject<Item> MARINE_IGUANA_BUCKET = RegistryHelper.ofItem("marine_iguana_bucket",
            () -> new ItemModFishBucket(ModEntities.MARINE_IGUANA, Fluids.WATER, new Item.Properties())).model().build();

    public static final RegistryObject<Item> POTTED_PILLBUG = RegistryHelper.ofItem("potted_pillbug",
            () ->  new ModCatchableItem(ModEntities.PILLBUG::get, Items.FLOWER_POT, false, new Item.Properties().stacksTo(1))).build();

    public static final RegistryObject<Item> REPTILE_BAG = RegistryHelper.ofItem("reptile_bag",
            () ->  new Item(new Item.Properties().stacksTo(16))).model().build();
    public static final RegistryObject<Item> BAGGED_BLUNT_HEADED_TREE_SNAKE = RegistryHelper.ofItem("bagged_blunt_headed_tree_snake",
            () ->  new BagItem(ModEntities.BLUNT_HEADED_TREE_SNAKE::get, ModItems.REPTILE_BAG.get(), (new Item.Properties()).stacksTo(1))).model().build();
    public static final RegistryObject<Item> BAGGED_GECKO = RegistryHelper.ofItem("bagged_gecko",
            () ->  new BagItem(ModEntities.GECKO::get, ModItems.REPTILE_BAG.get(), (new Item.Properties()).stacksTo(1))).model().build();

    public static final RegistryObject<Item> SNAKE_HOOK = RegistryHelper.ofItem("snake_hook",
            () ->  new Item(new Item.Properties().stacksTo(1))).model().build();

    public static final RegistryObject<Item> CAPTURED_BLUNT_HEADED_TREE_SNAKE = RegistryHelper.ofItem("captured_blunt_headed_tree_snake",
            () ->  new HookItem(ModEntities.BLUNT_HEADED_TREE_SNAKE::get, ModItems.SNAKE_HOOK.get(), (new Item.Properties()).stacksTo(1))).model().build();

    public static final RegistryObject<Item> LONGHORN_DIDGERIDOO = RegistryHelper.ofItem("longhorn_didgeridoo",
            () -> new LongHornDidgeridooItem(new Item.Properties().stacksTo(1))).build();
    public static final RegistryObject<Item> LONGHORN_HORN =
            RegistryHelper.ofItem("longhorn_horn", () -> new Item(new Item.Properties())).model().build();

    public static final RegistryObject<Item> SALT =
            RegistryHelper.ofItem("salt", () -> new Item(new Item.Properties())).model().build();
    public static final RegistryObject<Item> SALTED_KELP =
            RegistryHelper.ofItem("salted_kelp", () -> new Item(new Item.Properties().food(ModFoods.SALTED_KELP))).model().build();

    public static final RegistryObject<Item> SEAGRASS_BALL =
            RegistryHelper.ofItem("seagrass_ball", () -> new BlockItem(ModBlocks.SEAGRASS_BALL_PLACED.get(),(new Item.Properties()))).model().build();
    public static final RegistryObject<Item> DRIED_SEAGRASS_BALL =
            RegistryHelper.ofItem("dried_seagrass_ball", () -> new Item(new Item.Properties())).model().build();
    public static final RegistryObject<Item> STICKY_ARROW =
            RegistryHelper.ofItem("sticky_arrow", () -> new ModItemArrow(new Item.Properties())).model().build();

    public static final RegistryObject<Item> RAW_KRILL =
            RegistryHelper.ofItem("raw_krill", () -> new Item(new Item.Properties().food(ModFoods.RAW_KRILL))).model().build();
    public static final RegistryObject<Item> FRIED_KRILL =
            RegistryHelper.ofItem("fried_krill", () -> new Item(new Item.Properties().food(ModFoods.FRIED_KRILL))).model().build();
    public static final RegistryObject<Item> KRILL_COCKTAIL =
            RegistryHelper.ofItem("krill_cocktail", () -> new KrillCocktailItem(new Item.Properties().food(ModFoods.KRILL_COCKTAIL).stacksTo(1))).model().build();

    public static final RegistryObject<Item> RAW_SUNFISH_MEAT =
            RegistryHelper.ofItem("raw_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.RAW_SUNFISH_MEAT))).model().build();
    public static final RegistryObject<Item> COOKED_SUNFISH_MEAT =
            RegistryHelper.ofItem("cooked_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SUNFISH_MEAT))).model().build();
    public static final RegistryObject<Item> SUNFISH_SUSHI =
            RegistryHelper.ofItem("sunfish_sushi", () -> new Item(new Item.Properties().food(ModFoods.SUNFISH_SUSHI))).model().build();
    public static final RegistryObject<Item> RAW_GOLDEN_SUNFISH_MEAT =
            RegistryHelper.ofItem("raw_golden_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.RAW_GOLDEN_SUNFISH_MEAT))).model().build();
    public static final RegistryObject<Item> COOKED_GOLDEN_SUNFISH_MEAT =
            RegistryHelper.ofItem("cooked_golden_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.COOKED_GOLDEN_SUNFISH_MEAT))).model().build();

    public static final RegistryObject<Item> RAW_ANGELFISH =
            RegistryHelper.ofItem("raw_angelfish", () -> new Item(new Item.Properties().food(ModFoods.RAW_ANGELFISH))).model().build();

    public static final RegistryObject<Item> RAW_BARRELEYE =
            RegistryHelper.ofItem("raw_barreleye", () -> new Item(new Item.Properties().food(ModFoods.RAW_BARRELEYE))).model().build();

    public static final RegistryObject<Item> RAW_NEON_TETRA =
            RegistryHelper.ofItem("raw_neon_tetra", () -> new Item(new Item.Properties().food(ModFoods.RAW_NEON_TETRA))).model().build();

    public static final RegistryObject<Item> RAW_FLOUNDER =
            RegistryHelper.ofItem("raw_flounder", () -> new Item(new Item.Properties().food(ModFoods.RAW_FLOUNDER))).model().build();

    public static final RegistryObject<Item> EMU_EGG =
            RegistryHelper.ofItem("emu_egg", () -> new EmuEggItem(new Item.Properties().stacksTo(16))).model().build();
    public static final RegistryObject<Item> BOILED_EMU_EGG =
            RegistryHelper.ofItem("boiled_emu_egg", () -> new Item(new Item.Properties().food(ModFoods.BOILED_EMU_EGG).stacksTo(16))).model().build();
    public static final RegistryObject<Item> EMU_EGG_SHELL_PIECES =
            RegistryHelper.ofItem("emu_egg_shell_pieces", () -> new Item(new Item.Properties())).model().build();

    public static final RegistryObject<Item> RAW_PILLBUG =
            RegistryHelper.ofItem("raw_pillbug", () -> new Item(new Item.Properties().food(ModFoods.RAW_PILLBUG))).model().build();
    public static final RegistryObject<Item> POISONOUS_PILLBUG =
            RegistryHelper.ofItem("poisonous_pillbug", () -> new Item(new Item.Properties().food(ModFoods.POISONOUS_PILLBUG))).model().build();
    public static final RegistryObject<Item> ROASTED_PILLBUG =
            RegistryHelper.ofItem("roasted_pillbug", () -> new BlockItem(ModBlocks.ROASTED_PILLBUG_BLOCK.get(), (new Item.Properties()).stacksTo(1))).model().build();
    public static final RegistryObject<Item> PILLBUG_THROWABLE =
            RegistryHelper.ofItem("pillbug_throwable", () -> new PillbugProjectileItem(ModEntities.PILLBUG::get, Fluids.EMPTY, Items.AIR, (new Item.Properties()).stacksTo(1))).build();

    public static final RegistryObject<Item> PILLBUG_SCUTE =
            RegistryHelper.ofItem("pillbug_scute", () -> new Item(new Item.Properties())).model().build();

    public static final RegistryObject<Item> POISONOUS_PILLBUG_SCUTE =
            RegistryHelper.ofItem("poisonous_pillbug_scute", () -> new Item(new Item.Properties())).model().build();

    public static final RegistryObject<Item> RAW_PHEASANT =
            RegistryHelper.ofItem("pheasant", () -> new Item(new Item.Properties().food(ModFoods.RAW_PHEASANT))).model().build();
    public static final RegistryObject<Item> COOKED_PHEASANT =
            RegistryHelper.ofItem("cooked_pheasant", () -> new Item(new Item.Properties().food(ModFoods.COOKED_PHEASANT))).model().build();
    public static final RegistryObject<Item> PHEASANT_FEATHER =
            RegistryHelper.ofItem("pheasant_feather", () -> new Item(new Item.Properties())).model().build();
    public static final RegistryObject<Item> PHEASANT_EGG =
            RegistryHelper.ofItem("pheasant_egg", () -> new PheasantEggItem(new Item.Properties().stacksTo(16))).model().build();

    public static final RegistryObject<Item> LION_ARMOR =
            RegistryHelper.ofItem("lion_armor", () -> new Item(new Item.Properties().stacksTo(1))).model().build();


    public static final RegistryObject<Item> MEMORIES_MUSIC_DISC = RegistryHelper.ofItem("memories_music_disc",
            () -> new RecordItem(15, ModSounds.MEMORIES, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 6280)).model().build();

    public static final RegistryObject<Item> BOUNTIFUL_MUSIC_DISC = RegistryHelper.ofItem("bountiful_music_disc",
            () -> new RecordItem(13, ModSounds.BOUNTIFUL, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3680)).model().build();

    public static final RegistryObject<Item> SNEEZE_MUSIC_DISC = RegistryHelper.ofItem("sneeze_music_disc",
            () -> new RecordItem(14, ModSounds.SNEEZE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 4280)).model().build();

    public static final RegistryObject<Item> BUGS_MUSIC_DISC = RegistryHelper.ofItem("bugs_music_disc",
            () -> new RecordItem(14, ModSounds.BUGS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 4400)).model().build();

    public static final RegistryObject<Item> EMU_SPAWN_EGG = RegistryHelper.ofItem("emu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.EMU, 0x72482e, 0x21486a, new Item.Properties())).build();
    public static final RegistryObject<Item> STINGRAY_SPAWN_EGG = RegistryHelper.ofItem("stingray_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.STINGRAY, 0x7a796a, 0x484c4c, new Item.Properties())).build();

    public static final RegistryObject<Item> SUNFISH_SPAWN_EGG = RegistryHelper.ofItem("sunfish_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SUNFISH, 0x5d4f46, 0x968d7f, new Item.Properties())).build();

    public static final RegistryObject<Item> KRILL_SPAWN_EGG = RegistryHelper.ofItem("krill_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.KRILL, 0xbd3a27, 0xf17448, new Item.Properties())).build();

    public static final RegistryObject<Item> MARINE_IGUANA_SPAWN_EGG = RegistryHelper.ofItem("marine_iguana_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MARINE_IGUANA, 0x403b3d, 0x94908b, new Item.Properties())).build();

    public static final RegistryObject<Item> LONGHORN_SPAWN_EGG = RegistryHelper.ofItem("longhorn_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LONGHORN, 0xad4e1a, 0xedd9b5, new Item.Properties())).build();

    public static final RegistryObject<Item> TOUCAN_SPAWN_EGG = RegistryHelper.ofItem("toucan_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TOUCAN, 0x1d1d21, 0xff8c1d, new Item.Properties())).build();

    public static final RegistryObject<Item> HUMPBACK_WHALE_SPAWN_EGG = RegistryHelper.ofItem("humpback_whale_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HUMPBACK_WHALE, 0x2e4044, 0x90959a, new Item.Properties())).build();

    public static final RegistryObject<Item> PILLBUG_SPAWN_EGG = RegistryHelper.ofItem("pillbug_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PILLBUG, 0x464e4b, 0x182020, new Item.Properties())).build();

    public static final RegistryObject<Item> BLUNT_HEADED_TREE_SNAKE_SPAWN_EGG = RegistryHelper.ofItem("blunt_headed_tree_snake_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BLUNT_HEADED_TREE_SNAKE, 0x864e2c, 0x4d2314, new Item.Properties())).build();

    public static final RegistryObject<Item> GECKO_SPAWN_EGG = RegistryHelper.ofItem("gecko_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GECKO, 0xf8cc2a, 0x545169, new Item.Properties())).build();

    public static final RegistryObject<Item> LION_SPAWN_EGG = RegistryHelper.ofItem("lion_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LION, 0xfabb34, 0x996428, new Item.Properties())).build();

    public static final RegistryObject<Item> BARRELEYE_SPAWN_EGG = RegistryHelper.ofItem("barreleye_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BARRELEYE, 0x1d2d26, 0x3d4f50, new Item.Properties())).build();

    public static final RegistryObject<Item> ANGELFISH_SPAWN_EGG = RegistryHelper.ofItem("angelfish_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ANGELFISH, 0x9b8b54, 0x322a1b, new Item.Properties())).build();

    public static final RegistryObject<Item> NEON_TETRA_SPAWN_EGG = RegistryHelper.ofItem("neon_tetra_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.NEON_TETRA, 0x85091c, 0x0ec4bf, new Item.Properties())).build();

    public static final RegistryObject<Item> FLOUNDER_SPAWN_EGG = RegistryHelper.ofItem("flounder_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FLOUNDER, 0x5b442f, 0x916e44, new Item.Properties())).build();

    public static final RegistryObject<Item> PHEASANT_SPAWN_EGG = RegistryHelper.ofItem("pheasant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PHEASANT, 0xa25938, 0x1e2034, new Item.Properties())).build();

    public static final RegistryObject<Item> HOGBEAR_SPAWN_EGG = RegistryHelper.ofItem("hogbear_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HOGBEAR, 0x695a4e, 0x988e86, new Item.Properties())).build();

    public static void initDispenser() {
        DispenserBlock.registerBehavior(STICKY_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                StickyArrowEntity entityarrow = new StickyArrowEntity(ModEntities.STICKY_ARROW.get(), position.x(), position.y(), position.z(), worldIn);
                entityarrow.pickup = StickyArrowEntity.Pickup.ALLOWED;
                return entityarrow;
            }
        });

        DispenserBlock.registerBehavior(EMU_EGG.get(), new AbstractProjectileDispenseBehavior() {
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                return new EmuEggEntity(worldIn, position.x(), position.y(), position.z());
            }
        });

        DispenseItemBehavior bucketDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public @NotNull ItemStack execute(BlockSource blockSource, ItemStack stack) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)stack.getItem();
                BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
                Level level = blockSource.getLevel();
                if (dispensiblecontaineritem.emptyContents(null, level, blockpos, null)) {
                    dispensiblecontaineritem.checkExtraContent(null, level, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
            }
        };

        DispenseItemBehavior potDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public @NotNull ItemStack execute(BlockSource blockSource, ItemStack stack) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)stack.getItem();
                BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
                Level level = blockSource.getLevel();
                if (dispensiblecontaineritem.emptyContents(null, level, blockpos, null)) {
                    dispensiblecontaineritem.checkExtraContent(null, level, stack, blockpos);
                    return new ItemStack(Items.FLOWER_POT);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
            }
        };

        DispenseItemBehavior bagDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public @NotNull ItemStack execute(BlockSource blockSource, ItemStack stack) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)stack.getItem();
                BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
                Level level = blockSource.getLevel();
                if (dispensiblecontaineritem.emptyContents(null, level, blockpos, null)) {
                    dispensiblecontaineritem.checkExtraContent(null, level, stack, blockpos);
                    return new ItemStack(ModItems.REPTILE_BAG.get());
                } else {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(STINGRAY_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(SUNFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(KRILL_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(ANGELFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(BARRELEYE_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NEON_TETRA_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(FLOUNDER_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MARINE_IGUANA_BUCKET.get(), bucketDispenseBehavior);

        DispenserBlock.registerBehavior(POTTED_PILLBUG.get(), potDispenseBehavior);

        DispenserBlock.registerBehavior(BAGGED_BLUNT_HEADED_TREE_SNAKE.get(), bagDispenseBehavior);
        DispenserBlock.registerBehavior(BAGGED_GECKO.get(), bagDispenseBehavior);
    }

    public static void init(){}
}

