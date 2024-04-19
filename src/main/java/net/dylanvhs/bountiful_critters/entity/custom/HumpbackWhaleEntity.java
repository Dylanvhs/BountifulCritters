package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.HumpbackWhaleJumpGoal;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;

public class HumpbackWhaleEntity extends Animal implements GeoAnimatable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(ModItems.RAW_KRILL.get());
    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(HumpbackWhaleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_FEEDING = SynchedEntityData.defineId(HumpbackWhaleEntity.class, EntityDataSerializers.BOOLEAN);
    public static final int TOTAL_AIR_SUPPLY = 4800;
    private static final int TOTAL_MOISTNESS_LEVEL = 2400;

    public HumpbackWhaleEntity(EntityType<? extends HumpbackWhaleEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 15);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 85D)
                .add(Attributes.FOLLOW_RANGE, 75D)
                .add(Attributes.MOVEMENT_SPEED, 1.5D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 5f)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new HumpbackWhaleJumpGoal(this, 1));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, KrillEntity.class, true));
    }
    @Nullable
    public HumpbackWhaleEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.HUMPBACK_WHALE.get().create(pLevel);
    }

    public static boolean checkFishSpawnRules(EntityType<? extends HumpbackWhaleEntity> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223363_3_, RandomSource randomIn) {
        return worldIn.getBlockState(p_223363_3_).is(Blocks.WATER) && worldIn.getBlockState(p_223363_3_.above()).is(Blocks.WATER);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this);
    }


    protected PathNavigation createNavigation(Level p_27480_) {
        return new WaterBoundPathNavigation(this, p_27480_);
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public int getMaxAirSupply() {
        return 4800;
    }

    protected int increaseAirSupply(int pCurrentAir) {
        return this.getMaxAirSupply();
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.75F;
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    public int getMoistnessLevel() {
        return this.entityData.get(MOISTNESS_LEVEL);
    }

    public void setMoisntessLevel(int pMoistnessLevel) {
        this.entityData.set(MOISTNESS_LEVEL, pMoistnessLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOISTNESS_LEVEL, 2400);
        this.entityData.define(IS_FEEDING, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Moistness", this.getMoistnessLevel());
    }


    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setMoisntessLevel(pCompound.getInt("Moistness"));
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    public boolean isFeeding() {
        return entityData.get(IS_FEEDING);
    }

    public void setFeeding(boolean feeding) {
        entityData.set(IS_FEEDING, feeding);
    }

    public void tick() {
        super.tick();
        List<KrillEntity> list = level().getNearbyEntities(KrillEntity.class, TargetingConditions.DEFAULT, this, getBoundingBox().inflate(5.0D, 2.0D, 5.0D));

        if (!list.isEmpty()) {
            setFeeding(true);
        } else {
            setFeeding(false);
        }

        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.isInWaterRainOrBubble()) {
                this.setMoisntessLevel(2400);
            } else {
                this.setMoisntessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.hurt(this.damageSources().dryOut(), 1.0F);
                }

                if (this.onGround()) {
                    this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.setOnGround(false);
                    this.hasImpulse = true;
                }
            }

            if (this.level().isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
                }
            }

        }
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() == Items.SAND && this.isAlive() && !isBaby()) {
            playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
            heldItem.shrink(1);
            float lootChange = this.getRandom().nextFloat();
            if(lootChange <= 0.94F){
                spawnAtLocation(Items.DIAMOND);
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.HEART_OF_THE_SEA, 3);
                }
                if(lootChange <= 0.45){
                    spawnAtLocation(Items.DIAMOND, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.EMERALD, 3);
                }
            } else if(lootChange <= 0.95F){
                spawnAtLocation(Items.EMERALD, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.LAPIS_BLOCK, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.96F){
                spawnAtLocation(Items.IRON_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.GOLD_BLOCK, 3);
                }
            } else if(lootChange <= 0.97F){
                spawnAtLocation(Items.GOLD_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.IRON_NUGGET, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.98F){
                spawnAtLocation(Items.GOLD_NUGGET, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COPPER_INGOT, 3);
                }

            } else if(lootChange <= 0.99F){
                spawnAtLocation(Items.BONE_MEAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COD, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }

            } else if(lootChange <= 1F){
                spawnAtLocation(Items.COAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else{
                spawnAtLocation(Items.SAND, 3);

                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.BONE_MEAL, 3);
                }
            }
            return InteractionResult.SUCCESS;
        } else if
        (heldItem.getItem() == Items.RED_SAND && this.isAlive() && !isBaby()) {
            playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
            heldItem.shrink(1);
            float lootChange = this.getRandom().nextFloat();
            if(lootChange <= 0.5F){
                spawnAtLocation(Items.DIAMOND);
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.HEART_OF_THE_SEA, 3);
                }
                if(lootChange <= 0.45){
                    spawnAtLocation(Items.DIAMOND, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.EMERALD, 3);
                }
            } else if(lootChange <= 0.95F){
                spawnAtLocation(Items.EMERALD, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.LAPIS_BLOCK, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.96F){
                spawnAtLocation(Items.IRON_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.GOLD_BLOCK, 3);
                }
            } else if(lootChange <= 0.97F){
                spawnAtLocation(Items.GOLD_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.IRON_NUGGET, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.98F){
                spawnAtLocation(Items.GOLD_NUGGET, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COPPER_INGOT, 3);
                }

            } else if(lootChange <= 0.99F){
                spawnAtLocation(Items.BONE_MEAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COD, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }

            } else if(lootChange <= 1F){
                spawnAtLocation(Items.COAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else{
                spawnAtLocation(Items.RED_SAND, 3);

                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.BONE_MEAL, 3);
                }
            }
            return InteractionResult.SUCCESS;
        } else if
        (heldItem.getItem() == Items.GRAVEL && this.isAlive() && !isBaby()) {
            playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
            heldItem.shrink(1);
            float lootChange = this.getRandom().nextFloat();
            if(lootChange <= 0.94F){
                spawnAtLocation(Items.DIAMOND);
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.HEART_OF_THE_SEA, 3);
                }
                if(lootChange <= 0.45){
                    spawnAtLocation(Items.DIAMOND, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
                if(lootChange <= 0.45F){
                    spawnAtLocation(Items.EMERALD, 3);
                }
            } else if(lootChange <= 0.95F){
                spawnAtLocation(Items.EMERALD, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.LAPIS_BLOCK, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.96F){
                spawnAtLocation(Items.IRON_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.GOLD_BLOCK, 3);
                }
            } else if(lootChange <= 0.97F){
                spawnAtLocation(Items.GOLD_INGOT, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.IRON_NUGGET, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else if(lootChange <= 0.98F){
                spawnAtLocation(Items.GOLD_NUGGET, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COPPER_INGOT, 3);
                }

            } else if(lootChange <= 0.99F){
                spawnAtLocation(Items.BONE_MEAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COD, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }

            } else if(lootChange <= 1F){
                spawnAtLocation(Items.COAL, 3);
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.SLIME_BALL, 3);
                }
            } else{
                spawnAtLocation(Items.GRAVEL, 3);

                if(lootChange <= 0.5){
                    spawnAtLocation(Items.COAL, 3);
                }
                if(lootChange <= 0.5){
                    spawnAtLocation(Items.BONE_MEAL, 3);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.DOLPHIN_SPLASH;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    static class MoveHelperController extends MoveControl {
        private final HumpbackWhaleEntity dolphin;

        public MoveHelperController(HumpbackWhaleEntity dolphinIn) {
            super(dolphinIn);
            this.dolphin = dolphinIn;
        }

        public void tick() {
            if (this.dolphin.isInWater()) {
                this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.dolphin.getNavigation().isDone()) {
                double d0 = this.wantedX - this.dolphin.getX();
                double d1 = this.wantedY - this.dolphin.getY();
                double d2 = this.wantedZ - this.dolphin.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    this.dolphin.setYRot(this.rotlerp(this.dolphin.getYRot(), f, 10.0F));
                    this.dolphin.yBodyRot = this.dolphin.getYRot();
                    this.dolphin.yHeadRot = this.dolphin.getYRot();
                    float f1 = (float) (this.speedModifier * this.dolphin.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (this.dolphin.isInWater()) {
                        this.dolphin.setSpeed(f1 * 0.02F);
                        float f2 = -((float) (Mth.atan2(d1, Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (double) (180F / (float) Math.PI)));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.setXRot(this.rotlerp(this.dolphin.getXRot(), f2, 5.0F));
                        float f3 = Mth.cos(this.dolphin.getXRot() * ((float) Math.PI / 180F));
                        float f4 = Mth.sin(this.dolphin.getXRot() * ((float) Math.PI / 180F));
                        this.dolphin.zza = f3 * f1;
                        this.dolphin.yya = -f4 * f1;
                    } else {
                        this.dolphin.setSpeed(f1 * 0.1F);
                    }

                }
            } else {
                this.dolphin.setSpeed(0.0F);
                this.dolphin.setXxa(0.0F);
                this.dolphin.setYya(0.0F);
                this.dolphin.setZza(0.0F);
            }
        }
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
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving()) {
            if (this.isSprinting()) {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.humpback_whale.jump", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            } else if (this.isFeeding()) {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.humpback_whale.eat", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            } else {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.humpback_whale.swim", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        } else
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.humpback_whale.swim", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return tickCount;
    }
}
