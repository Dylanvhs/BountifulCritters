package net.dylanvhs.bountiful_critters.entity.custom;

import com.google.common.collect.Lists;
import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.dylanvhs.bountiful_critters.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BluntHeadedTreeSnakeEntity extends Animal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final int POT_SEARCH_DISTANCE = 20;
    public static final String TAG_CANNOT_ENTER_POT_TICKS = "CannotEnterPotTicks";
    public static final String TAG_POT_POS = "PotPos";
    private int stayOutOfPotCountdown;

    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
    int remainingCooldownBeforeLocatingNewPot;
    BlockPos potPos;
    BluntHeadedTreeSnakeEntity snake = BluntHeadedTreeSnakeEntity.this;
    BluntHeadedTreeSnakeEntity.SnakeGoToPotGoal goToPotGoal;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BluntHeadedTreeSnakeEntity.class, EntityDataSerializers.BYTE);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(ModItems.POTTED_PILLBUG.get());
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BluntHeadedTreeSnakeEntity.class, EntityDataSerializers.INT);

    public BluntHeadedTreeSnakeEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setMaxUpStep(1F);
    }

    @Nullable
    public BluntHeadedTreeSnakeEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        BluntHeadedTreeSnakeEntity snake = ModEntities.BLUNT_HEADED_TREE_SNAKE.get().create(pLevel);
        if (snake != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((BluntHeadedTreeSnakeEntity) pOtherParent).getVariant();
            snake.setVariant(i);
            snake.setPersistenceRequired();
        }
        return snake;
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "white_brown";
            case 2 -> "yellow";
            case 3 -> "melinda";
            default -> "brown";
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 5.0F, 1.0D, 1.25D));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.25F, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Frog.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PillbugEntity.class, true));
        this.goalSelector.addGoal(5, new BluntHeadedTreeSnakeEntity.SnakeLocatePotGoal());
        this.goToPotGoal = new BluntHeadedTreeSnakeEntity.SnakeGoToPotGoal();
        this.goalSelector.addGoal(5, this.goToPotGoal);
        this.goalSelector.addGoal(1, new BluntHeadedTreeSnakeEntity.SnakeEnterPotGoal());
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 1D)
                .build();
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    protected void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack) {
        if (!pPlayer.isCreative()) {
            if (pStack.is(ModItems.POTTED_PILLBUG.get())) {
                pPlayer.setItemInHand(pHand, new ItemStack(Items.FLOWER_POT));
            } else {
                super.usePlayerItem(pPlayer, pHand, pStack);
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity) {
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.POISON, 80 * (int)f), this);
        }

        return flag;
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(BlockTags.LOGS) || blockstate.is(BlockTags.LEAVES);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.SNAKE_HISS.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GENERIC_HURT;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.SNAKE_HISS.get();
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        float variantChange = this.getRandom().nextFloat();
        if(variantChange <= 0.1F){
            this.setVariant(3);
        } else if(variantChange <= 0.25F){
            this.setVariant(2);
        } else if(variantChange <= 0.50F){
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    boolean isTooFarAway(BlockPos pPos) {
        return !this.closerThan(pPos, 32);
    }

    boolean closerThan(BlockPos pPos, int pDistance) {
        return pPos.closerThan(this.blockPosition(), (double)pDistance);
    }

    void pathfindRandomlyTowards(BlockPos pPos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(pPos);
        int i = 0;
        BlockPos blockpos = this.blockPosition();
        int j = (int) vec3.y - blockpos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int i1 = blockpos.distManhattan(pPos);
        if (i1 < 15) {
            k = i1 / 2;
            l = i1 / 2;
        }

        Vec3 vec31 = AirRandomPos.getPosTowards(this, k, l, i, vec3, (double) ((float) Math.PI / 10F));
        if (vec31 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec31.x, vec31.y, vec31.z, 1.0D);
        }
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.stayOutOfPotCountdown > 0) {
                --this.stayOutOfPotCountdown;
            }

            if (this.remainingCooldownBeforeLocatingNewPot > 0) {
                --this.remainingCooldownBeforeLocatingNewPot;
            }

            if (this.tickCount % 20 == 0 && !this.isPotValid()) {
                this.potPos = null;
            }
        }

    }

    boolean isPotValid() {
        if (!this.hasPot()) {
            return false;
        } else if (this.isTooFarAway(this.potPos)) {
            return false;
        } else {
            BlockEntity blockentity = this.level().getBlockEntity(this.potPos);
            return blockentity instanceof DecoratedPotBlockEntity;
        }
    }

    @VisibleForDebug
    public boolean hasPot() {
        return this.potPos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getPotPos() {
        return this.potPos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    boolean wantsToEnterPot() {
        return this.stayOutOfPotCountdown <= 0 && this.getTarget() == null;
    }

    abstract static class BaseSnakeGoal extends Goal {
        public abstract boolean canBeeUse();

        public abstract boolean canBeeContinueToUse();

        public boolean canUse() {
            return this.canBeeUse();
        }

        public boolean canContinueToUse() {
            return this.canBeeContinueToUse();
        }
    }

    class SnakeEnterPotGoal extends BluntHeadedTreeSnakeEntity.BaseSnakeGoal {
        public boolean canBeeUse() {
            if (snake.hasPot() && snake.wantsToEnterPot() && snake.potPos.closerToCenterThan(snake.position(), 2.0D)) {
                return snake.getBlockStateOn().is(Blocks.DECORATED_POT);
            }
            return false;
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            if (snake.getBlockStateOn().is(Blocks.DECORATED_POT)) {
                PotAccess.setSnake(snake.getBlockPosBelowThatAffectsMyMovement(), snake);
                BountifulCritters.LOGGER.info("moved snake to pot at " + snake.getBlockPosBelowThatAffectsMyMovement().toShortString());
            }

        }
    }

    @VisibleForDebug
    public class SnakeGoToPotGoal extends BluntHeadedTreeSnakeEntity.BaseSnakeGoal {
        public static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks = snake.level().random.nextInt(10);
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        final List<BlockPos> blacklistedTargets = Lists.newArrayList();
        @Nullable
        private Path lastPath;
        private int ticksStuck;

        SnakeGoToPotGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canBeeUse() {
            return snake.potPos != null && !snake.hasRestriction() && snake.wantsToEnterPot() && !this.hasReachedTarget(snake.potPos) && snake.level().getBlockState(snake.potPos).is(Blocks.DECORATED_POT);
        }

        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            snake.navigation.stop();
            snake.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (snake.potPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    this.dropAndBlacklistHive();
                } else if (!snake.navigation.isInProgress()) {
                    if (!snake.closerThan(snake.potPos, 16)) {
                        if (snake.isTooFarAway(snake.potPos)) {
                            this.dropHive();
                        } else {
                            snake.pathfindRandomlyTowards(snake.potPos);
                        }
                    } else {
                        boolean flag = this.pathfindDirectlyTowards(snake.potPos);
                        if (!flag) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && snake.navigation.getPath().sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > 60) {
                                this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = snake.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean pathfindDirectlyTowards(BlockPos pPos) {
            snake.navigation.setMaxVisitedNodesMultiplier(10.0F);
            snake.navigation.moveTo((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), 1.0D);
            return snake.navigation.getPath() != null && snake.navigation.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos pPos) {
            return this.blacklistedTargets.contains(pPos);
        }

        private void blacklistTarget(BlockPos pPos) {
            this.blacklistedTargets.add(pPos);

            while(this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }

        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (snake.potPos != null) {
                this.blacklistTarget(snake.potPos);
            }

            this.dropHive();
        }

        private void dropHive() {
            snake.potPos = null;
            snake.remainingCooldownBeforeLocatingNewPot = 200;
        }

        private boolean hasReachedTarget(BlockPos pPos) {
            if (snake.closerThan(pPos, 2)) {
                return true;
            } else {
                Path path = snake.navigation.getPath();
                return path != null && path.getTarget().equals(pPos) && path.canReach() && path.isDone();
            }
        }
    }

    class SnakeLocatePotGoal extends BluntHeadedTreeSnakeEntity.BaseSnakeGoal {
        public boolean canBeeUse() {
            return snake.remainingCooldownBeforeLocatingNewPot == 0 && !snake.hasPot() && snake.wantsToEnterPot();
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            List<BlockPos> list = this.findNearbyHivesWithSpace();
            snake.remainingCooldownBeforeLocatingNewPot = 200;
            snake.goToPotGoal.clearBlacklist();
            snake.potPos = list.get(0);
        }

        private List<BlockPos> findNearbyHivesWithSpace() {
            BlockPos blockpos = snake.blockPosition();
            PoiManager poimanager = ((ServerLevel)snake.level()).getPoiManager();
            Stream<PoiRecord> stream = poimanager.getInRange((p_218130_) -> {
                return p_218130_.is(ModTags.SNAKE_POT);
            }, blockpos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).sorted(Comparator.comparingDouble((p_148811_) -> {
                return p_148811_.distSqr(blockpos);
            })).collect(Collectors.toList());
        }
    }




    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blunt_headed_tree_snake.walk", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.3F);
            return PlayState.CONTINUE;
        } else geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blunt_headed_tree_snake.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.swinging && geoAnimatableAnimationState.isMoving() && geoAnimatableAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            geoAnimatableAnimationState.getController().forceAnimationReset();
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blunt_headed_tree_snake.walk_attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        } else if (this.swinging && !geoAnimatableAnimationState.isMoving() && geoAnimatableAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
            geoAnimatableAnimationState.getController().forceAnimationReset();
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blunt_headed_tree_snake.idle_attack", Animation.LoopType.PLAY_ONCE));
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
