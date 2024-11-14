package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.FleeSkyGoal;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class HogbearEntity extends Animal implements Enemy, GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(HogbearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(HogbearEntity.class, EntityDataSerializers.BYTE);

    private static final int FLAG_SLEEPING = 32;

    public HogbearEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new HogbearEntity.HogbearLookControl();
        this.moveControl = new HogbearEntity.HogbearMoveControl();
    }

    public static AttributeSupplier setAttributes() {
        return LionEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .build();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new HogbearEntity.HogbearFloatGoal());
        this.targetSelector.addGoal(4, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.25D, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SkeletonHorse.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Hoglin.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Piglin.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(7, new HogbearEntity.SleepGoal());
        this.goalSelector.addGoal(12, new HogbearLookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(6, new HogbearEntity.SeekShelterGoal(1.25D));
    }
    protected boolean onSoulSandBlock() {
        return this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(Blocks.SOUL_SAND);
    }
    public void aiStep() {
        if (this.isAlive()) {
            setSprinting(isAggressive());
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.onSoulSandBlock() ? 0.3 : 0.2);
        }

        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }

        super.aiStep();
    }

    protected boolean isImmobile() {
        return this.isDeadOrDying();
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Sleeping", this.isSleeping());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSleeping(pCompound.getBoolean("Sleeping"));
    }

    public boolean isSleeping() {
        return this.getFlag(32);
    }

    void setSleeping(boolean pSleeping) {
        this.setFlag(32, pSleeping);
    }

    private void setFlag(int pFlagId, boolean pValue) {
        if (pValue) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | pFlagId));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~pFlagId));
        }

    }

    private boolean getFlag(int pFlagId) {
        return (this.entityData.get(DATA_FLAGS_ID) & pFlagId) != 0;
    }

    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            boolean flag = this.isInWater();
            if (flag || this.getTarget() != null || this.level().isThundering()) {
                this.wakeUp();
            }
        }
    }

    void wakeUp() {
        this.setSleeping(false);
    }

    void clearStates() {
        this.setSleeping(false);
    }

    boolean canMove() {
        return !this.isSleeping();
    }

    public static class HogbearAlertableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(LivingEntity pEntity) {
            if (pEntity instanceof HogbearEntity) {
                return false;
            } else if (!(pEntity instanceof Monster)) {
               if (!(pEntity instanceof Player) || !pEntity.isSpectator() && !((Player) pEntity).isCreative()) {
                    return !pEntity.isSleeping();
               } else {
                   return false;
              }
            }  else {
                return true;
            }
        }
    }

    class HogbearFloatGoal extends FloatGoal {
        public HogbearFloatGoal() {
            super(HogbearEntity.this);
        }

        public void start() {
            super.start();
            HogbearEntity.this.clearStates();
        }

        public boolean canUse() {
            return HogbearEntity.this.isInWater() && HogbearEntity.this.getFluidHeight(FluidTags.WATER) > 0.25D || HogbearEntity.this.isInLava() || HogbearEntity.this.isInFluidType((fluidType, height) -> HogbearEntity.this.canSwimInFluidType(fluidType) && height > 0.25D);
        }
    }

    abstract class HogbearBehaviorGoal extends Goal {
        private final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(12.0D).ignoreLineOfSight().selector(new HogbearAlertableEntitiesSelector());
        protected boolean hasShelter() {
            BlockPos blockpos = BlockPos.containing(HogbearEntity.this.getX(), HogbearEntity.this.getBoundingBox().maxY, HogbearEntity.this.getZ());
            return !HogbearEntity.this.level().canSeeSky(blockpos)  && HogbearEntity.this.getWalkTargetValue(blockpos) >= 0.0F;
        }
        protected boolean alertable() {
            return !HogbearEntity.this.level().getNearbyEntities(LivingEntity.class, this.alertableTargeting, HogbearEntity.this, HogbearEntity.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty();
        }
    }

    static class HogbearLookAtPlayerGoal extends LookAtPlayerGoal {
        public HogbearLookAtPlayerGoal(Mob pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance) {
            super(pMob, pLookAtType, pLookDistance);
        }

        public boolean canUse() {
            return super.canUse();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
    }

    class SeekShelterGoal extends FleeSkyGoal {
        private int interval = reducedTickDelay(100);

        public SeekShelterGoal(double pSpeedModifier) {
            super(HogbearEntity.this, pSpeedModifier);
        }
        public boolean canUse() {
            if (!HogbearEntity.this.isSleeping() && this.mob.getTarget() == null) {
                if (HogbearEntity.this.level().canSeeSky(this.mob.blockPosition())) {
                    return this.setWantedPos();
                }
                if (this.interval > 0) {
                    --this.interval;
                    return false;
                } else {
                    this.interval = 100;
                    BlockPos blockpos = this.mob.blockPosition();
                    return HogbearEntity.this.level().canSeeSky(blockpos) && this.setWantedPos();
                }
            } else {
                return false;
            }
        }
        public void start() {
            HogbearEntity.this.clearStates();
            super.start();
        }
    }

    public class HogbearLookControl extends LookControl {
        public HogbearLookControl() {
            super(HogbearEntity.this);
        }

        public void tick() {
            if (!HogbearEntity.this.isSleeping()) {
                super.tick();
            }
        }
    }

    class HogbearMoveControl extends MoveControl {
        public HogbearMoveControl() {
            super(HogbearEntity.this);
        }

        public void tick() {
            if (HogbearEntity.this.canMove()) {
                super.tick();
            }

        }
    }

    class SleepGoal extends HogbearEntity.HogbearBehaviorGoal {
        private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
        private int countdown = HogbearEntity.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);

        public SleepGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            if (HogbearEntity.this.xxa == 0.0F && HogbearEntity.this.yya == 0.0F && HogbearEntity.this.zza == 0.0F) {
                return this.canSleep() || HogbearEntity.this.isSleeping();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return this.hasShelter() && !this.alertable() && !HogbearEntity.this.isInPowderSnow;
            }
        }

        public void stop() {
            this.countdown = HogbearEntity.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            HogbearEntity.this.clearStates();
        }
        public void start() {
            HogbearEntity.this.setSleeping(true);
            HogbearEntity.this.getNavigation().stop();
            HogbearEntity.this.getMoveControl().setWantedPosition(HogbearEntity.this.getX(), HogbearEntity.this.getY(), HogbearEntity.this.getZ(), 0.0D);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.HOGBEAR_SPAWN_EGG.get());
    }

    @Nullable
    public HogbearEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.HOGBEAR.get().create(pLevel);
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(Items.ROTTEN_FLESH);
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? pSize.height * 0.95F : 1.7F;
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.SOUL_SAND) || blockstate.is(Blocks.SOUL_SOIL);
    }
    @Override
    public boolean canAttack(LivingEntity entity) {
        boolean prev = super.canAttack(entity);
        if (isBaby()) {
            return false;
        }
        return prev;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving() && !this.isSprinting()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hogbear.walk", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
            return PlayState.CONTINUE;
        } else if (geoAnimatableAnimationState.isMoving() && this.isSprinting()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hogbear.sprint", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.5F);
            return PlayState.CONTINUE;
        }
        if (this.isSleeping()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hogbear.sleep", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
            return PlayState.CONTINUE;
        }
        else geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hogbear.idle", Animation.LoopType.LOOP));
        geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.swinging && geoAnimatableAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            geoAnimatableAnimationState.getController().forceAnimationReset();
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hogbear.attack", Animation.LoopType.PLAY_ONCE));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.5F);
            this.swinging = false;
        }  return PlayState.CONTINUE;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return tickCount;
    }

}
