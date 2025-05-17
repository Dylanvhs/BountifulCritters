package net.dylanvhs.bountiful_critters.datagen;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.RegistryHelper;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BountifulCritters.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Map.Entry<RegistryObject<Item>, ModelTemplate> entry : RegistryHelper.ITEM_MODELS.entrySet()) {
            if (entry.getValue() == ModelTemplates.FLAT_ITEM)
                simpleItem(entry.getKey());
        }

        for (RegistryObject<Item> entry : RegistryHelper.ITEMS.getEntries()) {
            if (entry.get() instanceof ForgeSpawnEggItem)
                simpleEggItem(entry);
        }
    }

    private ItemModelBuilder simpleEggItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BountifulCritters.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BountifulCritters.MOD_ID, "block/" + item.getId().getPath()));
    }

}


