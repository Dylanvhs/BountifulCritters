package net.dylanvhs.bountiful_critters.datagen.tags;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.RegistryHelper;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, BountifulCritters.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.MUSIC_DISCS)
                .add(ModItems.MEMORIES_MUSIC_DISC.get())
                .add(ModItems.BOUNTIFUL_MUSIC_DISC.get())
                .add(ModItems.SNEEZE_MUSIC_DISC.get())
                .add(ModItems.BUGS_MUSIC_DISC.get());
        for (TagKey<Item> tag : RegistryHelper.ITEM_TAGS.keySet()) {
            for (Item item : RegistryHelper.ITEM_TAGS.get(tag))
                this.tag(tag).add(item);
        }
    }
}
