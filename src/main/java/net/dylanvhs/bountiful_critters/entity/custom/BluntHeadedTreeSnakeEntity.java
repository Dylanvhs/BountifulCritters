package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;

public class BluntHeadedTreeSnakeEntity extends Animal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
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

    @Override
    public void tick() {
        if (this.getBlockStateOn().is(Blocks.DECORATED_POT)) {
            PotAccess.setSnake(this.level(), this.getBlockPosBelowThatAffectsMyMovement(), this);
            this.remove(RemovalReason.UNLOADED_TO_CHUNK);
            BountifulCritters.LOGGER.info("moved snake to pot at " + this.getBlockPosBelowThatAffectsMyMovement().toShortString());
        }
        super.tick();
    }
}