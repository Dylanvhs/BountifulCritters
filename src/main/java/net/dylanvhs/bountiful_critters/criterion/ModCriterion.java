package net.dylanvhs.bountiful_critters.criterion;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID)
public class ModCriterion {

    public static final ModCriteriaTriggers THROW_PILLBUG_IN_THE_VOID = CriteriaTriggers.register(new ModCriteriaTriggers("throw_pillbug_in_the_void"));
    public static final ModCriteriaTriggers THROW_PILLBUG = CriteriaTriggers.register(new ModCriteriaTriggers("throw_pillbug"));

}
