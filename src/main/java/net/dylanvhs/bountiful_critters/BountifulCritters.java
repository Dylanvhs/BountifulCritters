package net.dylanvhs.bountiful_critters;

import com.mojang.logging.LogUtils;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.effect.ModMobEffects;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.custom.land.PillbugEntity;
import net.dylanvhs.bountiful_critters.item.ModCreativeModeTabs;
import net.dylanvhs.bountiful_critters.loot.ModLootModifiers;
import net.dylanvhs.bountiful_critters.particles.ModParticles;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        ModEntities.ENTITY_TYPES.register(modEventBus);

        ModSounds.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModParticles.PARTICLE_TYPES.register(modEventBus);

        ModBlocks.BLOCKS.register(modEventBus);

        ModMobEffects.EFFECT_DEF_REG.register(modEventBus);





        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModItems::initDispenser);
        DispenserBlock.registerBehavior(ModItems.PILLBUG_THROWABLE.get(), new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                ServerLevel level = source.getLevel();
                BlockPos pos = source.getPos();
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);

                PillbugEntity pillbug = new PillbugEntity(ModEntities.PILLBUG.get(), level);
                UUID id = pillbug.getUUID();
                pillbug.deserializeNBT(stack.getOrCreateTag().getCompound("PillbugData"));
                pillbug.setUUID(id);
                pillbug.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
                pillbug.setProjectile(true);
                pillbug.shoot(direction.getStepX(), ((float)direction.getStepY() + 0.1F), direction.getStepZ(), 3.0F, 0.0F);

                level.addFreshEntity(pillbug);

                return ItemStack.EMPTY;
            }
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}