package net.dylanvhs.bountiful_critters.item.custom;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.interfaces.Hookable;
import net.dylanvhs.bountiful_critters.entity.custom.land.BluntHeadedTreeSnakeEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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

public class HookItem extends BucketItem {
    private final Supplier<? extends EntityType<?>> entityType;
    private final Item item1;
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    private static final Component BLUNT_HEADED_TREE_SNAKE = Component.translatable(Util.makeDescriptionId("snake", new ResourceLocation(BountifulCritters.MOD_ID,"blunt_headed_tree_snake"))).withStyle(TITLE_FORMAT);

    public HookItem(Supplier<EntityType<?>> entityType, Item item, Properties properties) {
        this(entityType, Fluids.EMPTY, item, properties);
    }
    public HookItem(Supplier<EntityType<?>> entityType, Fluid fluid, Item item, Properties properties) {
        super(fluid, properties);
        this.item1 = item;
        this.entityType = entityType;
    }

    private void spawn(ServerLevel pServerLevel, ItemStack pBucketedMobStack, BlockPos pPos) {
        Entity entity = getEntityType().spawn(pServerLevel, pBucketedMobStack, (Player)null, pPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Hookable bucketable) {
            bucketable.loadFromHookTag(pBucketedMobStack.getOrCreateTag());
            bucketable.setFromHook(true);
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
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack heldItem = player.getItemInHand(hand);
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockstate = world.getBlockState(blockpos);

        if (world.isClientSide) {
            if (entityType != null) {
                playEmptySound(player, world, blockpos);
            }
            return InteractionResult.SUCCESS;
        }
        else {
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
                if (entityType instanceof Hookable bucketable) {
                    bucketable.loadFromHookTag(heldItem.getOrCreateTag());
                    bucketable.setFromHook(true);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        return InteractionResultHolder.pass(itemstack);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {

        if (getEntityType() == ModEntities.BLUNT_HEADED_TREE_SNAKE.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("HookVariantTag", 3)) {
                int i = compoundnbt.getInt("HookVariantTag");
                tooltip.add(BLUNT_HEADED_TREE_SNAKE);
                String s = "entity.bountiful_critters.blunt_headed_tree_snake.variant_" + BluntHeadedTreeSnakeEntity.getVariantName(i);
                tooltip.add((Component.translatable(s)).withStyle(DESCRIPTION_FORMAT).withStyle(ChatFormatting.ITALIC));
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

