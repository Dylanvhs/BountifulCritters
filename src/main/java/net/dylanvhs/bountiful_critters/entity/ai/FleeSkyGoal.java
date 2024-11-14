package net.dylanvhs.bountiful_critters.entity.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FleeSkyGoal extends Goal {
    protected final PathfinderMob mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final Level level;

    public FleeSkyGoal(PathfinderMob pMob, double pSpeedModifier) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.level = pMob.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            return false;
        } else if (!this.mob.isOnFire()) {
            return false;
        } else if (!this.level.canSeeSky(this.mob.blockPosition())) {
            return false;
        } else {
            return !this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ? false : this.setWantedPos();
        }
    }

    protected boolean setWantedPos() {
        Vec3 vec3 = this.getHidePos();
        if (vec3 == null) {
            return false;
        } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            return true;
        }
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Nullable
    protected Vec3 getHidePos() {
        RandomSource randomsource = this.mob.getRandom();
        BlockPos blockpos = this.mob.blockPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(20) - 10, randomsource.nextInt(6) - 3, randomsource.nextInt(20) - 10);
            if (!this.level.canSeeSky(blockpos1) && this.mob.getWalkTargetValue(blockpos1) < 0.0F) {
                return Vec3.atBottomCenterOf(blockpos1);
            }
        }

        return null;
    }
}
