package net.dylanvhs.bountiful_critters.criterion;

import com.google.gson.JsonObject;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


public class ModCriteriaTriggers extends SimpleCriterionTrigger<ModCriteriaTriggers.TriggerInstance> {
    private final ResourceLocation ID;

    public ModCriteriaTriggers(String name) {
        ID = new ResourceLocation(BountifulCritters.MOD_ID, name);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    protected TriggerInstance createInstance(JsonObject p_66248_, ContextAwarePredicate predicate, DeserializationContext p_66250_) {
        return new ModCriteriaTriggers.TriggerInstance(ID, predicate);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation id, ContextAwarePredicate predicate) {
            super(id, predicate);
        }

    }
}
