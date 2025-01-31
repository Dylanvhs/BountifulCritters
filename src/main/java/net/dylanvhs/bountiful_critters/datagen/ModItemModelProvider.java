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
        simpleItem(ModItems.ANGELFISH_BUCKET);
        simpleItem(ModItems.BARRELEYE_BUCKET);
        simpleItem(ModItems.NEON_TETRA_BUCKET);
        simpleItem(ModItems.FLOUNDER_BUCKET);
        simpleItem(ModItems.MARINE_IGUANA_BUCKET);
        simpleItem(ModItems.LONGHORN_HORN);
        simpleItem(ModItems.SALT);
        simpleItem(ModItems.SALTED_KELP);
        simpleItem(ModItems.SEAGRASS_BALL);
        simpleItem(ModItems.DRIED_SEAGRASS_BALL);
        simpleItem(ModItems.STICKY_ARROW);
        simpleItem(ModItems.RAW_SUNFISH_MEAT);
        simpleItem(ModItems.COOKED_SUNFISH_MEAT);
        simpleItem(ModItems.RAW_GOLDEN_SUNFISH_MEAT);
        simpleItem(ModItems.COOKED_GOLDEN_SUNFISH_MEAT);
        simpleItem(ModItems.SUNFISH_SUSHI);
        simpleItem(ModItems.RAW_KRILL);
        simpleItem(ModItems.FRIED_KRILL);
        simpleItem(ModItems.KRILL_COCKTAIL);
        simpleItem(ModItems.RAW_ANGELFISH);
        simpleItem(ModItems.RAW_BARRELEYE);
        simpleItem(ModItems.RAW_NEON_TETRA);
        simpleItem(ModItems.RAW_FLOUNDER);
        simpleItem(ModItems.EMU_EGG);
        simpleItem(ModItems.EMU_EGG_SHELL_PIECES);
        simpleItem(ModItems.BOILED_EMU_EGG);
        simpleItem(ModItems.RAW_PILLBUG);
        simpleItem(ModItems.ROASTED_PILLBUG);
        simpleItem(ModItems.POISONOUS_PILLBUG);
        simpleItem(ModItems.PILLBUG_SCUTE);
        simpleItem(ModItems.POISONOUS_PILLBUG_SCUTE);
        simpleItem(ModItems.RAW_PHEASANT);
        simpleItem(ModItems.COOKED_PHEASANT);
        simpleItem(ModItems.PHEASANT_FEATHER);
        simpleItem(ModItems.PHEASANT_EGG);
        simpleItem(ModItems.REPTILE_BAG);
        simpleItem(ModItems.BAGGED_GECKO);
        simpleItem(ModItems.BAGGED_BLUNT_HEADED_TREE_SNAKE);
        simpleItem(ModItems.SNAKE_HOOK);
        simpleItem(ModItems.CAPTURED_BLUNT_HEADED_TREE_SNAKE);
        simpleItem(ModItems.MEMORIES_MUSIC_DISC);
        simpleItem(ModItems.BOUNTIFUL_MUSIC_DISC);
        simpleItem(ModItems.SNEEZE_MUSIC_DISC);
        simpleItem(ModItems.BUGS_MUSIC_DISC);
        simpleItem(ModItems.LION_ARMOR);


        withExistingParent(ModItems.EMU_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.STINGRAY_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SUNFISH_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.KRILL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.MARINE_IGUANA_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.LONGHORN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.TOUCAN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.HUMPBACK_WHALE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.PILLBUG_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.BLUNT_HEADED_TREE_SNAKE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.GECKO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.LION_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.BARRELEYE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ANGELFISH_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.NEON_TETRA_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FLOUNDER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.PHEASANT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.HOGBEAR_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

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


