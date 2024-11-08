package net.dylanvhs.bountiful_critters.item.custom;

import net.dylanvhs.bountiful_critters.entity.custom.PillbugProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PillbugProjectileItem extends Item {
    public PillbugProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound((Player)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!pLevel.isClientSide) {
            PillbugProjectileEntity pillbugProjectileEntity = new PillbugProjectileEntity(pLevel, pPlayer);
            pillbugProjectileEntity.setItem(itemstack);
            pillbugProjectileEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            if (itemstack.hasTag()) {
                pLevel.addFreshEntity(pillbugProjectileEntity);
                pillbugProjectileEntity.setCustomName(this.getName(itemstack));
            } else  pLevel.addFreshEntity(pillbugProjectileEntity);

        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}