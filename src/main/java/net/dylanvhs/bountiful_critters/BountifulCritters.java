package net.dylanvhs.bountiful_critters;

import com.mojang.logging.LogUtils;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.client.*;
import net.dylanvhs.bountiful_critters.item.ModCreativeModeTabs;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BountifulCritters.MOD_ID)
public class BountifulCritters
{

    public static final String MOD_ID = "bountiful_critters";

    private static final Logger LOGGER = LogUtils.getLogger();
    public BountifulCritters()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);





        modEventBus.addListener(this::commonSetup);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register
                    (ModEntities.STINGRAY.get(), StingrayRenderer:: new);

            EntityRenderers.register
                    (ModEntities.SUNFISH.get(), SunfishRenderer:: new);

            EntityRenderers.register
                    (ModEntities.EMU.get(), EmuRenderer:: new);

            EntityRenderers.register
                    (ModEntities.KRILL.get(), KrillRenderer:: new);

            EntityRenderers.register
                    (ModEntities.MARINE_IGUANA.get(), MarineIguanaRenderer:: new);

            EntityRenderers.register
                    (ModEntities.EMU_EGG.get(), ThrownItemRenderer::new);

        }
    }
}