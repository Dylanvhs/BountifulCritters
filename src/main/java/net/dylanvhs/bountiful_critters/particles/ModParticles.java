package net.dylanvhs.bountiful_critters.particles;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BountifulCritters.MOD_ID);
    public static final RegistryObject<SimpleParticleType> NEON_SHINE = register("neon_shine",false);

    private static RegistryObject<SimpleParticleType> register(String id, boolean flag) {
        return PARTICLE_TYPES.register(id, () -> new SimpleParticleType(flag));
    }
}
