package net.dylanvhs.bountiful_critters.entity.custom.flying;

import com.google.common.collect.Lists;
import net.dylanvhs.bountiful_critters.block.ModBlockEntities;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.block.entity.OakNestEntity;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmartBodyHelper;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HoopoeEntity extends Animal implements GeoEntity, FlyingAnimal, NeutralMob {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Byte> HOOPOE_FLAGS = SynchedEntityData.defineId(HoopoeEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> ANGER = SynchedEntityData.defineId(HoopoeEntity.class, EntityDataSerializers.INT);
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID angryAt;
    private int cannotEnterNestTicks;
    int ticksLeftToFindNest;
    @Nullable
    BlockPos nestPos;
    HoopoeEntity.MoveToNestGoal moveToNestGoal;
    public int timeUntilNextEgg = this.random.nextInt(6000) + 6000;

    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(ModItems.RAW_PILLBUG.get());
    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public HoopoeEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    public static AttributeSupplier setAttributes() {
        return HoopoeEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, (double)0.7F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .build();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.3D, 1.3D));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new HoopoeEntity.HoopoeWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(0, new FindNestGoal());
        this.goalSelector.addGoal(1, new EnterNestGoal());
        this.moveToNestGoal = new MoveToNestGoal();
        this.goalSelector.addGoal(2, this.moveToNestGoal);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.HOOPOE_SPAWN_EGG.get());
    }

    @Nullable
    public HoopoeEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.HOOPOE.get().create(pLevel);
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.5F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    static class HoopoeWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public HoopoeWanderGoal(PathfinderMob p_186224_, double p_186225_) {
            super(p_186224_, p_186225_);
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState blockstate = this.mob.level().getBlockState(blockpos$mutableblockpos1.setWithOffset(blockpos1, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().isEmptyBlock(blockpos$mutableblockpos.setWithOffset(blockpos1, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockpos1);
                    }
                }
            }

            return null;
        }
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }
    protected float getSoundVolume() {
        return 0.5F;
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.HOOPOE_EGG.get());
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.cannotEnterNestTicks > 0) {
            --this.cannotEnterNestTicks;
        }

        if (this.ticksLeftToFindNest > 0) {
            --this.ticksLeftToFindNest;
        }
        boolean bl = this.isAngry() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0;
        this.setNearTarget(bl);
        if (this.age % 20 == 0 && !this.isNestValid()) {
            this.nestPos = null;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOOPOE_FLAGS, (byte)0);
        this.entityData.define(ANGER, 0);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("CannotEnterDwellingTicks", this.cannotEnterNestTicks);
        if (this.hasNest()) {
            assert this.getNestPos() != null;
            compoundTag.put("DwellingPos", NbtUtils.writeBlockPos(this.getNestPos()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.cannotEnterNestTicks = compoundTag.getInt("CannotEnterDwellingTicks");
        if (compoundTag.contains("DwellingPos")) {
            this.nestPos = NbtUtils.readBlockPos(compoundTag.getCompound("NestPos"));
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER);
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(ANGER, angerTime);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }
    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }
    void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int)vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, 0.3141592741012573);
        if (vec3d2 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
        }
    }

    boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && this.getTarget() == null) return this.level().isDay();
        else return false;
    }

    public void setCannotEnterNestTicks(int cannotEnterNestTicks) {
        this.cannotEnterNestTicks = cannotEnterNestTicks;
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.level().getBlockEntity(pos);
        if (blockEntity instanceof OakNestEntity) {
            return !((OakNestEntity)blockEntity).isFullOfHoopoes();
        } else {
            return false;
        }
    }

    boolean isNestValid() {
        if (!this.hasNest()) {
            return false;
        } else {
            BlockEntity blockEntity = this.level().getBlockEntity(this.nestPos);
            return blockEntity != null && blockEntity.getType() == ModBlockEntities.OAK_NEST.get();
        }
    }

    private void setNearTarget(boolean nearTarget) {
        this.setHoopoeFlag(2, nearTarget);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    private void setHoopoeFlag(int bit, boolean value) {
        if (value) {
            this.entityData.set(HOOPOE_FLAGS, (byte)(this.entityData.get(HOOPOE_FLAGS) | bit));
        } else {
            this.entityData.set(HOOPOE_FLAGS, (byte)(this.entityData.get(HOOPOE_FLAGS) & ~bit));
        }

    }

    @VisibleForDebug
    public boolean hasNest() {
        return this.nestPos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getNestPos() {
        return this.nestPos;
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    private abstract class NotAngryGoal extends Goal {
        NotAngryGoal() {
        }

        public abstract boolean canHoopoeStart();

        public abstract boolean canHoopoeContinue();

        @Override
        public boolean canUse() {
            return this.canHoopoeStart() && !HoopoeEntity.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canHoopoeContinue() && !HoopoeEntity.this.isAngry();
        }
    }

    private class FindNestGoal extends HoopoeEntity.NotAngryGoal {
        FindNestGoal() {
            super();
        }

        @Override
        public boolean canHoopoeStart() {
            return HoopoeEntity.this.ticksLeftToFindNest == 0 && !HoopoeEntity.this.hasNest() && HoopoeEntity.this.canEnterNest();
        }

        @Override
        public boolean canHoopoeContinue() {
            return false;
        }

        @Override
        public void start() {
            HoopoeEntity.this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeDwellings();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        HoopoeEntity.this.moveToNestGoal.clearPossibleDwellings();
                        HoopoeEntity.this.nestPos = list.get(0);
                        return;
                    }

                    blockPos = var2.next();
                } while(HoopoeEntity.this.moveToNestGoal.isPossibleNest(blockPos));

                HoopoeEntity.this.nestPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeDwellings() {
            BlockPos blockPos = HoopoeEntity.this.blockPosition();
            PoiManager pointOfInterestStorage = ((ServerLevel) HoopoeEntity.this.level()).getPoiManager();
            Stream<PoiRecord> stream = pointOfInterestStorage.getInRange((poiType) -> poiType.is(ModTags.HOOPOE_HOME), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(HoopoeEntity.this::doesNestHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    @VisibleForDebug
    public class MoveToNestGoal extends HoopoeEntity.NotAngryGoal {
        int ticks;
        final List<BlockPos> possibleNests;
        @Nullable
        private Path path;
        private int ticksUntilLost;

        MoveToNestGoal() {
            super();
            this.ticks = HoopoeEntity.this.level().random.nextInt(10);
            this.possibleNests = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canHoopoeStart() {
            return HoopoeEntity.this.nestPos != null && !HoopoeEntity.this.hasRestriction() && HoopoeEntity.this.canEnterNest() && !this.isCloseEnough(HoopoeEntity.this.nestPos) && HoopoeEntity.this.level().getBlockState(HoopoeEntity.this.nestPos).is(ModBlocks.OAK_NEST.get());
        }

        @Override
        public boolean canHoopoeContinue() {
            return this.canHoopoeStart();
        }

        @Override
        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            HoopoeEntity.this.navigation.stop();
            HoopoeEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (HoopoeEntity.this.nestPos != null) {
                ++this.ticks;
                if (this.ticks > this.adjustedTickDelay(600)) {
                    this.makeChosenDwellingPossibleDwelling();
                } else if (!HoopoeEntity.this.navigation.isInProgress()) {
                    if (!HoopoeEntity.this.isWithinDistance(HoopoeEntity.this.nestPos, 16)) {
                        if (HoopoeEntity.this.isTooFar(HoopoeEntity.this.nestPos)) {
                            this.setLost();
                        } else {
                            HoopoeEntity.this.startMovingTo(HoopoeEntity.this.nestPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(HoopoeEntity.this.nestPos);
                        if (!bl) {
                            this.makeChosenDwellingPossibleDwelling();
                        } else if (this.path != null && Objects.requireNonNull(HoopoeEntity.this.navigation.getPath()).sameAs(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = HoopoeEntity.this.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            HoopoeEntity.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            HoopoeEntity.this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
            return HoopoeEntity.this.navigation.getPath() != null && HoopoeEntity.this.navigation.getPath().canReach();
        }

        boolean isPossibleNest(BlockPos pos) {
            return this.possibleNests.contains(pos);
        }

        private void addPossibleDwelling(BlockPos pos) {
            this.possibleNests.add(pos);

            while(this.possibleNests.size() > 3) {
                this.possibleNests.remove(0);
            }

        }

        void clearPossibleDwellings() {
            this.possibleNests.clear();
        }

        private void makeChosenDwellingPossibleDwelling() {
            if (HoopoeEntity.this.nestPos != null) {
                this.addPossibleDwelling(HoopoeEntity.this.nestPos);
            }

            this.setLost();
        }

        private void setLost() {
            HoopoeEntity.this.nestPos = null;
            HoopoeEntity.this.ticksLeftToFindNest = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (HoopoeEntity.this.isWithinDistance(pos, 2)) {
                return true;
            } else {
                Path path = HoopoeEntity.this.navigation.getPath();
                return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
            }
        }
    }

    class EnterNestGoal extends HoopoeEntity.NotAngryGoal {
        EnterNestGoal() {
            super();
        }

        @Override
        public boolean canHoopoeStart() {
            if (HoopoeEntity.this.hasNest() && HoopoeEntity.this.canEnterNest()) {
                assert HoopoeEntity.this.nestPos != null;
                if (HoopoeEntity.this.nestPos.closerToCenterThan(HoopoeEntity.this.position(), 2.0)) {
                    BlockEntity blockEntity = HoopoeEntity.this.level().getBlockEntity(HoopoeEntity.this.nestPos);
                    if (blockEntity instanceof OakNestEntity blockEntity1) {
                        if (!blockEntity1.isFullOfHoopoes()) {
                            return true;
                        }

                        HoopoeEntity.this.nestPos = null;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean canHoopoeContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = HoopoeEntity.this.level().getBlockEntity(HoopoeEntity.this.nestPos);
            if (blockEntity instanceof OakNestEntity birtDwellingBlockEntity) {
                birtDwellingBlockEntity.tryEnterNest(HoopoeEntity.this);
            }

        }
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving() && !this.isFlying()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hoopoe.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isFlying()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hoopoe.fly", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (!this.isFlying())
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hoopoe.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}
