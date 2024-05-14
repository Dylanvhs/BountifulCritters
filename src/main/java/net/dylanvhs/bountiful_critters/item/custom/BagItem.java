package net.dylanvhs.bountiful_critters.item.custom;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.Bagable;
import net.dylanvhs.bountiful_critters.entity.custom.GeckoEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BagItem extends BucketItem {
    private final Supplier<? extends EntityType<?>> entityType;
    private final Item item1;

    public BagItem(Supplier<EntityType<?>> entityType, Item item, Properties properties) {
        this(entityType, Fluids.EMPTY, item, properties);
    }
    public BagItem(Supplier<EntityType<?>> entityType, Fluid fluid, Item item, Properties properties) {
        super(fluid, properties);
        this.item1 = item;
        this.entityType = entityType;
    }

    private void spawn(ServerLevel pServerLevel, ItemStack pBucketedMobStack, BlockPos pPos) {
        Entity entity = getEntityType().spawn(pServerLevel, pBucketedMobStack, (Player)null, pPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bagable bucketable) {
            bucketable.loadFromBagTag(pBucketedMobStack.getOrCreateTag());
            bucketable.setFromBag(true);
        }

    }

    public void checkExtraContent(@Nullable Player pPlayer, Level pLevel, ItemStack pContainerStack, BlockPos pPos) {
        if (pLevel instanceof ServerLevel) {
            this.spawn((ServerLevel)pLevel, pContainerStack, pPos);
            pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, pPos);
        }

    }

    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos) {
        pLevel.playSound(pPlayer, pPos, getEmptySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            Player player = context.getPlayer();
            InteractionHand hand = context.getHand();
            ItemStack heldItem = player.getItemInHand(hand);
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            }
            else {
                blockpos1 = blockpos.relative(direction);
            }
            Supplier<? extends EntityType<?>> entitytype = entityType;
            Entity entityType = entitytype.get().spawn((ServerLevel) world, heldItem, player, blockpos1, MobSpawnType.BUCKET, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);

            if (entityType != null) {
                if(!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                    if (heldItem.isEmpty()) {
                        player.setItemInHand(hand, item1.getDefaultInstance());
                    }
                }
                if (entityType instanceof Bagable bucketable) {
                    bucketable.loadFromBagTag(heldItem.getOrCreateTag());
                    bucketable.setFromBag(true);
                }

                playEmptySound(player, world, blockpos);
            }
            return InteractionResult.SUCCESS;
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (getEntityType() == ModEntities.GECKO.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("BagVariantTag", 3)) {
                int i = compoundnbt.getInt("BagVariantTag");
                String s = "entity.bountiful_critters.gecko.variant_" + GeckoEntity.getVariantName(i);
                tooltip.add((Component.translatable(s)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
            }
        }

    }

    protected EntityType<?> getEntityType() {
        return entityType.get();
    }

    protected SoundEvent getEmptySound() {
        return SoundEvents.BUNDLE_DROP_CONTENTS;
    }
}
