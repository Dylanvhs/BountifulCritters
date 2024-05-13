package net.dylanvhs.bountiful_critters.datagen;


import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BountifulCritters.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.STINGRAY_BUCKET);
        simpleItem(ModItems.SUNFISH_BUCKET);
        simpleItem(ModItems.KRILL_BUCKET);
        simpleItem(ModItems.MARINE_IGUANA_BUCKET);
        simpleItem(ModItems.LONGHORN_HORN);
        simpleItem(ModItems.SALT);
        simpleItem(ModItems.SALTED_KELP);
        simpleItem(ModItems.SEAGRASS_BALL);
        simpleItem(ModItems.RAW_SUNFISH_MEAT);
        simpleItem(ModItems.COOKED_SUNFISH_MEAT);
        simpleItem(ModItems.SUNFISH_SUSHI);
        simpleItem(ModItems.RAW_KRILL);
        simpleItem(ModItems.FRIED_KRILL);
        simpleItem(ModItems.KRILL_COCKTAIL);
        simpleItem(ModItems.EMU_EGG);
        simpleItem(ModItems.BOILED_EMU_EGG);
        simpleItem(ModItems.RAW_PILLBUG);
        simpleItem(ModItems.ROASTED_PILLBUG);
        simpleItem(ModItems.POISONOUS_PILLBUG);
        simpleItem(ModItems.POTTED_PILLBUG);
        simpleItem(ModItems.REPTILE_BAG);
        simpleItem(ModItems.BAGGED_GECKO);
        simpleItem(ModItems.RETURNING_MEMORY_MUSIC_DISC);


        withExistingParent(ModItems.EMU_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.STINGRAY_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SUNFISH_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.KRILL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.MARINE_IGUANA_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.LONG_HORN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.TOUCAN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.HUMPBACK_WHALE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.PILLBUG_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.BLUNT_HEADED_TREE_SNAKE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.GECKO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BountifulCritters.MOD_ID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BountifulCritters.MOD_ID,"block/" + item.getId().getPath()));
    }

}


