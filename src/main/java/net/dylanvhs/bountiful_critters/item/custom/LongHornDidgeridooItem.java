package net.dylanvhs.bountiful_critters.item.custom;

import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class LongHornDidgeridooItem extends Item {
    public LongHornDidgeridooItem(Properties pProperties) {
        super(pProperties);
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.gameEvent(GameEvent.ITEM_INTERACT_START);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.LONG_HORN_DIDGERIDOO.get(), SoundSource.PLAYERS, 1.8f, 1f);
        pPlayer.getCooldowns().addCooldown(this, 40);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        itemstack.hurtAndBreak(1, pPlayer, (player) -> {
            player.broadcastBreakEvent(pPlayer.getUsedItemHand());
            });
        return InteractionResultHolder.success(itemstack) ;
    }
}
