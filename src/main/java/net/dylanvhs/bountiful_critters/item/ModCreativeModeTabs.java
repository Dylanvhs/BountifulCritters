package net.dylanvhs.bountiful_critters.item;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BountifulCritters.MOD_ID);
    public static final RegistryObject<CreativeModeTab> BOUNTIFUL_CRITTERS_TAB = CREATIVE_MODE_TABS.register("bountiful_critters_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STINGRAY_BUCKET.get()))

                    .title(Component.translatable("creativetab.bountiful_critters_tab"))
                    .displayItems((pParameters, pOutput) -> {



                        pOutput.accept(ModItems.STINGRAY_BUCKET.get());
                        pOutput.accept(ModItems.SUNFISH_BUCKET.get());
                        pOutput.accept(ModItems.KRILL_BUCKET.get());
                        pOutput.accept(ModItems.MARINE_IGUANA_BUCKET.get());
                        pOutput.accept(ModItems.POTTED_PILLBUG.get());
                        pOutput.accept(ModItems.RAW_KRILL.get());
                        pOutput.accept(ModItems.FRIED_KRILL.get());
                        pOutput.accept(ModItems.KRILL_COCKTAIL.get());
                        pOutput.accept(ModItems.RAW_SUNFISH_MEAT.get());
                        pOutput.accept(ModItems.COOKED_SUNFISH_MEAT.get());
                        pOutput.accept(ModItems.SUNFISH_SUSHI.get());
                        pOutput.accept(ModItems.EMU_EGG.get());
                        pOutput.accept(ModItems.BOILED_EMU_EGG.get());
                        pOutput.accept(ModItems.LONGHORN_HORN.get());
                        pOutput.accept(ModItems.LONGHORN_DIDGERIDOO.get());
                        pOutput.accept(ModItems.SALT.get());
                        pOutput.accept(ModBlocks.SALT_LAMP.get());
                        pOutput.accept(ModItems.SALTED_KELP.get());
                        pOutput.accept(ModItems.SEAGRASS_BALL.get());
                        pOutput.accept(ModBlocks.SEAGRASS_BALL_BLOCK.get());
                        pOutput.accept(ModItems.RAW_PILLBUG.get());
                        pOutput.accept(ModItems.ROASTED_PILLBUG.get());
                        pOutput.accept(ModItems.POISONOUS_PILLBUG.get());
                        pOutput.accept(ModItems.PILLBUG_THROWABLE.get());
                        pOutput.accept(ModItems.REPTILE_BAG.get());
                        pOutput.accept(ModItems.BAGGED_GECKO.get());
                        pOutput.accept(ModItems.RETURNING_MEMORY_MUSIC_DISC.get());


                        pOutput.accept(ModItems.STINGRAY_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SUNFISH_SPAWN_EGG.get());
                        pOutput.accept(ModItems.KRILL_SPAWN_EGG.get());
                        pOutput.accept(ModItems.EMU_SPAWN_EGG.get());
                        pOutput.accept(ModItems.MARINE_IGUANA_SPAWN_EGG.get());
                        pOutput.accept(ModItems.LONG_HORN_SPAWN_EGG.get());
                        pOutput.accept(ModItems.TOUCAN_SPAWN_EGG.get());
                        pOutput.accept(ModItems.HUMPBACK_WHALE_SPAWN_EGG.get());
                        pOutput.accept(ModItems.PILLBUG_SPAWN_EGG.get());
                        pOutput.accept(ModItems.BLUNT_HEADED_TREE_SNAKE_SPAWN_EGG.get());
                        pOutput.accept(ModItems.GECKO_SPAWN_EGG.get());




                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}