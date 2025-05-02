package net.dylanvhs.bountiful_critters.entity.custom.land;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmartBodyHelper;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class LonghornEntity extends TamableAnimal implements NeutralMob, GeoAnimatable {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.GRASS);

    private static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(LonghornEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(LonghornEntity.class, EntityDataSerializers.BOOLEAN);
    private int stunnedTick;
    private boolean canBePushed = true;
    private UUID persistentAngerTarget;

    private static final float START_HEALTH = 25.0F;
    private static final float TAME_HEALTH = 35.0F;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(LonghornEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CHARGE_COOLDOWN_TICKS = SynchedEntityData.defineId(LonghornEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(LonghornEntity.class, EntityDataSerializers.BOOLEAN);
    public LonghornEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public static AttributeSupplier setAttributes() {
        return LonghornEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D)
                .build();
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.LONGHORN_SPAWN_EGG.get());
    }

    @Nullable
    public LonghornEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        LonghornEntity longhorn = ModEntities.LONGHORN.get().create(pLevel);
        if (longhorn != null) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                longhorn.setOwnerUUID(uuid);
                longhorn.setTame(true);
            }
        }
        return longhorn;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.8F;
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK);
    }

    @Override
    public boolean isPushable() {
        return this.canBePushed;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.COW_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(1, new LongHornPrepareChargeGoal(this));
        this.goalSelector.addGoal(3, new LongHornChargeGoal(this, 2.5F));
    }

    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0D);
            this.setHealth(20.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("StunTick", this.stunnedTick);
        compound.putBoolean("HasLeftHorn", this.hasLeftHorn());
        compound.putBoolean("HasRightHorn", this.hasRightHorn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.stunnedTick = compound.getInt("StunTick");
        this.entityData.set(DATA_HAS_LEFT_HORN, compound.getBoolean("HasLeftHorn"));
        this.entityData.set(DATA_HAS_RIGHT_HORN, compound.getBoolean("HasRightHorn"));
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGE_COOLDOWN_TICKS, 0);
        this.entityData.define(HAS_TARGET, false);
        this.entityData.define(DATA_HAS_LEFT_HORN, true);
        this.entityData.define(DATA_HAS_RIGHT_HORN, true);
    }

    public boolean hasLeftHorn() {
        return this.entityData.get(DATA_HAS_LEFT_HORN);
    }

    public boolean hasRightHorn() {
        return this.entityData.get(DATA_HAS_RIGHT_HORN);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.isBaby()) {
            return false;
        } else {
            boolean shouldHurt = true;
            float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float knockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            if (shouldHurt == target.hurt(this.damageSources().mobAttack(this), damage)) {
                if (knockback > 0.0f && target instanceof LivingEntity) {
                    ((LivingEntity) target).knockback(knockback * 0.5f, Mth.sin(this.getYRot() * ((float) Math.PI / 180)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180)));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                }
                this.doEnchantDamageEffects(this, target);
                this.setLastHurtMob(target);
            }
            return shouldHurt;
        }
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pTime);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    public boolean isAngryAt(LivingEntity p_21675_) {
        return this.canAttack(p_21675_);
    }

    protected void ageBoundaryReached() {
        if (this.isBaby()) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0D);
            this.removeHorns();
        } else {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D);
            this.addHorns();
        }

    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        boolean prev = super.canAttack(entity);
        if (isBaby()) {
            return false;
        }
        if (!hasRightHorn() && !hasLeftHorn()) {
            return false;
        }
        return prev;
    }

    protected boolean hasTargets() {
        return true;
    }

    private void attack(LivingEntity entity) {
        entity.hurt(this.damageSources().mobAttack(this), 8.0F);
    }

    public void setChargeCooldownTicks(int ticks) {
        this.entityData.set(CHARGE_COOLDOWN_TICKS, ticks);
    }

    public int getChargeCooldownTicks() {
        return this.entityData.get(CHARGE_COOLDOWN_TICKS);
    }

    public boolean hasChargeCooldown() {
        return this.entityData.get(CHARGE_COOLDOWN_TICKS) > 0;
    }

    public void resetChargeCooldownTicks() {
        this.entityData.set(CHARGE_COOLDOWN_TICKS, 40);
    }


    public void setHasTarget(boolean hasTarget) {
        this.entityData.set(HAS_TARGET, hasTarget);
    }
    public boolean hasTarget() {
        return this.entityData.get(HAS_TARGET);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return entityIn.is(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isAlive()) {
            return;
        }
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
        if (this.stunnedTick > 0) {
            --this.stunnedTick;
            this.stunEffect();
            if (random.nextFloat() <= 0.02F) {
                if (random.nextFloat() < 0.02F) {
                    this.dropHorn();
                }
            }
        }
    }

    public boolean dropHorn() {
        boolean flag = this.hasLeftHorn();
        boolean flag1 = this.hasRightHorn();
        if (!flag && !flag1) {
            return false;
        } else {
            EntityDataAccessor<Boolean> entitydataaccessor;
            if (!flag) {
                entitydataaccessor = DATA_HAS_RIGHT_HORN;
            } else if (!flag1) {
                entitydataaccessor = DATA_HAS_LEFT_HORN;
            } else {
                entitydataaccessor = this.random.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
            }

            this.entityData.set(entitydataaccessor, false);
            Vec3 vec3 = this.position();
            ItemStack itemstack = new ItemStack(ModItems.LONGHORN_HORN.get());
            double d0 = (double)Mth.randomBetween(this.random, -0.2F, 0.2F);
            double d1 = (double)Mth.randomBetween(this.random, 0.3F, 0.7F);
            double d2 = (double)Mth.randomBetween(this.random, -0.2F, 0.2F);
            ItemEntity itementity = new ItemEntity(this.level(), vec3.x(), vec3.y(), vec3.z(), itemstack, d0, d1, d2);
            this.level().addFreshEntity(itementity);
            return true;
        }
    }

    public void addHorns() {
        this.entityData.set(DATA_HAS_LEFT_HORN, true);
        this.entityData.set(DATA_HAS_RIGHT_HORN, true);
    }

    public void removeHorns() {
        this.entityData.set(DATA_HAS_LEFT_HORN, false);
        this.entityData.set(DATA_HAS_RIGHT_HORN, false);
    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d = this.getX() - (double) this.getBbWidth() * Math.sin(this.yBodyRot * ((float) Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            double e = this.getY() + (double) this.getBbHeight() - 0.3;
            double f = this.getZ() + (double) this.getBbWidth() * Math.cos(this.yBodyRot * ((float) Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            level().addParticle(ParticleTypes.CRIT, true, this.getX(), this.getEyeY() + 0.5F, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.stunnedTick > 0;
    }

    @Override
    protected void blockedByShield(LivingEntity defender) {
        this.stunnedTick = 60;
        this.resetChargeCooldownTicks();
        this.getNavigation().stop();
        this.playSound(SoundEvents.RAVAGER_STUNNED, 1.0f, 1.0f);
        this.level().broadcastEntityEvent(this, (byte) 39);
        defender.push(this);
        defender.hurtMarked = true;
    }

    public void performAttack() {
        if (!this.level().isClientSide) {
            this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
            for (Entity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D))) {
                if (!(entity instanceof LonghornEntity) && !(entity instanceof Player)) {
                    if (this.isTame() && this.hasControllingPassenger()) {
                        entity.hurt(this.damageSources().mobAttack(this), 10.0F);
                    }
                }
            }
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23F);
        }
    }

    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) {
            if (pTarget instanceof LonghornEntity) {
                LonghornEntity wolf = (LonghornEntity)pTarget;
                return !wolf.isTame() || wolf.getOwner() != pOwner;
            } else if (pTarget instanceof Player && pOwner instanceof Player && !((Player)pOwner).canHarmPlayer((Player)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorse && ((AbstractHorse)pTarget).isTamed()) {
                return false;
            } else {
                return !(pTarget instanceof TamableAnimal) || !((TamableAnimal)pTarget).isTame();
            }
        } else {
            return false;
        }
    }


    @Override
    public void handleEntityEvent(byte id) {
        if (id == 39) {
            this.stunnedTick = 60;
        }
        super.handleEntityEvent(id);
    }

    private boolean isWithinYRange(LivingEntity target) {
        if (target == null) {
            return false;
        }
        return Math.abs(target.getY() - this.getY()) < 3;
    }

    static class LongHornPrepareChargeGoal extends Goal {
        protected final LonghornEntity longhorn;

        public LongHornPrepareChargeGoal(LonghornEntity longhorn) {
            this.longhorn = longhorn;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.longhorn.getTarget();
            if (target == null || !target.isAlive() || this.longhorn.stunnedTick > 0 || !this.longhorn.isWithinYRange(target) || this.longhorn.isBaby()) {
                this.longhorn.resetChargeCooldownTicks();
                return false;
            }
            return target instanceof Player && longhorn.hasChargeCooldown() ;
        }

        @Override
        public void start() {
            LivingEntity target = this.longhorn.getTarget();
            if (target == null) {
                return;
            }
            this.longhorn.setHasTarget(true);
            this.longhorn.resetChargeCooldownTicks();
            this.longhorn.canBePushed = false;
        }

        @Override
        public void stop() {
            this.longhorn.setHasTarget(false);
            this.longhorn.canBePushed = true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.longhorn.getTarget();
            if (target == null) {
                return;
            }
            this.longhorn.getLookControl().setLookAt(target);
            this.longhorn.setChargeCooldownTicks(Math.max(0, this.longhorn.getChargeCooldownTicks() - 1));
        }
    }

    static class LongHornChargeGoal extends Goal {
        protected final LonghornEntity mob;
        private final double speedModifier;
        private Path path;
        private Vec3 chargeDirection;

        public LongHornChargeGoal(LonghornEntity pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.chargeDirection = Vec3.ZERO;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0 || this.mob.isBaby()) {
                return false;
            }
            this.path = (Path) this.mob.getNavigation().createPath(target, 0);
            return target instanceof Player && this.path != null;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0) {
                return false;
            }
            return !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            BlockPos blockPosition = this.mob.blockPosition();
            BlockPos target = this.path.getTarget();
            this.chargeDirection = new Vec3(blockPosition.getX() - target.getX(), 0.0, blockPosition.getZ() - target.getZ()).normalize();
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.mob.setSprinting(true);
        }

        @Override
        public void stop() {
            this.mob.resetChargeCooldownTicks();
            this.mob.getNavigation().stop();
            this.mob.setSprinting(false);
        }


        @Override
        public void tick() {
            this.mob.getLookControl().setLookAt(Vec3.atCenterOf(this.path.getTarget()));
            if (this.mob.horizontalCollision && this.mob.onGround()) {
                this.mob.jumpFromGround();
            }
            this.tryToHurt();
        }

        protected void tryToHurt() {
            List<LivingEntity> nearbyEntities = this.mob.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.mob, this.mob.getBoundingBox());
            if (!nearbyEntities.isEmpty()) {
                LivingEntity livingEntity = nearbyEntities.get(0);
                if (!(livingEntity instanceof LonghornEntity)) {
                    livingEntity.hurt(this.mob.damageSources().mobAttack(this.mob), (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    float speed = Mth.clamp(this.mob.getSpeed() * 1.65f, 0.2f, 3.0f);
                    float shieldBlockModifier = livingEntity.isDamageSourceBlocked(this.mob.damageSources().mobAttack(this.mob)) ? 0.5f : 1.0f;
                    livingEntity.knockback(shieldBlockModifier * speed * 2.0D, this.chargeDirection.x(), this.chargeDirection.z());
                    double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0.0, 0.4f * knockbackResistance, 0.0));
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    if (livingEntity.equals(this.mob.getTarget())) {
                        this.stop();
                    }
                }
            }
        }
    }


    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
            pPlayer.setItemInHand(pHand, itemstack1);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(pPlayer) || this.isTame() || itemstack.is(Items.GRASS) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                this.heal((float) itemstack.getFoodProperties(this).getNutrition());
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
            if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                return InteractionResult.SUCCESS;
            } else {
                return interactionresult;
            }

        } else if (itemstack.is(Items.GRASS) && this.stunnedTick > 0 ) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                this.tame(pPlayer);
                this.navigation.stop();
                this.setTarget((LivingEntity)null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.stunnedTick > 0) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.long_horn.stunned", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (geoAnimatableAnimationState.isMoving()) {
            if (this.isSprinting()) {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.long_horn.charge", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            } else {
                geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.long_horn.walk", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        } else if (this.hasChargeCooldown() && this.hasTarget()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.long_horn.prep", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.long_horn.idle", Animation.LoopType.LOOP));
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
