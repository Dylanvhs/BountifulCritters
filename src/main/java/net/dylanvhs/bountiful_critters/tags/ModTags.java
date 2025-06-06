package net.dylanvhs.bountiful_critters.tags;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;

public interface ModTags {

    TagKey<Biome> OAK_NESTED_TREE_SPAWNS_IN = TagKey.create(Registries.BIOME, new ResourceLocation(BountifulCritters.MOD_ID, "oak_nested_tree_spawns_in"));

    TagKey<PoiType> HOOPOE_HOME = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(BountifulCritters.MOD_ID, "hoopoe_home"));
}
