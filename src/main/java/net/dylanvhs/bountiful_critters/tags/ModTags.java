package net.dylanvhs.bountiful_critters.tags;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {

    public static final TagKey<PoiType> SNAKE_POT = registerPoiTypeTag("snake_pot");



    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }

    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }

    private static TagKey<Structure> registerStructureTag(String name) {
        return TagKey.create(Registries.STRUCTURE, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }

    private static TagKey<PoiType> registerPoiTypeTag(String name) {
        return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(BountifulCritters.MOD_ID, name));
    }
}

