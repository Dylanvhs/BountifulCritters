package net.dylanvhs.bountiful_critters.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CustomRandomSwimGoal extends RandomStrollGoal {

    PathfinderMob fims;
    Vec3 wantedPos;

    int radius;
    int height;
    int prox;

    public CustomRandomSwimGoal(PathfinderMob fi, double spdmultiplier, int interval, int radius, int height, int proximity) {
        super(fi, spdmultiplier, interval);
        this.fims = fi;
        this.radius = radius;
        this.height = height;
        this.prox = proximity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && fims.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        wantedPos = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
        return super.canContinueToUse() && fims.isInWater() && !(this.wantedPos.distanceTo(this.fims.position()) <= this.fims.getBbWidth() * prox);
        //second part cancels the goal if the animal gets close enough
    }

    public void tick() {
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Nullable
    protected Vec3 getPosition() {
        return GoalUtils.getRandomSwimmablePosThatIsntTheSameDepth(this.mob, radius, height);
    }
    //previously 32 and 12
}
