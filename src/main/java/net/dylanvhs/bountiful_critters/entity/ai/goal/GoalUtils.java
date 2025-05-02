package net.dylanvhs.bountiful_critters.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
public class GoalUtils {

    @Nullable
    public static Vec3 getRandomSwimmablePosWithSeabed(PathfinderMob pPathfinder, int pRadius, int pVerticalDistance) {
        Vec3 testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        int MaxSearchAmount = pRadius*pRadius*pRadius;

        for (int x = 0; testPos != null && x < MaxSearchAmount; testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance), x ++) {
            if (testPos != null) {
                Vec3 belowPos = testPos.subtract(0, 1, 0);

                if (pPathfinder.level().getBlockState(BlockPos.containing(belowPos)).entityCanStandOn(pPathfinder.level(), BlockPos.containing(testPos), pPathfinder) && pPathfinder.level().getBlockState(BlockPos.containing(testPos)).isPathfindable(pPathfinder.level(), BlockPos.containing(testPos), PathComputationType.WATER)) {
                    return testPos;
                }

                if (x == MaxSearchAmount - 1) {
                    return testPos;
                    //basically this keeps the shark moving if it can't find somewhere to go. it's flawed because there's nothing to prevent double checks but you probably can't get the shark stuck in open waters unless you're intentionally doing it
                    //might be useful for drowned farms or smthing
                }
            }
        }

        return null;
    }

    @Nullable
    public static Vec3 getRandomSwimmablePosThatIsntTheSameDepth(PathfinderMob pPathfinder, int pRadius, int pVerticalDistance) {
        Vec3 testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        int MaxSearchAmount = pRadius*pRadius*pRadius;

        for (int x = 0; testPos != null && x < MaxSearchAmount; testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance), x ++) {

            if (testPos != null) {

                if (Math.abs(Math.abs(testPos.y) - Math.abs(pPathfinder.position().y())) > 1) {
                    return testPos;
                }

                if (x == MaxSearchAmount - 1) {
                    return testPos;
                    //basically this keeps the entity moving if it can't find somewhere to go
                    //might be useful for drowned farms or smthing
                }
            }
        }

        return null;
    }
}

