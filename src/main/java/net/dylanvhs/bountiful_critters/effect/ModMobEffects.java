package net.dylanvhs.bountiful_critters.effect;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.effect.custom.GluedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECT_DEF_REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BountifulCritters.MOD_ID);

    public static final RegistryObject<MobEffect> GLUED = EFFECT_DEF_REG.register("glued", GluedEffect::new);

}