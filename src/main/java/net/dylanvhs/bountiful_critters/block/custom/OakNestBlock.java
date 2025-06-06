package net.dylanvhs.bountiful_critters.block.custom;

import net.dylanvhs.bountiful_critters.block.ModBlockEntities;
import net.dylanvhs.bountiful_critters.block.entity.OakNestEntity;
import net.dylanvhs.bountiful_critters.block.property.ModProperties;
import net.dylanvhs.bountiful_critters.entity.custom.flying.HoopoeEntity;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class OakNestBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty HOOPOES = ModProperties.HOOPOES;
    public static final IntegerProperty EGGS = ModProperties.HOOPOE_EGGS;

    public OakNestBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HOOPOES, 0).setValue(EGGS, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (state.getValue(EGGS) > 0 && player.getItemInHand(hand).isEmpty()) {
            if (!world.isClientSide() && world.getBlockEntity(pos) instanceof OakNestEntity oakNestEntity) {
                oakNestEntity.angerHoopoes(player, state, OakNestEntity.NestState.EMERGENCY);
            }
            world.setBlockAndUpdate(pos, state.setValue(EGGS, state.getValue(EGGS) - 1));
            BlockPos itemPos = pos.relative(state.getValue(FACING));
            world.playSound(null, itemPos, SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 1 ,1);
            popResource(world, itemPos, new ItemStack(ModItems.HOOPOE_EGG.get()));
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, blockHitResult);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        if (!level.isClientSide && blockEntity instanceof OakNestEntity oakNestEntity) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0) {
                oakNestEntity.angerHoopoes(player, blockState, OakNestEntity.NestState.EMERGENCY);
                level.updateNeighbourForOutputSignal(blockPos, this);
                this.angerNearbyBirts(level, blockPos);
            }
        }
    }

    private void angerNearbyBirts(Level world, BlockPos pos) {
        List<HoopoeEntity> hoopoeList = world.getEntitiesOfClass(HoopoeEntity.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
        if (!hoopoeList.isEmpty()) {
            List<Player> playerList = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
            for (HoopoeEntity hoopoe : hoopoeList) {
                if (hoopoe.getTarget() != null) continue;
                hoopoe.setTarget(playerList.get(world.random.nextInt(playerList.size())));
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(EGGS, this.defaultBlockState().getValue(EGGS)).setValue(HOOPOES, this.defaultBlockState().getValue(HOOPOES));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HOOPOES, EGGS);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OakNestEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : OakNestBlock.createTickerHelper(blockEntityType, ModBlockEntities.OAK_NEST.get(), OakNestEntity::serverTick);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState blockState, Player player) {
        BlockEntity blockEntity;
        if (!world.isClientSide() && player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && (blockEntity = world.getBlockEntity(pos)) instanceof OakNestEntity) {
            OakNestEntity blockEntity1 = (OakNestEntity)blockEntity;
            ItemStack itemStack = new ItemStack(this);
            boolean bl = !blockEntity1.hasNoHoopoes();
            if (bl) {
                CompoundTag nbtCompound = new CompoundTag();
                nbtCompound.put("Birts", blockEntity1.getHoopoes());
                BlockItem.setBlockEntityData(itemStack, ModBlockEntities.OAK_NEST.get(), nbtCompound);
                nbtCompound = new CompoundTag();
                itemStack.addTagElement("BlockStateTag", nbtCompound);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            }
        }
        super.playerWillDestroy(world, pos, blockState, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) {
            BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof OakNestEntity blockEntity1) {
                blockEntity1.angerHoopoes(null, blockState, OakNestEntity.NestState.EMERGENCY);
            }
        }

        return super.getDrops(blockState, builder);
    }

}