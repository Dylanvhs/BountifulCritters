package net.dylanvhs.bountiful_critters;

import com.mojang.logging.LogUtils;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.effect.ModMobEffects;
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

import java.util.ArrayList;
import java.util.List;

@Mod(BountifulCritters.MOD_ID)
public class BountifulCritters
{

    public static final String MOD_ID = "bountiful_critters";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final List<Runnable> CALLBACKS = new ArrayList<>();
    public BountifulCritters()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);

        ModBlocks.BLOCKS.register(modEventBus);

        ModMobEffects.EFFECT_DEF_REG.register(modEventBus);





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
                    (ModEntities.LONGHORN.get(), LongHornRenderer:: new);

            EntityRenderers.register
                    (ModEntities.TOUCAN.get(), ToucanRenderer:: new);

            EntityRenderers.register
                    (ModEntities.HUMPBACK_WHALE.get(), HumpbackWhaleRenderer:: new);

            EntityRenderers.register
                    (ModEntities.PILLBUG.get(), PillbugRenderer:: new);

            EntityRenderers.register
                    (ModEntities.BLUNT_HEADED_TREE_SNAKE.get(), BluntHeadedTreeSnakeRenderer:: new);

            EntityRenderers.register
                    (ModEntities.GECKO.get(), GeckoRenderer:: new);

            EntityRenderers.register
                    (ModEntities.EMU_EGG.get(), ThrownItemRenderer::new);

            EntityRenderers.register
                    (ModEntities.THROWABLE_PILLBUG.get(), ThrownItemRenderer::new);

        }
    }
}