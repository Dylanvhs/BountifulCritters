package net.dylanvhs.bountiful_critters.event;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.stringtemplate.v4.ST;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        ItemProperties.register(ModItems.STINGRAY_BUCKET.get(), new ResourceLocation(BountifulCritters.MOD_ID, "variant"), (stack, world, player, i) -> stack.hasTag() ? stack.getOrCreateTag().getInt("Variant") : 0);
    }



    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent e) {
        e.register(ModEntities.STINGRAY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.WORLD_SURFACE, StingrayEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);

    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STINGRAY.get(), StingrayEntity.setAttributes());

    }




}
