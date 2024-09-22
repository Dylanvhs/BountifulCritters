package net.dylanvhs.bountiful_critters.sounds;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BountifulCritters.MOD_ID);

    public static final RegistryObject<SoundEvent> EMU_AMBIENT = registerSoundEvents("emu_ambient");
    public static final RegistryObject<SoundEvent> EMU_HURT = registerSoundEvents("emu_hurt");
    public static final RegistryObject<SoundEvent> EMU_DEATH = registerSoundEvents("emu_death");

    public static final RegistryObject<SoundEvent> TOUCAN_AMBIENT = registerSoundEvents("toucan_ambient");
    public static final RegistryObject<SoundEvent> TOUCAN_HURT = registerSoundEvents("toucan_hurt");
    public static final RegistryObject<SoundEvent> TOUCAN_DEATH = registerSoundEvents("toucan_death");

    public static final RegistryObject<SoundEvent> PILLBUG_AMBIENT = registerSoundEvents("pillbug_ambient");
    public static final RegistryObject<SoundEvent> PILLBUG_HURT = registerSoundEvents("pillbug_hurt");
    public static final RegistryObject<SoundEvent> PILLBUG_STEP = registerSoundEvents("pillbug_step");
    public static final RegistryObject<SoundEvent> PILLBUG_DEATH = registerSoundEvents("pillbug_death");
    public static final RegistryObject<SoundEvent> PILLBUG_BOUNCE = registerSoundEvents("pillbug_bounce");

    public static final RegistryObject<SoundEvent> SNAKE_HISS = registerSoundEvents("snake_hiss");

    public static final RegistryObject<SoundEvent> GECKO_AMBIENT = registerSoundEvents("gecko_ambient");

    public static final RegistryObject<SoundEvent> WHALE_AMBIENT = registerSoundEvents("whale_ambient");

    public static final RegistryObject<SoundEvent> PHEASANT_AMBIENT = registerSoundEvents("pheasant_ambient");
    public static final RegistryObject<SoundEvent> PHEASANT_HURT = registerSoundEvents("pheasant_hurt");

    public static final RegistryObject<SoundEvent> LONG_HORN_DIDGERIDOO = registerSoundEvents("longhorn_didgeridoo");

    public static final RegistryObject<SoundEvent> MEMORIES = registerSoundEvents("memories");
    public static final RegistryObject<SoundEvent> BOUNTIFUL = registerSoundEvents("bountiful");


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BountifulCritters.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

