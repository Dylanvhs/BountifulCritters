package net.dylanvhs.bountiful_critters.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CustomBreathAirGoal extends Goal {
    private final PathfinderMob mob;

    public CustomBreathAirGoal(PathfinderMob pMob) {
        this.mob = pMob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    public boolean canUse() {
        return this.mob.getAirSupply() < 750;
    }


    public boolean canContinueToUse() {
        return this.canUse();
    }

    public boolean isInterruptable() {
        return false;
    }


    public void start() {
        this.findAirPosition();
    }

    private void findAirPosition() {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 1.0D), this.mob.getBlockY(), Mth.floor(this.mob.getZ() - 1.0D), Mth.floor(this.mob.getX() + 1.0D), Mth.floor(this.mob.getY() + 8.0D), Mth.floor(this.mob.getZ() + 1.0D));
        BlockPos blockpos = null;

        for(BlockPos blockpos1 : iterable) {
            if (this.givesAir(this.mob.level(), blockpos1)) {
                blockpos = blockpos1;
                break;
            }
        }

        if (blockpos == null) {
            blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 8.0D, this.mob.getZ());
        }

        this.mob.getNavigation().moveTo((double)blockpos.getX(), (double)(blockpos.getY() + 1), (double)blockpos.getZ(), 1.0D);
    }

    public void tick() {
        this.findAirPosition();
        this.mob.moveRelative(0.1F, new Vec3((double)this.mob.xxa, (double)this.mob.yya, (double)this.mob.zza));
        this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
    }

    private boolean givesAir(LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        return (pLevel.getFluidState(pPos).isEmpty() || blockstate.is(Blocks.BUBBLE_COLUMN)) && blockstate.isPathfindable(pLevel, pPos, PathComputationType.LAND);
    }
}
