package net.dylanvhs.bountiful_critters.entity;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.custom.StingrayEntity;
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
                    () -> EntityType.Builder.of(StingrayEntity::new, MobCategory.WATER_AMBIENT)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(BountifulCritters.MOD_ID, "stingray").toString()));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}