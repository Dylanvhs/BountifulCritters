package net.dylanvhs.bountiful_critters.datagen;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.loot.SuspiciousSandItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, BountifulCritters.MOD_ID);
    }

    @Override
    protected void start() {

        add("bountiful_music_disc_from_suspicious_sand", new SuspiciousSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build() }, ModItems.BOUNTIFUL_MUSIC_DISC.get()));

        add("bountiful_music_disc_from_suspicious_gravel", new SuspiciousSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/ocean_ruin_warm")).build() }, ModItems.SNEEZE_MUSIC_DISC.get()));

    }
}