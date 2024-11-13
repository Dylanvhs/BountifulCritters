package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
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
import java.util.UUID;

public class HogbearEntity extends Animal implements Enemy, GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public HogbearEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Animal.class, true) {
            @Override
            public boolean canUse() {
                return !isAlliedTo(mob) && super.canUse();
            }
        });
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

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            setSprinting(isAggressive());
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
