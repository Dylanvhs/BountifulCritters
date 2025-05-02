package net.dylanvhs.bountiful_critters.entity.custom.land;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmartBodyHelper;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;

public class EmuEntity extends Animal implements GeoAnimatable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.APPLE);
    public int timeUntilNextEgg = this.random.nextInt(6000) + 6000;
    private PanicGoal panicGoal;

    public int filterCooldown;

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public EmuEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .build();
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.EMU_SPAWN_EGG.get());
    }

    @Nullable
    public EmuEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.EMU.get().create(pLevel);
    }

    public boolean isFood(ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? pSize.height * 0.95F : 2.0F;
    }

    public static <T extends Mob> boolean canSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(p_223317_3_.below());
        return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.ORANGE_TERRACOTTA);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.EMU_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.EMU_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.EMU_DEATH.get();
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }
    protected float getSoundVolume() {
        return 0.5F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.panicGoal = new PanicGoal(this, 1.3D);
        this.goalSelector.addGoal(0, this.panicGoal);
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.3D, 1.3D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new EmuEntity.EmuEatLeaves((double)1.2F, 16, 6));
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.EMU_EGG.get());
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }
        setSprinting(isPanicking());
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        pCompound.putInt("FilterCooldown", this.filterCooldown);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.filterCooldown = pCompound.getInt("FilterCooldown");
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isAlive()) {
            return;
        }
        if (this.filterCooldown > 0) {
            --this.filterCooldown;
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 39) {
            this.filterCooldown = 1000;
        }
        super.handleEntityEvent(id);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        ItemStack itemstack1 = Items.MELON_SLICE.getDefaultInstance();
        ItemStack itemstack2 = Items.PUMPKIN.getDefaultInstance();
        ItemStack itemstack3 = Items.BEETROOT.getDefaultInstance();
        ItemStack itemstack4 = Items.EGG.getDefaultInstance();
        float moreDrops = this.getRandom().nextFloat();
        if (this.filterCooldown == 0) {
            if (heldItem.getItem() == Items.MELON_SLICE && this.isAlive() && !isBaby()) {
                playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F);
                for(int i = 0; i < 8; ++i) {
                    Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack1), this.getX() + this.getLookAngle().x / 2.0D, this.getEyeHeight(), this.getZ() + this.getLookAngle().z / 4.0D, vec3.x, vec3.y + 0.05D, vec3.z);
                }
                heldItem.shrink(1);
                this.filterCooldown = 100;
                if (moreDrops <= 0.6F) {
                    spawnAtLocation(Items.MELON_SEEDS,2);
                    spawnAtLocation(Items.MELON_SEEDS,2);
                    if (moreDrops <= 0.5F) {
                        spawnAtLocation(Items.MELON_SEEDS,2);
                        spawnAtLocation(Items.MELON_SEEDS,2);

                    }

                } else if (moreDrops <= 0.75F) {
                    spawnAtLocation(Items.MELON_SEEDS,2);
                    spawnAtLocation(Items.MELON_SEEDS,2);

                } else if (moreDrops <= 0.9F) {
                    spawnAtLocation(Items.MELON_SEEDS,2);
                    spawnAtLocation(Items.MELON_SEEDS,2);

                } else if (moreDrops <= 0.95F){
                    spawnAtLocation(Items.MELON_SEEDS,2);

                }
                spawnAtLocation(Items.MELON_SEEDS,2);
                return InteractionResult.SUCCESS;
            }

            if (heldItem.getItem() == Items.PUMPKIN && this.isAlive() && !isBaby()) {
                playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F);
                for(int i = 0; i < 8; ++i) {
                    Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack2), this.getX() + this.getLookAngle().x / 2.0D, this.getEyeHeight(), this.getZ() + this.getLookAngle().z / 4.0D, vec3.x, vec3.y + 0.05D, vec3.z);
                }
                heldItem.shrink(1);
                this.filterCooldown = 100;
                if (moreDrops <= 0.6F) {
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    if (moreDrops <= 0.5F) {
                        spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                        spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    }

                } else if (moreDrops <= 0.75F) {
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);

                } else if (moreDrops <= 0.9F) {
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);

                }else if (moreDrops <= 0.99F){
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                    spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                }
                spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                spawnAtLocation(Items.PUMPKIN_SEEDS,2);
                return InteractionResult.SUCCESS;
            }

            if (heldItem.getItem() == Items.BEETROOT && this.isAlive() && !isBaby()) {
                playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F);
                for(int i = 0; i < 8; ++i) {
                    Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack3), this.getX() + this.getLookAngle().x / 2.0D, this.getEyeHeight(), this.getZ() + this.getLookAngle().z / 4.0D, vec3.x, vec3.y + 0.05D, vec3.z);
                }
                heldItem.shrink(1);
                this.filterCooldown = 100;
                if (moreDrops <= 0.6F) {
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    if (moreDrops <= 0.5F) {
                        spawnAtLocation(Items.BEETROOT_SEEDS,2);
                        spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    }
                } else if (moreDrops <= 0.75F) {
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);

                } else if (moreDrops <= 0.9F) {
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);

                } else if (moreDrops <= 0.99F){
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                    spawnAtLocation(Items.BEETROOT_SEEDS,2);
                }
                spawnAtLocation(Items.BEETROOT_SEEDS,2);
                spawnAtLocation(Items.BEETROOT_SEEDS,2);
                return InteractionResult.SUCCESS;
            }

            if (heldItem.getItem() == Items.EGG && this.isAlive() && !isBaby() || heldItem.getItem() == ModItems.EMU_EGG.get() && this.isAlive() && !isBaby() || heldItem.getItem() == ModItems.PHEASANT_EGG.get() && this.isAlive() && !isBaby()) {
                playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F);
                for(int i = 0; i < 8; ++i) {
                    Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack4), this.getX() + this.getLookAngle().x / 2.0D, this.getEyeHeight(), this.getZ() + this.getLookAngle().z / 4.0D, vec3.x, vec3.y + 0.05D, vec3.z);
                }
                heldItem.shrink(1);
                this.filterCooldown = 100;
                if (moreDrops <= 0.6F) {
                    spawnAtLocation(Items.BONE_MEAL,2);
                    spawnAtLocation(Items.BONE_MEAL,2);
                    if (moreDrops <= 0.5F) {
                        spawnAtLocation(Items.BONE_MEAL,2);
                        spawnAtLocation(Items.BONE_MEAL,2);
                    }
                } else if (moreDrops <= 0.75F) {
                    spawnAtLocation(Items.BONE_MEAL,2);
                    spawnAtLocation(Items.BONE_MEAL,2);

                } else if (moreDrops <= 0.9F) {
                    spawnAtLocation(Items.BONE_MEAL,2);
                    spawnAtLocation(Items.BONE_MEAL,2);

                } else if (moreDrops <= 0.99F){
                    spawnAtLocation(Items.BONE_MEAL,2);
                    spawnAtLocation(Items.BONE_MEAL,2);
                }
                spawnAtLocation(Items.BONE_MEAL,2);
                spawnAtLocation(Items.BONE_MEAL,2);
                spawnAtLocation(Items.BONE_MEAL,2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    private boolean isPanicking() {
        return this.panicGoal != null && this.panicGoal.isRunning();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    public class EmuEatLeaves extends MoveToBlockGoal {
        private static final int WAIT_TICKS = 40;
        protected int ticksWaited;

        public EmuEatLeaves(double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(EmuEntity.this, pSpeedModifier, pSearchRange, pVerticalSearchRange);
        }

        public double acceptedDistance() {
            return 2.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            BlockState blockstate = pLevel.getBlockState(pPos);
            return blockstate.is(Blocks.OAK_LEAVES);
        }

        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }

        protected void onReachedTarget() {
            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(EmuEntity.this.level(), EmuEntity.this)) {
                BlockState blockstate = EmuEntity.this.level().getBlockState(this.blockPos);
                if (blockstate.is(Blocks.OAK_LEAVES)) {
                    this.eatLeaves(blockstate);
                }
            }
        }
        private void eatLeaves(BlockState pState) {
            EmuEntity.this.level().destroyBlock(blockPos,false);
            EmuEntity.this.playSound(SoundEvents.GRASS_BREAK, 1.0F, 1.0F);
             if (EmuEntity.this.random.nextFloat() < 0.6F) {
               for(int i = 0; i < EmuEntity.this.random.nextInt(2) + 1; ++i) {
                  spawnAtLocation(Items.APPLE);
               }
               for(int i = 1; i < EmuEntity.this.random.nextInt(2) + 2; ++i) {
                   spawnAtLocation(Items.APPLE);
               }
               for(int i = 2; i < EmuEntity.this.random.nextInt(2) + 3; ++i) {
                   spawnAtLocation(Items.APPLE);
               }
            }
        }
        public boolean canUse() {
            return !isBaby() && super.canUse();
        }

        public void start() {
            this.ticksWaited = 0;
            super.start();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.emu.sprint", Animation.LoopType.LOOP));
            geoAnimatableAnimationState.getController().setAnimationSpeed(5F);
            return PlayState.CONTINUE;

        } else geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.emu.idle", Animation.LoopType.LOOP));
        geoAnimatableAnimationState.getController().setAnimationSpeed(1F);
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

