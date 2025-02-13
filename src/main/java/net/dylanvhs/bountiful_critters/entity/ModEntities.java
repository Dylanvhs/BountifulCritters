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
                            .sized(0.8f, 0.3f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "stingray").toString()));

    public static final RegistryObject<EntityType<SunfishEntity>> SUNFISH =
            ENTITY_TYPES.register("sunfish",
                    () -> EntityType.Builder.of(SunfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "sunfish").toString()));

    public static final RegistryObject<EntityType<EmuEntity>> EMU =
            ENTITY_TYPES.register("emu",
                    () -> EntityType.Builder.of(EmuEntity::new, MobCategory.CREATURE)
                            .sized(1f, 2f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "emu").toString()));

    public static final RegistryObject<EntityType<KrillEntity>> KRILL =
            ENTITY_TYPES.register("krill",
                    () -> EntityType.Builder.of(KrillEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "krill").toString()));

    public static final RegistryObject<EntityType<MarineIguanaEntity>> MARINE_IGUANA =
            ENTITY_TYPES.register("marine_iguana",
                    () -> EntityType.Builder.of(MarineIguanaEntity::new, MobCategory.AXOLOTLS)
                            .sized(0.9f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "marine_iguana").toString()));

    public static final RegistryObject<EntityType<LonghornEntity>> LONGHORN =
            ENTITY_TYPES.register("longhorn",
                    () -> EntityType.Builder.of(LonghornEntity::new, MobCategory.CREATURE)
                            .sized(1.25f, 1.7f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "longhorn").toString()));

    public static final RegistryObject<EntityType<ToucanEntity>> TOUCAN =
            ENTITY_TYPES.register("toucan",
                    () -> EntityType.Builder.of(ToucanEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 1f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "toucan").toString()));

    public static final RegistryObject<EntityType<HumpbackWhaleEntity>> HUMPBACK_WHALE =
            ENTITY_TYPES.register("humpback_whale",
                    () -> EntityType.Builder.of(HumpbackWhaleEntity::new, MobCategory.WATER_CREATURE)
                            .sized(3.75f, 3.75f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "humpback_whale").toString()));

    public static final RegistryObject<EntityType<PillbugEntity>> PILLBUG =
            ENTITY_TYPES.register("pillbug",
                    () -> EntityType.Builder.of(PillbugEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 0.65f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "pillbug").toString()));

    public static final RegistryObject<EntityType<BluntHeadedTreeSnakeEntity>> BLUNT_HEADED_TREE_SNAKE =
            ENTITY_TYPES.register("blunt_headed_tree_snake",
                    () -> EntityType.Builder.of(BluntHeadedTreeSnakeEntity::new, MobCategory.CREATURE)
                            .sized(1f, 1.25f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "blunt_headed_tree_snake").toString()));

    public static final RegistryObject<EntityType<GeckoEntity>> GECKO =
            ENTITY_TYPES.register("gecko",
                    () -> EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 0.7f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "gecko").toString()));

    public static final RegistryObject<EntityType<LionEntity>> LION =
            ENTITY_TYPES.register("lion",
                    () -> EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                            .sized(1f, 2f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "lion").toString()));

    public static final RegistryObject<EntityType<BarreleyeEntity>> BARRELEYE =
            ENTITY_TYPES.register("barreleye",
                    () -> EntityType.Builder.of(BarreleyeEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "barreleye").toString()));

    public static final RegistryObject<EntityType<AngelfishEntity>> ANGELFISH =
            ENTITY_TYPES.register("angelfish",
                    () -> EntityType.Builder.of(AngelfishEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "angelfish").toString()));

    public static final RegistryObject<EntityType<NeonTetraEntity>> NEON_TETRA =
            ENTITY_TYPES.register("neon_tetra",
                    () -> EntityType.Builder.of(NeonTetraEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.35f, 0.35f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "neon_tetra").toString()));

    public static final RegistryObject<EntityType<FlounderEntity>> FLOUNDER =
            ENTITY_TYPES.register("flounder",
                    () -> EntityType.Builder.of(FlounderEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.6f, 0.35f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "flounder").toString()));

    public static final RegistryObject<EntityType<PheasantEntity>> PHEASANT =
            ENTITY_TYPES.register("pheasant",
                    () -> EntityType.Builder.of(PheasantEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.25f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "pheasant").toString()));

    public static final RegistryObject<EntityType<HogbearEntity>> HOGBEAR =
            ENTITY_TYPES.register("hogbear",
                    () -> EntityType.Builder.of(HogbearEntity::new, MobCategory.MONSTER)
                            .sized(2f, 2.75f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "hogbear").toString()));



    public static final RegistryObject<EntityType<EmuEggEntity>> EMU_EGG =
                    ENTITY_TYPES.register("emu_egg_projectile", () -> EntityType.Builder.<EmuEggEntity>of(EmuEggEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f).build("emu_egg_projectile"));

    public static final RegistryObject<EntityType<PheasantEggEntity>> PHEASANT_EGG =
            ENTITY_TYPES.register("pheasant_egg_projectile", () -> EntityType.Builder.<PheasantEggEntity>of(PheasantEggEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("pheasant_egg_projectile"));

    public static final RegistryObject<EntityType<StickyArrowEntity>> STICKY_ARROW =
            ENTITY_TYPES.register("sticky_arrow_projectile", () -> EntityType.Builder.<StickyArrowEntity>of(StickyArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).setCustomClientFactory(StickyArrowEntity::new).build("sticky_arrow_projectile"));

    public static final RegistryObject<EntityType<PillbugProjectileEntity>> THROWABLE_PILLBUG =
            ENTITY_TYPES.register("pillbug_projectile", () -> EntityType.Builder.<PillbugProjectileEntity>of(PillbugProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("pillbug_projectile"));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}