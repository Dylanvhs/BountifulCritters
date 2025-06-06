package net.dylanvhs.bountiful_critters.world.gen.features;

import com.google.common.collect.ImmutableList;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_NESTED_OAK = registerConfiguredFeature("oak_nested_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_NESTED_OAK_TREE_FILTERED = registerConfiguredFeature("oak_nested_oak_tree_filtered");
    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeature(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BountifulCritters.MOD_ID, id));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstapContext) {
        HolderGetter<PlacedFeature> holderGetter2 = bootstapContext.lookup(Registries.PLACED_FEATURE);
        FeatureUtils.register(bootstapContext, OAK_NESTED_OAK, Feature.TREE, oakNestedOak().build());
        FeatureUtils.register(bootstapContext, OAK_NESTED_OAK_TREE_FILTERED, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(holderGetter2.getOrThrow(ModPlacedFeatures.OAK_NESTED_OAK_TREE_CHECKED), 0.0F)), holderGetter2.getOrThrow(ModPlacedFeatures.OAK_NESTED_OAK_TREE_CHECKED)));
    }

    private static TreeConfiguration.TreeConfigurationBuilder builder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius) {
        return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(log), new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight), BlockStateProvider.simple(leaves), new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1));
    }

    private static TreeConfiguration.TreeConfigurationBuilder oakNestedOak() {
        return builder(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 5, 2).decorators(ImmutableList.of(OakNestLogDecorator.INSTANCE)).ignoreVines();
    }

}
