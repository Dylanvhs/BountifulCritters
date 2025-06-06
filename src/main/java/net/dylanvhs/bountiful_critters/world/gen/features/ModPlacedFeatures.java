package net.dylanvhs.bountiful_critters.world.gen.features;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> OAK_NESTED_OAK_TREE_CHECKED = registerPlacedFeature("oak_nested_oak");
    public static final ResourceKey<PlacedFeature> OAK_NESTED_OAK_TREES = registerPlacedFeature("oak_nested_oak_trees");

    public static void bootstrap(BootstapContext<PlacedFeature> bootstapContext) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = bootstapContext.lookup(Registries.CONFIGURED_FEATURE);
        PlacementUtils.register(bootstapContext, OAK_NESTED_OAK_TREE_CHECKED, holderGetter.getOrThrow(ModConfiguredFeatures.OAK_NESTED_OAK), PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContext, OAK_NESTED_OAK_TREES, holderGetter.getOrThrow(ModConfiguredFeatures.OAK_NESTED_OAK_TREE_FILTERED), VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(75)));
    }

    public static ResourceKey<PlacedFeature> registerPlacedFeature(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(BountifulCritters.MOD_ID, id));
    }
}
