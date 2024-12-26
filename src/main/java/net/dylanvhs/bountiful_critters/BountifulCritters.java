package net.dylanvhs.bountiful_critters;

import com.mojang.logging.LogUtils;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.effect.ModMobEffects;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.ModCreativeModeTabs;
import net.dylanvhs.bountiful_critters.loot.ModLootModifiers;
import net.dylanvhs.bountiful_critters.particles.ModParticles;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

        ModLootModifiers.register(modEventBus);

        ModParticles.PARTICLE_TYPES.register(modEventBus);

        ModBlocks.BLOCKS.register(modEventBus);

        ModMobEffects.EFFECT_DEF_REG.register(modEventBus);





        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModItems::initDispenser);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}