package net.dylanvhs.bountiful_critters.entity;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.aquatic.*;
import net.dylanvhs.bountiful_critters.entity.custom.flying.ToucanEntity;
import net.dylanvhs.bountiful_critters.entity.custom.land.*;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.EmuEggEntity;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.PheasantEggEntity;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.PillbugProjectileEntity;
import net.dylanvhs.bountiful_critters.entity.custom.projectile.StickyArrowEntity;
import net.dylanvhs.bountiful_critters.entity.custom.semi_aquatic.MarineIguanaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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
            register("stingray",
                    EntityType.Builder.of(StingrayEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 0.3f));

    public static final RegistryObject<EntityType<SunfishEntity>> SUNFISH =
            register("sunfish",
                    EntityType.Builder.of(SunfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 1.8f));

    public static final RegistryObject<EntityType<EmuEntity>> EMU =
            register("emu",
                    EntityType.Builder.of(EmuEntity::new, MobCategory.CREATURE)
                            .sized(1f, 2f));

    public static final RegistryObject<EntityType<KrillEntity>> KRILL =
            register("krill",
                    EntityType.Builder.of(KrillEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<MarineIguanaEntity>> MARINE_IGUANA =
            register("marine_iguana",
                    EntityType.Builder.of(MarineIguanaEntity::new, MobCategory.AXOLOTLS)
                            .sized(0.9f, 0.5f));

    public static final RegistryObject<EntityType<LonghornEntity>> LONGHORN =
            register("longhorn",
                    EntityType.Builder.of(LonghornEntity::new, MobCategory.CREATURE)
                            .sized(1.25f, 1.7f));

    public static final RegistryObject<EntityType<ToucanEntity>> TOUCAN =
            register("toucan",
                    EntityType.Builder.of(ToucanEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 1f));

    public static final RegistryObject<EntityType<HumpbackWhaleEntity>> HUMPBACK_WHALE =
            register("humpback_whale",
                    EntityType.Builder.of(HumpbackWhaleEntity::new, MobCategory.WATER_CREATURE)
                            .sized(3.75f, 3.75f));

    public static final RegistryObject<EntityType<PillbugEntity>> PILLBUG =
            register("pillbug",
                    EntityType.Builder.of(PillbugEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 0.65f));

    public static final RegistryObject<EntityType<BluntHeadedTreeSnakeEntity>> BLUNT_HEADED_TREE_SNAKE =
            register("blunt_headed_tree_snake",
                    EntityType.Builder.of(BluntHeadedTreeSnakeEntity::new, MobCategory.CREATURE)
                            .sized(1f, 1.25f));

    public static final RegistryObject<EntityType<GeckoEntity>> GECKO =
            register("gecko",
                    EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 0.7f));

    public static final RegistryObject<EntityType<LionEntity>> LION =
            register("lion",
                    EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                            .sized(1f, 2f));

    public static final RegistryObject<EntityType<BarreleyeEntity>> BARRELEYE =
            register("barreleye",
                    EntityType.Builder.of(BarreleyeEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<AngelfishEntity>> ANGELFISH =
            register("angelfish",
                    EntityType.Builder.of(AngelfishEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<NeonTetraEntity>> NEON_TETRA =
            register("neon_tetra",
                    EntityType.Builder.of(NeonTetraEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.35f, 0.35f));

    public static final RegistryObject<EntityType<FlounderEntity>> FLOUNDER =
            register("flounder",
                    EntityType.Builder.of(FlounderEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.6f, 0.35f));

    public static final RegistryObject<EntityType<PheasantEntity>> PHEASANT =
            register("pheasant",
                    EntityType.Builder.of(PheasantEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.25f));

    public static final RegistryObject<EntityType<HogbearEntity>> HOGBEAR =
            register("hogbear",
                    EntityType.Builder.of(HogbearEntity::new, MobCategory.MONSTER)
                    .sized(2f, 2.75f));



    public static final RegistryObject<EntityType<EmuEggEntity>> EMU_EGG =
            register("emu_egg_projectile", EntityType.Builder.<EmuEggEntity>of(EmuEggEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<PheasantEggEntity>> PHEASANT_EGG =
            register("pheasant_egg_projectile", EntityType.Builder.<PheasantEggEntity>of(PheasantEggEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<StickyArrowEntity>> STICKY_ARROW =
            register("sticky_arrow_projectile", EntityType.Builder.<StickyArrowEntity>of(StickyArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).setCustomClientFactory(StickyArrowEntity::new));

    public static final RegistryObject<EntityType<PillbugProjectileEntity>> THROWABLE_PILLBUG =
            register("pillbug_projectile", EntityType.Builder.<PillbugProjectileEntity>of(PillbugProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(registryname, () -> entityTypeBuilder.build(registryname));
    }
}