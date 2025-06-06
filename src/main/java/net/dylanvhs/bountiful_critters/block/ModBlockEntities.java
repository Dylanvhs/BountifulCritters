package net.dylanvhs.bountiful_critters.block;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.block.entity.OakNestEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BountifulCritters.MOD_ID);

    public static final RegistryObject<BlockEntityType<OakNestEntity>> OAK_NEST = BLOCK_ENTITY_TYPES.register("oak_nest_entity",
            () -> BlockEntityType.Builder.of(OakNestEntity::new, ModBlocks.OAK_NEST.get()).build(null));
}
