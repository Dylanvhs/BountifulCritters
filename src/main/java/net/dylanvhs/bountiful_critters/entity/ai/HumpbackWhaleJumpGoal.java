package net.dylanvhs.bountiful_critters.entity.ai;


import net.dylanvhs.bountiful_critters.entity.custom.aquatic.HumpbackWhaleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class HumpbackWhaleJumpGoal extends JumpGoal {
    private static final int[] STEPS_TO_CHECK = new int[]{0, 1, 4, 5, 6, 7};
    private final HumpbackWhaleEntity liopleurodon;
    private final int interval;
    private boolean breached;

    public HumpbackWhaleJumpGoal(HumpbackWhaleEntity pLiopleurodon, int pInterval) {
        this.liopleurodon = pLiopleurodon;
        this.interval = reducedTickDelay(pInterval);
    }

    public boolean canUse() {
        if (this.liopleurodon.getRandom().nextInt(this.interval) != 0) {
            return false;
        } else {
            Direction direction = this.liopleurodon.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockpos = this.liopleurodon.blockPosition();

            for (int k : STEPS_TO_CHECK) {
                if (!this.waterIsClear(blockpos, i, j, k) || !this.surfaceIsClear(blockpos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean waterIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
        BlockPos blockpos = pPos.offset(pDx * pScale, 0, pDz * pScale);
        return this.liopleurodon.level().getFluidState(blockpos).is(FluidTags.WATER) && !this.liopleurodon.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean surfaceIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
        return this.liopleurodon.level().getBlockState(pPos.offset(pDx * pScale, 1, pDz * pScale)).isAir() && this.liopleurodon.level().getBlockState(pPos.offset(pDx * pScale, 2, pDz * pScale)).isAir();
    }

    public boolean canContinueToUse() {
        double d0 = this.liopleurodon.getDeltaMovement().y;
        return (!(d0 * d0 < (double) 0.1F) || this.liopleurodon.getXRot() == 0.0F || !(Math.abs(this.liopleurodon.getXRot()) < 10.0F) || !this.liopleurodon.isInWater()) && !this.liopleurodon.onGround();
    }

    public boolean isInterruptable() {
        return false;
    }

    public boolean isJumping() {
        return true;
    }

    public void start() {
        Direction direction = this.liopleurodon.getMotionDirection();
        this.liopleurodon.setDeltaMovement(this.liopleurodon.getDeltaMovement().add((double) direction.getStepX() * 0.6D, 1D, (double) direction.getStepZ() * 0.6D));
        this.liopleurodon.getNavigation().stop();
        this.liopleurodon.setSprinting(true);

    }

    public void stop() {
        this.liopleurodon.setXRot(0.0F);
        this.liopleurodon.setSprinting(false);
    }

    public void tick() {
        boolean flag = this.breached;
        if (!flag) {
            FluidState fluidstate = this.liopleurodon.level().getFluidState(this.liopleurodon.blockPosition());
            this.breached = fluidstate.is(FluidTags.WATER);
        }

        if (this.breached && !flag) {
            this.liopleurodon.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3 vec3 = this.liopleurodon.getDeltaMovement();
        if (vec3.y * vec3.y < (double) 0.1F && this.liopleurodon.getXRot() != 0.0F) {
            this.liopleurodon.setXRot(Mth.rotLerp(0.2F, this.liopleurodon.getXRot(), 0.0F));
        } else if (vec3.length() > (double) 1.0E-5F) {
            double d0 = vec3.horizontalDistance();
            double d1 = Math.atan2(-vec3.y, d0) * (double) (180F / (float) Math.PI);
            this.liopleurodon.setXRot((float) d1);
        }

    }
}

