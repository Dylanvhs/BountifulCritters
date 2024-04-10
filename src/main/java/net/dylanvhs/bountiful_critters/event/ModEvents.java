package net.dylanvhs.bountiful_critters.event;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.custom.*;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
    }



    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent e) {
        e.register(ModEntities.STINGRAY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, StingrayEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.KRILL.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, KrillEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.SUNFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, SunfishEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.EMU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmuEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        e.register(ModEntities.MARINE_IGUANA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmuEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STINGRAY.get(), StingrayEntity.setAttributes());
        event.put(ModEntities.SUNFISH.get(), SunfishEntity.setAttributes());
        event.put(ModEntities.EMU.get(), EmuEntity.setAttributes());
        event.put(ModEntities.KRILL.get(), KrillEntity.setAttributes());
        event.put(ModEntities.MARINE_IGUANA.get(), MarineIguanaEntity.setAttributes());

    }




}
