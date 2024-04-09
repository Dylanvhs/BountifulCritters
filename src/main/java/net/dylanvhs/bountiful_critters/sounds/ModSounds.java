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

    public static final RegistryObject<SoundEvent> RETURNING_MEMORY = registerSoundEvents("returning_memory");


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BountifulCritters.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

