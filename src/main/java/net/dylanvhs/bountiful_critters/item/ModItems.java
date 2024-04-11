package net.dylanvhs.bountiful_critters.item;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.custom.EmuEggItem;
import net.dylanvhs.bountiful_critters.item.custom.ItemModFishBucket;
import net.dylanvhs.bountiful_critters.item.custom.ModFoods;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
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
    public static final RegistryObject<Item> RAW_KRILL =
            ITEMS.register("raw_krill", () -> new Item(new Item.Properties().food(ModFoods.RAW_KRILL)));
    public static final RegistryObject<Item> FRIED_KRILL =
            ITEMS.register("fried_krill", () -> new Item(new Item.Properties().food(ModFoods.FRIED_KRILL)));
    public static final RegistryObject<Item> RAW_SUNFISH_MEAT =
            ITEMS.register("raw_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.RAW_SUNFISH_MEAT)));
    public static final RegistryObject<Item> COOKED_SUNFISH_MEAT =
            ITEMS.register("cooked_sunfish_meat", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SUNFISH_MEAT)));
    public static final RegistryObject<Item> EMU_EGG =
            ITEMS.register("emu_egg", () -> new EmuEggItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BOILED_EMU_EGG =
            ITEMS.register("boiled_emu_egg", () -> new Item(new Item.Properties().food(ModFoods.BOILED_EMU_EGG).stacksTo(16)));

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

    public static final RegistryObject<Item> LONG_HORN_SPAWN_EGG = ITEMS.register("long_horn_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LONG_HORN, 0xad4e1a, 0xedd9b5, new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

