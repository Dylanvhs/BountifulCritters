package net.dylanvhs.bountiful_critters.item;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.custom.*;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BountifulCritters.MOD_ID);
    public static final RegistryObject<Item> STINGRAY_BUCKET = ITEMS.register("stingray_bucket",
            () -> new ItemModFishBucket(ModEntities.STINGRAY, Fluids.WATER, new Item.Properties()));
    public static final RegistryObject<Item> SUNFISH_BUCKET = ITEMS.register("sunfish_bucket",
            () -> new ItemModFishBucket(ModEntities.SUNFISH, Fluids.WATER, new Item.Properties()));
    public static final RegistryObject<Item> KRILL_BUCKET = ITEMS.register("krill_bucket",
            () -> new ItemModFishBucket(ModEntities.KRILL, Fluids.WATER, new Item.Properties()));
    public static final RegistryObject<Item> MARINE_IGUANA_BUCKET = ITEMS.register("marine_iguana_bucket",
            () -> new ItemModFishBucket(ModEntities.MARINE_IGUANA, Fluids.WATER, new Item.Properties()));
    public static final RegistryObject<Item> POTTED_PILLBUG = ITEMS.register("potted_pillbug",
            () ->  new ModCatchableItem(ModEntities.PILLBUG::get, Items.FLOWER_POT, false, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> REPTILE_BAG = ITEMS.register("reptile_bag",
            () ->  new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BAGGED_BLUNT_HEADED_TREE_SNAKE = ITEMS.register("bagged_blunt_headed_tree_snake",
            () ->  new BagItem(ModEntities.BLUNT_HEADED_TREE_SNAKE::get, ModItems.REPTILE_BAG.get(), (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> BAGGED_GECKO = ITEMS.register("bagged_gecko",
            () ->  new BagItem(ModEntities.GECKO::get, ModItems.REPTILE_BAG.get(), (new Item.Properties()).stacksTo(1)));

    public static final RegistryObject<Item> LONGHORN_DIDGERIDOO = ITEMS.register("longhorn_didgeridoo",
            () -> new LongHornDidgeridooItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LONGHORN_HORN =
            ITEMS.register("longhorn_horn", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SALT =
            ITEMS.register("salt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SALTED_KELP =
            ITEMS.register("salted_kelp", () -> new Item(new Item.Properties().food(ModFoods.SALTED_KELP)));

    public static final RegistryObject<Item> SEAGRASS_BALL =
            ITEMS.register("seagrass_ball", () -> new BlockItem(ModBlocks.SEAGRASS_BALL_PLACED.get(),(new Item.Properties())));

    public static final RegistryObject<Item> RAW_KRILL =
            ITEMS.register("raw_krill", () -> new Item(new Item.Properties().food(ModFoods.RAW_KRILL)));
    public static final RegistryObject<Item> FRIED_KRILL =
            ITEMS.register("fried_krill", () -> new Item(new Item.Properties().food(ModFoods.FRIED_KRILL)));
    public static final RegistryObject<Item> KRILL_COCKTAIL =
            ITEMS.register("krill_cocktail", () -> new KrillCocktailItem(new Item.Properties().food(ModFoods.KRILL_COCKTAIL).stacksTo(1)));

    public static final RegistryObject<Item> RAW_SUNFISH_MEAT =
            ITEMS.register("raw_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.RAW_SUNFISH_MEAT)));
    public static final RegistryObject<Item> COOKED_SUNFISH_MEAT =
            ITEMS.register("cooked_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SUNFISH_MEAT)));
    public static final RegistryObject<Item> SUNFISH_SUSHI =
            ITEMS.register("sunfish_sushi", () -> new Item(new Item.Properties().food(ModFoods.SUNFISH_SUSHI)));

    public static final RegistryObject<Item> EMU_EGG =
            ITEMS.register("emu_egg", () -> new EmuEggItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BOILED_EMU_EGG =
            ITEMS.register("boiled_emu_egg", () -> new Item(new Item.Properties().food(ModFoods.BOILED_EMU_EGG).stacksTo(16)));

    public static final RegistryObject<Item> RAW_PILLBUG =
            ITEMS.register("raw_pillbug", () -> new Item(new Item.Properties().food(ModFoods.RAW_PILLBUG)));
    public static final RegistryObject<Item> POISONOUS_PILLBUG =
            ITEMS.register("poisonous_pillbug", () -> new Item(new Item.Properties().food(ModFoods.POISONOUS_PILLBUG)));
    public static final RegistryObject<Item> ROASTED_PILLBUG =
            ITEMS.register("roasted_pillbug", () -> new BlockItem(ModBlocks.ROASTED_PILLBUG_BLOCK.get(), (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> PILLBUG_THROWABLE =
            ITEMS.register("pillbug_throwable", () -> new PillbugProjectileItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> RETURNING_MEMORY_MUSIC_DISC = ITEMS.register("returning_memory_music_disc",
            () -> new RecordItem(15, ModSounds.RETURNING_MEMORY, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 6280));

    public static final RegistryObject<Item> EMU_SPAWN_EGG = ITEMS.register("emu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.EMU, 0x72482e, 0x21486a, new Item.Properties()));
    public static final RegistryObject<Item> STINGRAY_SPAWN_EGG = ITEMS.register("stingray_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.STINGRAY, 0x7a796a, 0x484c4c, new Item.Properties()));

    public static final RegistryObject<Item> SUNFISH_SPAWN_EGG = ITEMS.register("sunfish_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SUNFISH, 0x5d4f46, 0x968d7f, new Item.Properties()));

    public static final RegistryObject<Item> KRILL_SPAWN_EGG = ITEMS.register("krill_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.KRILL, 0xbd3a27, 0xf17448, new Item.Properties()));

    public static final RegistryObject<Item> MARINE_IGUANA_SPAWN_EGG = ITEMS.register("marine_iguana_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MARINE_IGUANA, 0x403b3d, 0x94908b, new Item.Properties()));

    public static final RegistryObject<Item> LONG_HORN_SPAWN_EGG = ITEMS.register("longhorn_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LONGHORN, 0xad4e1a, 0xedd9b5, new Item.Properties()));

    public static final RegistryObject<Item> TOUCAN_SPAWN_EGG = ITEMS.register("toucan_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TOUCAN, 0x1d1d21, 0xff8c1d, new Item.Properties()));

    public static final RegistryObject<Item> HUMPBACK_WHALE_SPAWN_EGG = ITEMS.register("humpback_whale_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HUMPBACK_WHALE, 0x2e4044, 0x90959a, new Item.Properties()));

    public static final RegistryObject<Item> PILLBUG_SPAWN_EGG = ITEMS.register("pillbug_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PILLBUG, 0x464e4b, 0x182020, new Item.Properties()));

    public static final RegistryObject<Item> BLUNT_HEADED_TREE_SNAKE_SPAWN_EGG = ITEMS.register("blunt_headed_tree_snake_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BLUNT_HEADED_TREE_SNAKE, 0x864e2c, 0x4d2314, new Item.Properties()));

    public static final RegistryObject<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GECKO, 0xdfb643, 0x5f5356, new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

