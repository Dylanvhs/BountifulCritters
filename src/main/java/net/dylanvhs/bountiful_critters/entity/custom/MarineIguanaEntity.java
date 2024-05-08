package net.dylanvhs.bountiful_critters.entity.custom;

import com.mojang.serialization.Codec;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.IntFunction;

public class MarineIguanaEntity  extends Animal implements GeoEntity, Bucketable {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(MarineIguanaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(StingrayEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SNEEZING = SynchedEntityData.defineId(MarineIguanaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.KELP);

    public int timeUntilNextSneeze = this.random.nextInt(3500) + 3500;

    public MarineIguanaEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        this.moveControl = new MarineIguanaEntity.IguanaMoveControl(this);
        this.setMaxUpStep(1.0F);
    }

    static class IguanaMoveControl extends SmoothSwimmingMoveControl {
        private final MarineIguanaEntity axolotl;

        public IguanaMoveControl(MarineIguanaEntity pAxolotl) {
            super(pAxolotl, 85, 10, 0.6F, 0.5F, false);
            this.axolotl = pAxolotl;
        }
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "neon";
            case 2 -> "warm";
            case 3 -> "red";
            case 4 -> "ash";
            default -> "stony";
        };
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D, 15));
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .build();
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRAVEL) || blockstate.is(Blocks.STONE);
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }


        @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(IS_SNEEZING, false);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    @Nonnull
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(ModItems.MARINE_IGUANA_BUCKET.get());
        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
        return stack;
    }

    @Override
    @Nonnull
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.hasCustomName()) {
            bucket.setHoverName(this.getCustomName());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("Age", this.getAge());
        compoundnbt.putInt("BucketVariantTag", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        }
        if (compound.contains("BucketVariantTag", 3)) {
            this.setVariant(compound.getInt("BucketVariantTag"));
        }
    }

    @Override
    @Nonnull
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        Bucketable.bucketMobPickup(player, hand, this);
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == Items.SEAGRASS && this.isAlive() && !isBaby()) {
            playSound(SoundEvents.FROG_EAT, 1.0F, 1.0F);
            heldItem.shrink(1);
            spawnAtLocation(ModItems.SEAGRASS_BALL.get());
            return InteractionResult.SUCCESS;

        }  else {
            return super.mobInteract(player, hand);
        }
    }


    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean p_203706_1_) {
        this.entityData.set(FROM_BUCKET, p_203706_1_);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        float variantChange = this.getRandom().nextFloat();
        if(variantChange <= 0.009F){
            this.setVariant(1);
        } else if(variantChange <= 0.49F){
            this.setVariant(2);
        } else if(variantChange <= 0.499F){
            this.setVariant(3);
        } else if(variantChange <= 0.50F){
            this.setVariant(4);
        } else{
            this.setVariant(0);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.AXOLOTL_HURT;
    }

    @javax.annotation.Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.AXOLOTL_DEATH;
    }

    @javax.annotation.Nullable
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.AXOLOTL_SPLASH;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        MarineIguanaEntity iguana = ModEntities.MARINE_IGUANA.get().create(pLevel);
        if (iguana != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((MarineIguanaEntity) pOtherParent).getVariant();
            iguana.setVariant(i);
            iguana.setPersistenceRequired();
        }
        return iguana;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isSneezing();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
        }
    }

    public boolean isSneezing() {
        return entityData.get(IS_SNEEZING);
    }

    public void setSneezing(boolean sneezing) {
        entityData.set(IS_SNEEZING, sneezing);
    }


    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && this.onGround() && --this.timeUntilNextSneeze <= 0) {
            this.playSound(SoundEvents.AXOLOTL_HURT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.SALT.get());
            this.timeUntilNextSneeze = this.random.nextInt(3500) + 3500;
            setSneezing(true);
            double d0 = 0;
            double d1 = Math.max(0.0D, 1.0D - d0);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)0.4F * d1, 0.0D));
        } else if (this.timeUntilNextSneeze > 0) {
            setSneezing(false);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {

        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && !this.isInWater()) {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.marine_iguana.walk", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
        }
        if (this.isSneezing()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.marine_iguana.sneeze", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if (this.isInWater() && !this.onGround()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.marine_iguana.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.marine_iguana.idle", Animation.LoopType.LOOP));
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
