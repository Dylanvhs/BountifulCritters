package net.dylanvhs.bountiful_critters.entity;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BountifulCritters.MOD_ID);

    public static final RegistryObject<EntityType<StingrayEntity>> STINGRAY =
            ENTITY_TYPES.register("stingray",
                    () -> EntityType.Builder.of(StingrayEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 0.25f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "stingray").toString()));

    public static final RegistryObject<EntityType<SunfishEntity>> SUNFISH =
            ENTITY_TYPES.register("sunfish",
                    () -> EntityType.Builder.of(SunfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "sunfish").toString()));

    public static final RegistryObject<EntityType<EmuEntity>> EMU =
            ENTITY_TYPES.register("emu",
                    () -> EntityType.Builder.of(EmuEntity::new, MobCategory.AMBIENT)
                            .sized(1f, 2f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "emu").toString()));

    public static final RegistryObject<EntityType<KrillEntity>> KRILL =
            ENTITY_TYPES.register("krill",
                    () -> EntityType.Builder.of(KrillEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "krill").toString()));

    public static final RegistryObject<EntityType<MarineIguanaEntity>> MARINE_IGUANA =
            ENTITY_TYPES.register("marine_iguana",
                    () -> EntityType.Builder.of(MarineIguanaEntity::new, MobCategory.AMBIENT)
                            .sized(0.9f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "marine_iguana").toString()));

    public static final RegistryObject<EntityType<LonghornEntity>> LONG_HORN =
            ENTITY_TYPES.register("long_horn",
                    () -> EntityType.Builder.of(LonghornEntity::new, MobCategory.AMBIENT)
                            .sized(1.25f, 1.7f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "long_horn").toString()));

    public static final RegistryObject<EntityType<ToucanEntity>> TOUCAN =
            ENTITY_TYPES.register("toucan",
                    () -> EntityType.Builder.of(ToucanEntity::new, MobCategory.AMBIENT)
                            .sized(0.5f, 1f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "toucan").toString()));


    public static final RegistryObject<EntityType<EmuEggEntity>> EMU_EGG =
                    ENTITY_TYPES.register("emu_egg_projectile", () -> EntityType.Builder.<EmuEggEntity>of(EmuEggEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f).build("emu_egg_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}