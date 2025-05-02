package net.dylanvhs.bountiful_critters.entity.custom.land;

import com.google.common.collect.Sets;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmartBodyHelper;
import net.dylanvhs.bountiful_critters.entity.custom.flying.ToucanEntity;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
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
import java.util.Set;
import java.util.UUID;


public class LionEntity extends TamableAnimal implements NeutralMob, GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public int timeUntilNextMane = this.random.nextInt(8000) + 8000;
    private static final EntityDataAccessor<Boolean> IS_ARMORED = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ARMOR_SLIGHTLY_DAMAGED = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ARMOR_DAMAGED = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ARMOR_REPAIRED = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_FUR_ID = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_HAS_MANE = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    public boolean waxed = false;

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    private static final Set<Item> TAME_FOOD = Sets.newHashSet(
            Items.BEEF,Items.COOKED_BEEF,
            Items.PORKCHOP,Items.COOKED_PORKCHOP,
            Items.MUTTON,Items.COOKED_MUTTON,
            Items.CHICKEN,Items.COOKED_CHICKEN,
            Items.RABBIT,Items.COOKED_RABBIT,
            ModItems.RAW_SUNFISH_MEAT.get(), ModItems.COOKED_SUNFISH_MEAT.get(),
            ModItems.RAW_ANGELFISH.get(),
            ModItems.RAW_NEON_TETRA.get(),
            ModItems.RAW_BARRELEYE.get(),
            ModItems.RAW_KRILL.get(),
            ModItems.RAW_FLOUNDER.get(),
            ModItems.RAW_PILLBUG.get(),
            Items.COD,Items.COOKED_COD,
            Items.SALMON,Items.COOKED_SALMON

    );

    public static final Ingredient TAME_ITEM = Ingredient.of(
            Items.BEEF,Items.COOKED_BEEF,
            Items.PORKCHOP,Items.COOKED_PORKCHOP,
            Items.MUTTON,Items.COOKED_MUTTON,
            Items.CHICKEN,Items.COOKED_CHICKEN,
            Items.RABBIT,Items.COOKED_RABBIT,
            ModItems.RAW_SUNFISH_MEAT.get(), ModItems.COOKED_SUNFISH_MEAT.get(),
            ModItems.RAW_ANGELFISH.get(),
            ModItems.RAW_NEON_TETRA.get(),
            ModItems.RAW_BARRELEYE.get(),
            ModItems.RAW_KRILL.get(),
            ModItems.RAW_FLOUNDER.get(),
            ModItems.RAW_PILLBUG.get(),
            Items.COD,Items.COOKED_COD,
            Items.SALMON,Items.COOKED_SALMON

    );

    public int armorDurability = 256;

    public LionEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
        return LionEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .build();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HasMane", this.hasMane());

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_HAS_MANE, compound.getBoolean("HasMane"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(IS_ARMORED, false);
        this.entityData.define(IS_ARMOR_SLIGHTLY_DAMAGED, false);
        this.entityData.define(IS_ARMOR_DAMAGED, false);
        this.entityData.define(IS_ARMOR_REPAIRED, false);
        this.entityData.define(DATA_FUR_ID, (byte)0);
        this.entityData.define(DATA_HAS_MANE,true);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cow.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pig.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Rabbit.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EmuEntity.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ToucanEntity.class, true) {
            @Override
            public boolean canUse() {
                return !isTame() && super.canUse();
            }
        });
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D) {
            @Override
            public boolean canUse() {
                return isBaby() && super.canUse();
            }
        });
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.LION_SPAWN_EGG.get());
    }

    @Nullable
    public LionEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        LionEntity lion = ModEntities.LION.get().create(pLevel);
        if (lion != null) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                lion.setOwnerUUID(uuid);
                lion.setTame(true);
            }
            int i = this.random.nextBoolean() ? this.getVariant() : ((LionEntity) pOtherParent).getVariant();
            lion.setVariant(i);
            lion.setPersistenceRequired();
        }
        return lion;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isArmored()) {
            if (!this.level().isClientSide) {
                this.spawnAtLocation(ModItems.LION_ARMOR.get());
            }
            this.setArmored(false);
        }
    }

    public boolean isFood(ItemStack pStack) {
        return TAME_ITEM.test(pStack);
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? pSize.height * 0.95F : 1.7F;
    }

    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.COARSE_DIRT);
    }

    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
            this.setHealth(50.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            setSprinting(isAggressive());

            setArmorRepaired(this.isArmored() && armorDurability > 128);
            setArmorSlightlyDamaged(this.isArmored() && armorDurability < 128);
            setArmorDamaged(this.isArmored() && armorDurability < 64);

            if (this.isArmored() && armorDurability == 0) {
                setArmored(false);
                setArmorDamaged(false);
            }
        }
    }
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !this.hasMane() && this.isAlive() && !this.isBaby() && !this.waxed && --this.timeUntilNextMane <= 0) {
            this.setMane(true);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            Entity entity = pSource.getEntity();
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
            }

            if (this.isArmored() && armorDurability > 0) {
                --armorDurability;
                --armorDurability;
                --armorDurability;
                --armorDurability;
            }

            if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                pAmount = (pAmount + 1.0F) / 2.0F;
            }

            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        boolean prev = super.canAttack(entity);
        if (this.isBaby()) {
            return false;
        }
        return prev;
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

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean hasMane() {
        return this.entityData.get(DATA_HAS_MANE);
    }

    public void setMane(boolean mane) {
        entityData.set(DATA_HAS_MANE, mane);
    }

    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) {
            if (pTarget instanceof LionEntity) {
                LionEntity wolf = (LionEntity)pTarget;
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

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();

        if (itemstack.getItem() == Items.SHEARS) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, pPlayer);
                this.setMane(false);
                this.timeUntilNextMane = this.random.nextInt(8000) + 8000;
                itemstack.hurtAndBreak(1, pPlayer, (p_29822_) -> {
                    p_29822_.broadcastBreakEvent(pHand);
                });
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        }

        if(itemstack.getItem() == Items.HONEYCOMB && !this.waxed && !this.hasMane()) {

            if (!this.level().isClientSide) {
                if (!pPlayer.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.HONEYCOMB_WAX_ON, 1.0F, 1.0F);
                this.waxed = true;

            }
            if (this.level().isClientSide && !this.waxed) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.WAX_ON, this.getX() - vec3.x * (double) f2 + (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 + (double) f1, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.WAX_ON, this.getX() - vec3.x * (double) f2 - (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 - (double) f1, 0.0D, 0.0D, 0.0D);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == Items.BROWN_WOOL && this.waxed) {
            if (!this.level().isClientSide) {
                if (!pPlayer.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.AXE_WAX_OFF, 1.0F, 1.0F);
                this.waxed = false;

            }
            if (this.level().isClientSide && this.waxed) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.WAX_OFF, this.getX() - vec3.x * (double) f2 + (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 + (double) f1, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.WAX_OFF, this.getX() - vec3.x * (double) f2 - (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 - (double) f1, 0.0D, 0.0D, 0.0D);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (this.level().isClientSide) {

            boolean flag = this.isOwnedBy(pPlayer) || this.isTame() || TAME_FOOD.contains(itemstack.getItem()) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;

        } else if (this.isTame()) {

            if (itemstack.getItem() == ModItems.LION_ARMOR.get() && !this.isArmored() && !this.isBaby()) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                playSound(SoundEvents.HORSE_ARMOR, 1.0F, 1.0F);
                setArmored(true);
                setArmorRepaired(true);
                armorDurability = 256;
                return InteractionResult.SUCCESS;
            }

            if (itemstack.getItem() == Items.COPPER_INGOT && this.isArmored() && armorDurability < 256 && !this.isBaby()) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                playSound(SoundEvents.COPPER_PLACE, 1.0F, 1.0F);
                ++armorDurability;
                ++armorDurability;
                return InteractionResult.SUCCESS;
            }


            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                this.heal((float)itemstack.getFoodProperties(this).getNutrition());
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.SUCCESS;

            } else {

                InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget((LivingEntity)null);
                    return InteractionResult.SUCCESS;
                } else {
                    return interactionresult;
                }
            }
        } else if (TAME_FOOD.contains(itemstack.getItem()) && !this.isAngry()) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
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
        }
        else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    public boolean isSheared() {
        return (this.entityData.get(DATA_FUR_ID) & 16) != 0;
    }

    public void setSheared(boolean pSheared) {
        byte b0 = this.entityData.get(DATA_FUR_ID);
        if (pSheared) {
            this.entityData.set(DATA_FUR_ID, (byte)(b0 | 16));
        } else {
            this.entityData.set(DATA_FUR_ID, (byte)(b0 & -17));
        }

    }

    public void shear(SoundSource pCategory) {
        this.level().playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, pCategory, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);
        for(int j = 0; j < i; ++j) {
            ItemEntity itementity = this.spawnAtLocation(Items.BROWN_WOOL);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby() && this.hasMane();
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        float variantChange = this.getRandom().nextFloat();
        if(variantChange <= 0.25F){
            this.setVariant(0);
        } else if(variantChange <= 0.30F){
            this.setVariant(1);
        } else if(variantChange <= 0.40F){
            this.setVariant(0);
        } else if(variantChange <= 0.55F){
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean isArmored() {
        return entityData.get(IS_ARMORED);
    }

    public void setArmored(boolean armored) {
        entityData.set(IS_ARMORED, armored);
    }

    public boolean isArmorRepaired() {
        return entityData.get(IS_ARMOR_REPAIRED);
    }

    public void setArmorRepaired(boolean armored) {
        entityData.set(IS_ARMOR_REPAIRED, armored);
    }

    public boolean isArmorSlightlyDamaged() {
        return entityData.get(IS_ARMOR_SLIGHTLY_DAMAGED);
    }

    public void setArmorSlightlyDamaged(boolean armored) {
        entityData.set(IS_ARMOR_SLIGHTLY_DAMAGED, armored);
    }

    public boolean isArmorDamaged() {
        return entityData.get(IS_ARMOR_DAMAGED);
    }

    public void setArmorDamaged(boolean armored) {
        entityData.set(IS_ARMOR_DAMAGED, armored);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving() && !this.isSprinting()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lion.walk", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
            return PlayState.CONTINUE;
        } else if (geoAnimatableAnimationState.isMoving() && this.isSprinting()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lion.sprint", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.5F);
            return PlayState.CONTINUE;
        }
        if (this.isInSittingPose()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lion.sit", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
            return PlayState.CONTINUE;
        }
        else geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lion.idle", Animation.LoopType.LOOP));
        geoAnimatableAnimationState.getController().setAnimationSpeed(1.0F);
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (this.swinging && geoAnimatableAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            geoAnimatableAnimationState.getController().forceAnimationReset();
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lion.attack", Animation.LoopType.PLAY_ONCE));
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
