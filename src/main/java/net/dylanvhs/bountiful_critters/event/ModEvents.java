package net.dylanvhs.bountiful_critters.event;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.client.*;
import net.dylanvhs.bountiful_critters.entity.custom.*;
import net.dylanvhs.bountiful_critters.particles.ModParticles;
import net.dylanvhs.bountiful_critters.particles.client.NeonShineParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e) {

        EntityRenderers.register
                (ModEntities.STINGRAY.get(), StingrayRenderer:: new);

        EntityRenderers.register
                (ModEntities.SUNFISH.get(), SunfishRenderer:: new);

        EntityRenderers.register
                (ModEntities.EMU.get(), EmuRenderer:: new);

        EntityRenderers.register
                (ModEntities.KRILL.get(), KrillRenderer:: new);

        EntityRenderers.register
                (ModEntities.MARINE_IGUANA.get(), MarineIguanaRenderer:: new);

        EntityRenderers.register
                (ModEntities.LONGHORN.get(), LongHornRenderer:: new);

        EntityRenderers.register
                (ModEntities.TOUCAN.get(), ToucanRenderer:: new);

        EntityRenderers.register
                (ModEntities.HUMPBACK_WHALE.get(), HumpbackWhaleRenderer:: new);

        EntityRenderers.register
                (ModEntities.PILLBUG.get(), PillbugRenderer:: new);

        EntityRenderers.register
                (ModEntities.BLUNT_HEADED_TREE_SNAKE.get(), BluntHeadedTreeSnakeRenderer:: new);

        EntityRenderers.register
                (ModEntities.GECKO.get(), GeckoRenderer:: new);

        EntityRenderers.register
                (ModEntities.LION.get(), LionRenderer:: new);

        EntityRenderers.register
                (ModEntities.BARRELEYE.get(), BarreleyeRenderer:: new);

        EntityRenderers.register
                (ModEntities.ANGELFISH.get(), AngelfishRenderer:: new);

        EntityRenderers.register
                (ModEntities.NEON_TETRA.get(), NeonTetraRenderer:: new);

        EntityRenderers.register
                (ModEntities.FLOUNDER.get(), FlounderRenderer:: new);

        EntityRenderers.register
                (ModEntities.PHEASANT.get(), PheasantRenderer:: new);

        EntityRenderers.register
                (ModEntities.HOGBEAR.get(), HogbearRenderer:: new);

        EntityRenderers.register
                (ModEntities.STICKY_ARROW.get(), StickyArrowRenderer::new);

        EntityRenderers.register
                (ModEntities.EMU_EGG.get(), ThrownItemRenderer::new);

        EntityRenderers.register
                (ModEntities.PHEASANT_EGG.get(), ThrownItemRenderer::new);

        EntityRenderers.register
                (ModEntities.THROWABLE_PILLBUG.get(), ThrownItemRenderer::new);

    }
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.NEON_SHINE.get(), NeonShineParticle.Provider::new);
    }



    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent e) {
        e.register(ModEntities.STINGRAY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, StingrayEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.KRILL.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, KrillEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.SUNFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, SunfishEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.EMU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmuEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.MARINE_IGUANA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MarineIguanaEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.LONGHORN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LonghornEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.TOUCAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, ToucanEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.HUMPBACK_WHALE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, HumpbackWhaleEntity::checkFishSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.PILLBUG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PillbugEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.BLUNT_HEADED_TREE_SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BluntHeadedTreeSnakeEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.GECKO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GeckoEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.LION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LionEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.BARRELEYE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, BarreleyeEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.ANGELFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, AngelfishEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.NEON_TETRA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, NeonTetraEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.FLOUNDER.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, FlounderEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.PHEASANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PheasantEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.HOGBEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HogbearEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STINGRAY.get(), StingrayEntity.setAttributes());
        event.put(ModEntities.SUNFISH.get(), SunfishEntity.setAttributes());
        event.put(ModEntities.EMU.get(), EmuEntity.setAttributes());
        event.put(ModEntities.KRILL.get(), KrillEntity.setAttributes());
        event.put(ModEntities.MARINE_IGUANA.get(), MarineIguanaEntity.setAttributes());
        event.put(ModEntities.LONGHORN.get(), LonghornEntity.setAttributes());
        event.put(ModEntities.TOUCAN.get(), ToucanEntity.setAttributes());
        event.put(ModEntities.HUMPBACK_WHALE.get(), HumpbackWhaleEntity.setAttributes());
        event.put(ModEntities.PILLBUG.get(), PillbugEntity.setAttributes());
        event.put(ModEntities.BLUNT_HEADED_TREE_SNAKE.get(), BluntHeadedTreeSnakeEntity.setAttributes());
        event.put(ModEntities.GECKO.get(), GeckoEntity.setAttributes());
        event.put(ModEntities.LION.get(), LionEntity.setAttributes());
        event.put(ModEntities.BARRELEYE.get(), BarreleyeEntity.setAttributes());
        event.put(ModEntities.ANGELFISH.get(), AngelfishEntity.setAttributes());
        event.put(ModEntities.NEON_TETRA.get(), NeonTetraEntity.setAttributes());
        event.put(ModEntities.FLOUNDER.get(), FlounderEntity.setAttributes());
        event.put(ModEntities.PHEASANT.get(), PheasantEntity.setAttributes());
        event.put(ModEntities.HOGBEAR.get(), HogbearEntity.setAttributes());

    }




}
