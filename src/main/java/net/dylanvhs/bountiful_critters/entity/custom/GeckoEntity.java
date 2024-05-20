package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.Bagable;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GeckoEntity extends Animal implements GeoEntity, Bagable {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FROM_BAG = SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_WARNING = SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.INT);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(ModItems.RAW_PILLBUG.get());
    public GeckoEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.GECKO_SPAWN_EGG.get());
    }

    @Nullable
    public GeckoEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        GeckoEntity gecko = ModEntities.GECKO.get().create(pLevel);
        if (gecko != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((GeckoEntity) pOtherParent).getVariant();
            gecko.setVariant(i);
            gecko.setPersistenceRequired();
        }
        return gecko;
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "green";
            case 2 -> "tokay";
            case 3 -> "electric_blue_day";
            default -> "leopard";
        };
    }

    @Override
    public boolean fromBag() {
        return this.entityData.get(FROM_BAG);
    }

    @Override
    public void setFromBag(boolean p_203706_1_) {
        this.entityData.set(FROM_BAG, p_203706_1_);
    }

    @Override
    @Nonnull
    public ItemStack getBagItemStack() {
        ItemStack stack = new ItemStack(ModItems.BAGGED_GECKO.get());
        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
        return stack;
    }

    @Override
    public void saveToBagTag(@Nonnull ItemStack bucket) {
        if (this.hasCustomName()) {
            bucket.setHoverName(this.getCustomName());
        }
        Bagable.saveDefaultDataToBagTag(this, bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("Age", this.getAge());
        compoundnbt.putInt("BagVariantTag", this.getVariant());
    }

    @Override
    public void loadFromBagTag(@Nonnull CompoundTag compound) {
        Bagable.loadDefaultDataFromBagTag(this, compound);
        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        }
        if (compound.contains("BagVariantTag", 3)) {
            this.setVariant(compound.getInt("BagVariantTag"));
        }
    }

    @Override
    @Nonnull
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return Bagable.bagMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    @Nonnull
    public SoundEvent getPickupSound() {
        return SoundEvents.BUNDLE_INSERT;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(FROM_BAG, false);
        this.entityData.define(IS_WARNING, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBag", this.fromBag());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFromBag(compound.getBoolean("FromBag"));
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
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.25F, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PillbugEntity.class, true));
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 1D)
                .build();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.GECKO_AMBIENT.get();
    }

    protected float getSoundVolume() {
        return 0.45F;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(BlockTags.LOGS) || blockstate.is(BlockTags.LEAVES);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isWarning();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
        }
    }

    public boolean isWarning() {
        return entityData.get(IS_WARNING);
    }

    public void setWarning(boolean rollUp) {
        entityData.set(IS_WARNING, rollUp);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

        if (this.isWarning() && this.random.nextFloat() < 0.01F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                playSound(ModSounds.GECKO_AMBIENT.get(),0.5F,1 );
            }
        }

        List<Monster> list = level().getNearbyEntities(Monster.class, TargetingConditions.DEFAULT, this, getBoundingBox().inflate(8.0D, 3.0D, 8.0D));
        if (!list.isEmpty()) {
            if (list.stream().noneMatch(Entity::isCrouching)) {
                setWarning(true);
                getNavigation().stop();
            }
            else {
                setWarning(false);
            }
        }
        else {
            setWarning(false);
        }
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
        boolean flag = false;
        float variantChange = this.getRandom().nextFloat();
        if (reason != MobSpawnType.BUCKET) {
            if (flag) {
                this.setAge(-24000);
            }
        }
        if(variantChange <= 0.25F){
            this.setVariant(3);
        } else if(variantChange <= 0.40F){
            this.setVariant(2);
        } else if(variantChange <= 0.55F){
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.isWarning()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.warn", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(2F);
            return PlayState.CONTINUE;
        }
        if (geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.walk", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.5F);
            return PlayState.CONTINUE;
        } else if (this.isClimbing() && geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.climb", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isClimbing() && !geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.climb_idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        else geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.swinging && geoAnimatableAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            geoAnimatableAnimationState.getController().forceAnimationReset();
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.gecko.lick", Animation.LoopType.PLAY_ONCE));
            geoAnimatableAnimationState.getController().setAnimationSpeed(2.5F);
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
