package net.dylanvhs.bountiful_critters.item.custom;

import net.dylanvhs.bountiful_critters.criterion.ModCriterion;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.custom.land.PillbugEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;


import java.util.UUID;
import java.util.function.Supplier;

public class PillbugProjectileItem extends PickItem {
    public PillbugProjectileItem(Supplier<EntityType<?>> entityType, Fluid fluid, Item item, Properties properties) {
        super(entityType,fluid, item, properties);

    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        player.playSound(SoundEvents.EGG_THROW, 1.5F,  (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.5F );

        if (!level.isClientSide) {
            PillbugEntity pillbug = new PillbugEntity(ModEntities.PILLBUG.get(), level);
            UUID id = pillbug.getUUID();
            pillbug.deserializeNBT(itemstack.getOrCreateTag().getCompound("PillbugData"));
            pillbug.setUUID(id);
            pillbug.moveTo(player.getEyePosition());
            pillbug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
            pillbug.setProjectile(true);
            pillbug.setThrower(player.getUUID());

            level.addFreshEntity(pillbug);
        }
        if (player instanceof ServerPlayer serverPlayer) ModCriterion.THROW_PILLBUG.trigger(serverPlayer);

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}