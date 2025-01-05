
package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.criterion.ModCriterion;
import net.dylanvhs.bountiful_critters.damage.ModDamageTypes;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.Bagable;
import net.dylanvhs.bountiful_critters.entity.ai.Pickable;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PillbugEntity extends Animal implements GeoEntity, Pickable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.BYTE);
    private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;
    private static final EntityDataAccessor<Boolean> IS_ROLLED_UP = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_POISONOUS = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BOUNCES = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_PROJECTILE = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> THROWER = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> FROM_BAG = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean canBePushed = true;
    int bounces = 0;

    public PillbugEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isBasketball() {
        String n = ChatFormatting.stripFormatting(this.getName().getString());
        return n != null && (n.toLowerCase().contains("basketball"));
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.STONE) || blockstate.is(Blocks.DEEPSLATE);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.PILLBUG_SPAWN_EGG.get());
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

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(Items.ROTTEN_FLESH), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, (double) 1.2F, true));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Spider.class, 8.0F, 1.2D, 1.2D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Husk.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Drowned.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Stray.class, true));
    }

    public static AttributeSupplier setAttributes() {
        return AbstractSchoolingFish.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 3D)
                .add(Attributes.ARMOR_TOUGHNESS, 1D)
                .build();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.setRollUp(false);

            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public boolean isPushable() {
        return this.canBePushed;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(Items.ROTTEN_FLESH);
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.PILLBUG_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.PILLBUG_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.PILLBUG_DEATH.get();
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(ModSounds.PILLBUG_STEP.get(), 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageable) {
        return ModEntities.PILLBUG.get().create(world);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() == Items.FLOWER_POT && this.isAlive() && !isBaby() && !isRolledUp() && !this.isProjectile()) {
            playSound(SoundEvents.DECORATED_POT_PLACE, 1.0F, 1.0F);
            heldItem.shrink(1);
            ItemStack itemstack1 = new ItemStack(ModItems.POTTED_PILLBUG.get());
            this.setBucketData(itemstack1);
            if (!this.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }
            if (heldItem.isEmpty()) {
                player.setItemInHand(hand, itemstack1);
            } else if (!player.getInventory().add(itemstack1)) {
                player.drop(itemstack1, false);
            }
            this.discard();
            return InteractionResult.SUCCESS;
        }

        // DO NOT TOUCH
        // Unfortunately for you, I will touch
        else if (heldItem.isEmpty() && !isBaby() && isRolledUp()) {
            Pickable.bagMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }
        return super.mobInteract(player, hand);
    }

    public void shoot(double p_37266_, double p_37267_, double p_37268_, float scale, float p_37270_) {
        Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(this.random.triangle(0.0D, 0.04D * (double)p_37270_), this.random.triangle(0.0D, 0.04D * (double)p_37270_), this.random.triangle(0.0D, 0.04D * (double)p_37270_)).scale(scale);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotation(@Nullable Entity entity, float p_37253_, float p_37254_, float p_37255_, float scale, float inaccuracy) {
        float f = -Mth.sin(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((p_37253_ + p_37255_) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, scale, inaccuracy);
        if (entity != null) {
            Vec3 vec3 = entity.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, entity.onGround() ? 0.0D : vec3.y, vec3.z));
        }
    }

    @Override
    public boolean fromBag() {
        return this.entityData.get(FROM_BAG);
    }

    @Override
    public void setFromBag(boolean p_203706_1_) {
        this.entityData.set(FROM_BAG, p_203706_1_);
    }


    private void setBucketData(ItemStack bucket) {
        if (this.hasCustomName()) {
            bucket.setHoverName(this.getCustomName());
        }
    }

    @Override
    @Nonnull
    public ItemStack getBagItemStack() {
        ItemStack stack = new ItemStack(ModItems.PILLBUG_THROWABLE.get());
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
        Pickable.saveDefaultDataToBagTag(this, bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.put("PillbugData", serializeNBT());
        compoundnbt.putInt("Age", this.getAge());

    }

    @Override
    @Nonnull
    public SoundEvent getPickupSound() {
        return ModSounds.PILLBUG_AMBIENT.get();
    }

    @Override
    public void loadFromBagTag(@Nonnull CompoundTag compound) {
        Pickable.loadDefaultDataFromBagTag(this, compound);
        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        }
        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getBounces());
        compound.putBoolean("FromBag", this.fromBag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBounces(compound.getInt("Variant"));
        this.setFromBag(compound.getBoolean("FromBag"));
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROLLED_UP, false);
        this.entityData.define(IS_POISONOUS, false);
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(BOUNCES, bounces);
        this.entityData.define(IS_PROJECTILE, false);
        this.entityData.define(THROWER, Optional.empty());
        this.entityData.define(FROM_BAG, false);
    }

    public boolean isRolledUp() {
        return entityData.get(IS_ROLLED_UP);
    }

    public void setRollUp(boolean rollUp) {
        entityData.set(IS_ROLLED_UP, rollUp);
    }

    public boolean isPoisonous() {
        return entityData.get(IS_POISONOUS);
    }

    public void setPoisonous(boolean poisonous) {
        entityData.set(IS_POISONOUS, poisonous);
    }

    public int getBounces() {
        return this.entityData.get(BOUNCES);
    }

    public void setBounces(int variant) {
        this.entityData.set(BOUNCES, Integer.valueOf(variant));
    }

    public UUID getThrower() {
        return this.entityData.get(THROWER).orElse(null);
    }

    public void setThrower(UUID uuid) {
        this.entityData.set(THROWER, Optional.ofNullable(uuid));
    }


    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isRolledUp();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(this.isImmobile() ? 0.6 : 0.0);
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, pEntity);
        }

        return flag;
    }

    @Override
    protected float getJumpPower() {
        return 0.0F;
    }

    public void die(DamageSource pCause) {
        if (!this.isBaby()) {
            if (this.hasEffect(MobEffects.POISON)) {
                spawnAtLocation(ModItems.POISONOUS_PILLBUG.get());
            } else if (isOnFire()) {
                spawnAtLocation(ModItems.ROASTED_PILLBUG.get());
            } else spawnAtLocation(ModItems.RAW_PILLBUG.get());
        }
        if (!this.isBaby()) {
            if (this.hasEffect(MobEffects.POISON)) {
                spawnAtLocation(ModItems.POISONOUS_PILLBUG_SCUTE.get());
                spawnAtLocation(ModItems.POISONOUS_PILLBUG_SCUTE.get());
            } else {
                spawnAtLocation(ModItems.PILLBUG_SCUTE.get());
                spawnAtLocation(ModItems.PILLBUG_SCUTE.get());
            }
        }
        setRollUp(false);
        super.die(pCause);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.isInvulnerableTo(source);
    }

    @Override
    public void push(Entity entity) {
        if (isProjectile()) {
            setProjectile(false);
        }
        super.push(entity);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float amount) {
        super.actuallyHurt(damageSource, amount);

        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) && this.isProjectile() && amount > this.getHealth()) {
            Player player = level().getPlayerByUUID(getThrower());
            if (player instanceof ServerPlayer serverPlayer) ModCriterion.THROW_PILLBUG_IN_THE_VOID.trigger(serverPlayer);
        }
    }


    @Override
    public void tick() {
        super.tick();

        if (isProjectile()) {
            List<Entity> entities = level().getEntities(this, getBoundingBox(), Entity::canBeHitByProjectile);
            for (var entity : entities) {
                if (getBoundingBox().intersects(entity.getBoundingBox()) && entity.hurt(damageSources().mobProjectile(this, getThrower() != null ? level().getPlayerByUUID(getThrower()) : this), 1.0F)) {
                    playSound(ModSounds.PILLBUG_BOUNCE.get(), 0.8F, 1.0F);
                    setProjectile(false);
                }
                if (entity instanceof Player player && player.getUUID().equals(UUID.fromString("c7e2fbc4-e21e-40be-b8e1-8ac69ad53416"))) {
                    System.out.println(entity.getUUID());
                    if (level().getPlayerByUUID(getThrower()) instanceof ServerPlayer serverPlayer) ModCriterion.THROW_PILLBUG_IN_THE_VOID.trigger(serverPlayer);
                }
            }
            Level world = this.level();
            BlockPos pos = this.blockPosition() ;
            int radius = 1;
            for (int sx = -radius; sx <= radius; sx++) {
                    for (int sz = -radius; sz <= radius; sz++) {
                        for (int sy = -radius; sy <= radius; sy++) {
                            // Same loops but sy and sz, all nested
                            if (world.getBlockState(pos.offset(sx, sy, sz)).getBlock() == Blocks.GRASS_BLOCK) {

                                BlockHitResult hit = new BlockHitResult(this.position(), Direction.DOWN, this.blockPosition(), false);
                                this.setBounces(bounces ++);
                                playSound(ModSounds.PILLBUG_BOUNCE.get(), 0.8F, 1.0F);
                                Vec3 deltaMovement = this.getDeltaMovement();
                                Vec3 vec3 = deltaMovement.subtract(deltaMovement.x / 5, 0.0D, deltaMovement.z / 5);
                                Direction direction = hit.getDirection();
                                // x / 10.F = bounciness
                                double booster = 0.3D + (2 / 10.0F);
                                if (direction == Direction.UP || direction == Direction.DOWN) {
                                    this.setDeltaMovement(vec3.x, vec3.y < 0.0D ? -vec3.y * booster : 0.0D, vec3.z);
                                }
                                if (direction == Direction.WEST || direction == Direction.EAST) {
                                    this.setDeltaMovement(vec3.x < 0.65D ? -vec3.x * booster * Mth.sin(Mth.PI / 2) : 0.0D, vec3.y, vec3.z);
                                }
                                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                                    this.setDeltaMovement(vec3.x, vec3.y, vec3.z < 0.65D ? -vec3.z * booster * Mth.sin(3 * Mth.PI / 4) : 0.0D);
                                }
                                if (getBounces() >= 4) {
                                    setProjectile(false);
                                }

                            }
                        }
                    }

            }
        }

        if (onGround() || isInWater()) {
            setProjectile(false);
        }


        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
            setPoisonous(hasEffect(MobEffects.POISON));
        }
        setRollUp(isRolledUp());

        List<Player> list = level().getNearbyEntities(Player.class, TargetingConditions.DEFAULT, this, getBoundingBox().inflate(5.0D, 2.0D, 5.0D));
        List<BluntHeadedTreeSnakeEntity> list1 = level().getNearbyEntities(BluntHeadedTreeSnakeEntity.class, TargetingConditions.DEFAULT, this, getBoundingBox().inflate(5.0D, 2.0D, 5.0D));
        List<GeckoEntity> list2 = level().getNearbyEntities(GeckoEntity.class, TargetingConditions.DEFAULT, this, getBoundingBox().inflate(5.0D, 2.0D, 5.0D));


        if (!list.isEmpty() || !list1.isEmpty() || !list2.isEmpty()) {
            if (list.stream().noneMatch(Entity::isCrouching)) {
                setRollUp(true);
                getNavigation().stop();
            }
            else setRollUp(false);

        }
        else this.setRollUp(this.isProjectile() && !this.isClimbing());

    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        boolean prev = super.canAttack(entity);
        if (isBaby()) {
            return false;
        }
        return prev;
    }

    public static class PillbugEffectsGroupData implements SpawnGroupData {
        @javax.annotation.Nullable
        public MobEffect effect;

        public void setRandomEffect(RandomSource pRandom) {
            int i = pRandom.nextInt(2);
            if (i <= 1) {
                this.effect = MobEffects.MOVEMENT_SPEED;
            } else if (i == 2) {
                this.effect = MobEffects.POISON;
            }
        }
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        RandomSource randomsource = pLevel.getRandom();
        if (pSpawnData == null) {
            pSpawnData = new PillbugEntity.PillbugEffectsGroupData();
            if (pLevel.getDifficulty() == Difficulty.HARD && randomsource.nextFloat() < 0.1F * pDifficulty.getSpecialMultiplier()) {
                ((PillbugEntity.PillbugEffectsGroupData)pSpawnData).setRandomEffect(randomsource);
            }
        }

        if (pSpawnData instanceof PillbugEntity.PillbugEffectsGroupData pillbug$pillbugeffectsgroupdata) {
            MobEffect mobeffect =  pillbug$pillbugeffectsgroupdata.effect;
            if (mobeffect != null) {
                this.addEffect(new MobEffectInstance(mobeffect, -1));
            }
        }
        return pSpawnData;
    }

    public boolean isProjectile() {
        return this.entityData.get(IS_PROJECTILE);
    }

    public void setProjectile(boolean projectile) {
        this.entityData.set(IS_PROJECTILE, projectile);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {

        if (this.isRolledUp()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.pillbug.rolled_up", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isClimbing() && geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.pillbug.climb", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isClimbing() && !geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.pillbug.climb_idle", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.4F);
            return PlayState.CONTINUE;
        } else if (geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.pillbug.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.pillbug.idle", Animation.LoopType.LOOP));
        geoAnimatableAnimationState.getController().setAnimationSpeed(1.4F);
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
