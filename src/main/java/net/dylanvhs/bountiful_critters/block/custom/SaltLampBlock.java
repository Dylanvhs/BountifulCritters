package net.dylanvhs.bountiful_critters.block.custom;

import javax.annotation.Nullable;

import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltLampBlock extends Block {

    protected static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape SALT = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 13.0D, 12.0D);
    public static final VoxelShape SHAPE_COMMON = Shapes.or(BASE, SALT);
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public SaltLampBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_COMMON;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, Boolean.valueOf(pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())));
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean flag = pState.getValue(LIT);
            if (flag != pLevel.hasNeighborSignal(pPos)) {
                if (flag) {
                    pLevel.scheduleTick(pPos, this, 4);
                } else {
                    pLevel.setBlock(pPos, pState.cycle(LIT), 2);
                }
            }

        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT) && !pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        }

    }
    public void animateTick(BlockState p_222503_, Level p_222504_, BlockPos p_222505_, RandomSource p_222506_) {
        if (p_222503_.getValue(LIT)) {
            int i = p_222505_.getX();
            int j = p_222505_.getY();
            int k = p_222505_.getZ();
            double d0 = (double) i + p_222506_.nextDouble();
            double d1 = (double) j + 0.7D;
            double d2 = (double) k + p_222506_.nextDouble();
            p_222504_.addParticle(ParticleTypes.ELECTRIC_SPARK, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int l = 0; l < 14; ++l) {
                blockpos$mutableblockpos.set(i + Mth.nextInt(p_222506_, -10, 10), j - p_222506_.nextInt(10), k + Mth.nextInt(p_222506_, -10, 10));
                BlockState blockstate = p_222504_.getBlockState(blockpos$mutableblockpos);
                if (!blockstate.isCollisionShapeFullBlock(p_222504_, blockpos$mutableblockpos)) {
                    p_222504_.addParticle(ParticleTypes.ELECTRIC_SPARK, (double) blockpos$mutableblockpos.getX() + p_222506_.nextDouble(), (double) blockpos$mutableblockpos.getY() + p_222506_.nextDouble(), (double) blockpos$mutableblockpos.getZ() + p_222506_.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }
}
