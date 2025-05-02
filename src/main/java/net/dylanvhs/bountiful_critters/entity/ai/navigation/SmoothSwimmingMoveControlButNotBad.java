package net.dylanvhs.bountiful_critters.entity.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SmoothSwimmingMoveControlButNotBad extends MoveControl{

    private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;
    private static final float STOP_TURN_THRESHOLD = 60.0F;
    private final int maxTurnX;
    private final int maxTurnY;
    private final float inWaterSpeedModifier;
    private final float outsideWaterSpeedModifier;
    private final boolean applyGravity;

    public SmoothSwimmingMoveControlButNotBad(Mob pMob, int pMaxTurnX, int pMaxTurnY, float pInWaterSpeedModifier, float pOutsideWaterSpeedModifier, boolean pApplyGravity) {
        super(pMob);
        this.maxTurnX = pMaxTurnX;
        this.maxTurnY = pMaxTurnY;
        this.inWaterSpeedModifier = pInWaterSpeedModifier;
        this.outsideWaterSpeedModifier = pOutsideWaterSpeedModifier;
        this.applyGravity = pApplyGravity;
    }

    public void tick() {

        if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double $$0 = this.wantedX - this.mob.getX();
            double $$1 = this.wantedY - this.mob.getY();
            double $$2 = this.wantedZ - this.mob.getZ();
            double $$3 = $$0 * $$0 + $$1 * $$1 + $$2 * $$2;
            if ($$3 < 2.500000277905201E-7) {
                this.mob.setZza(0.0F);
            } else {
                float $$4 = (float)(Mth.atan2($$2, $$0) * 57.2957763671875) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), $$4, (float)this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float $$5 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed($$5 * this.inWaterSpeedModifier);
                    double $$6 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                    float $$8;
                    if (Math.abs($$1) > 9.999999747378752E-6 || Math.abs($$6) > 9.999999747378752E-6) {
                        $$8 = -((float)(Mth.atan2($$1, $$6) * 57.2957763671875));
                        $$8 = Mth.clamp(Mth.wrapDegrees($$8), (float)(-this.maxTurnX), (float)this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), $$8, 5.0F));
                    }

                    $$8 = Mth.cos(this.mob.getXRot() * 0.017453292F);
                    float $$9 = Mth.sin(this.mob.getXRot() * 0.017453292F);
                    this.mob.zza = $$8 * $$5;
                    this.mob.yya = -$$9 * $$5;
                } else {
                    float $$10 = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - $$4));
                    float $$11 = getTurningSpeedFactor($$10);
                    this.mob.setSpeed($$5 * this.outsideWaterSpeedModifier * $$11);
                }

            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    private static float getTurningSpeedFactor(float p_249853_) {
        return 1.0F - Mth.clamp((p_249853_ - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}
