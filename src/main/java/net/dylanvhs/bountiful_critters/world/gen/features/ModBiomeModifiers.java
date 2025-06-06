package net.dylanvhs.bountiful_critters.world.gen.features;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.tags.ModTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModBiomeModifiers {
    private static final ResourceKey<BiomeModifier> ADD_OAK_NESTED_OAK_TREES = createKey("add_birted_birch_trees");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        context.register(ADD_OAK_NESTED_OAK_TREES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomeTag(context, ModTags.OAK_NESTED_TREE_SPAWNS_IN), getPlacedFeature(context, ModPlacedFeatures.OAK_NESTED_OAK_TREES), GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    @SafeVarargs
    @NotNull
    private static HolderSet.Direct<PlacedFeature> getPlacedFeature(BootstapContext<BiomeModifier> context, ResourceKey<PlacedFeature>... placedFeature) {
        return HolderSet.direct(Stream.of(placedFeature).map(resourceKey -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(resourceKey)).collect(Collectors.toList()));
    }

    @NotNull
    private static HolderSet.Direct<PlacedFeature> getPlacedFeature(BootstapContext<BiomeModifier> context, ResourceKey<PlacedFeature> placedFeature) {
        return HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature));
    }

    @NotNull
    private static HolderSet.Named<Biome> biomeTag(BootstapContext<BiomeModifier> context, TagKey<Biome> tag) {
        return context.lookup(Registries.BIOME).getOrThrow(tag);
    }

    public static ResourceKey<BiomeModifier> createKey(String string) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(BountifulCritters.MOD_ID, string));
    }

}
