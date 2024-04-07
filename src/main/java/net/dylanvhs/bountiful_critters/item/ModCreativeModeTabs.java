package net.dylanvhs.bountiful_critters.item;

import net.dylanvhs.bountiful_critters.BountifulCritters;
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
                        pOutput.accept(ModItems.EMU_EGG.get());
                        pOutput.accept(ModItems.BOILED_EMU_EGG.get());

                        pOutput.accept(ModItems.EMU_SPAWN_EGG.get());
                        pOutput.accept(ModItems.STINGRAY_SPAWN_EGG.get());




                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}