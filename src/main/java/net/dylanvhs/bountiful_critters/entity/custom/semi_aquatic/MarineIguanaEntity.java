package net.dylanvhs.bountiful_critters.entity.custom.semi_aquatic;

import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.entity.ai.ModBlockPos;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmartBodyHelper;
import net.dylanvhs.bountiful_critters.entity.ai.navigation.SmoothSwimmingMoveControlButNotBad;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class MarineIguanaEntity extends Animal implements GeoEntity, Bucketable {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(MarineIguanaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(MarineIguanaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SNEEZING = SynchedEntityData.defineId(MarineIguanaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.KELP, Items.SEAGRASS);

    public int timeUntilNextSneeze = this.random.nextInt(3500) + 3500;

    public boolean passive = false;

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.4F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public MarineIguanaEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new MarineIguanaEntity.IguanaMoveControl(this);
        this.lookControl = new MarineIguanaEntity.IguanaLookControl(this, 20);
        this.setMaxUpStep(1.0F);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.MARINE_IGUANA_SPAWN_EGG.get());
    }

    static class IguanaMoveControl extends SmoothSwimmingMoveControlButNotBad {
        private final MarineIguanaEntity axolotl;

        public IguanaMoveControl(MarineIguanaEntity pAxolotl) {
            super(pAxolotl, 85, 10, 0.5F, 0.6F, true);
            this.axolotl = pAxolotl;
        }

        public void tick() {
            super.tick();
        }
    }

    class IguanaLookControl extends SmoothSwimmingLookControl {
        public IguanaLookControl(MarineIguanaEntity pAxolotl, int pMaxYRotFromCenter) {
            super(pAxolotl, pMaxYRotFromCenter);
        }

        public void tick() {
            super.tick();
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
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.25D, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10) {
            @Override
            public boolean canUse() {
                return isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D, 15) {
            @Override
            public boolean canUse() {
                return !isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new MarineIguanaEntity.IguanaEatSeagrass(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return !isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return !isInWater() && super.canUse();
            }
        });
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
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
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if(itemstack.getItem() == ModItems.SALTED_KELP.get() && !this.passive) {

            if (!this.level().isClientSide) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.setTarget((LivingEntity)null);
                this.heal(20.0F);
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);

                this.passive = true;

            }
            if (this.level().isClientSide && !this.passive) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.SNEEZE, this.getX() - vec3.x * (double) f2 + (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 + (double) f1, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.SNEEZE, this.getX() - vec3.x * (double) f2 - (double) f, this.getY() - vec3.y, this.getZ() - vec3.z * (double) f2 - (double) f1, 0.0D, 0.0D, 0.0D);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
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

    public boolean isName() {
        String n = ChatFormatting.stripFormatting(this.getName().getString());
        return n != null && (n.toLowerCase().contains("gojira"));
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        boolean flag = false;
        float variantChange = this.getRandom().nextFloat();
        if (reason != MobSpawnType.BUCKET) {
            if (flag) {
                this.setAge(-24000);
            }
        }
        if(variantChange <= 0.009F){
            this.setVariant(1);
        } else if(variantChange <= 0.30F){
            this.setVariant(2);
        } else if(variantChange <= 0.45F){
            this.setVariant(3);
        } else if(variantChange <= 0.60F){
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

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.AXOLOTL_DEATH;
    }

    @Nullable
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
        return true;
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

    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && this.onGround() && --this.timeUntilNextSneeze <= 0) {
            this.playSound(SoundEvents.AXOLOTL_HURT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.SALT.get(), 2);
            this.timeUntilNextSneeze = this.random.nextInt(3500) + 3500;
            setSneezing(true);
            double d0 = 0;
            double d1 = Math.max(0.0D, 1.0D - d0);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)0.4F * d1, 0.0D));

        } else if (this.timeUntilNextSneeze > 0) {
            setSneezing(false);
        }
        if (this.level().isClientSide && this.isAlive() && !this.isBaby() && this.onGround() && this.timeUntilNextSneeze < 3) {
            for(int i = 0; i < 8; ++i) {
                Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                this.level().addParticle(ParticleTypes.SPIT, this.getX() + this.getLookAngle().x / 2.0D, this.getY(), this.getZ() + this.getLookAngle().z / 2.0D, vec3.x, vec3.y + 0.05D, vec3.z);
            }
        }
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

    public class IguanaEatSeagrass extends Goal {
        private final MarineIguanaEntity iguana;
        private int idleAtFlowerTime = 0;
        private int timeoutCounter = 0;
        private int searchCooldown = 0;
        private boolean isAboveDestinationBear;
        private BlockPos destinationBlock;
        private final BlockSorter targetSorter;



        public IguanaEatSeagrass(MarineIguanaEntity iguana) {
            super();
            this.iguana = iguana;
            this.targetSorter = new BlockSorter(iguana);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        public void start() {
            super.start();
        }

        public boolean canUse() {

            if (!iguana.isBaby()  && iguana.isInWater() && !iguana.passive) {
                if(searchCooldown <= 0){
                    resetTarget();
                    searchCooldown = 1000 + iguana.getRandom().nextInt(1000);
                    return destinationBlock != null;
                }else{
                    searchCooldown--;
                }
            }
            return false;
        }

        public boolean canContinueToUse() {
            return destinationBlock != null && timeoutCounter < 1200 && (iguana.getTarget() == null || !iguana.getTarget().isAlive());
        }

        public void stop() {
            searchCooldown = 1000;
            timeoutCounter = 0;
            destinationBlock = null;
        }

        public double getTargetDistanceSq() {
            return 2.3D;
        }

        public void tick() {
            BlockPos blockpos = destinationBlock;
            float yDist = (float) Math.abs(blockpos.getY() - iguana.getY() - iguana.getBbHeight()/2);
            this.iguana.getNavigation().moveTo((double) ((float) blockpos.getX()) + 0.5D, blockpos.getY() + 0.5D, (double) ((float) blockpos.getZ()) + 0.5D, 1);
            if (!isWithinXZDist(blockpos, iguana.position(), this.getTargetDistanceSq()) || yDist > 2F) {
                this.isAboveDestinationBear = false;
                ++this.timeoutCounter;
            } else {
                this.isAboveDestinationBear = true;
                --this.timeoutCounter;
            }
            if(timeoutCounter > 2400){
                stop();
            }
            if (this.getIsAboveDestination()) {
                iguana.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(destinationBlock.getX() + 0.5D, destinationBlock.getY(), destinationBlock.getZ() + 0.5));
                if (this.idleAtFlowerTime >= 2) {
                    idleAtFlowerTime = 0;
                    this.breakBlock();
                    playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.stop();
                } else {
                    ++this.idleAtFlowerTime;
                }
            }
            super.tick();

        }

        private void resetTarget() {
            List<BlockPos> allBlocks = new ArrayList<>();
            int radius = 16;
            for (BlockPos pos : BlockPos.betweenClosedStream(this.iguana.blockPosition().offset(-radius, -radius, -radius), this.iguana.blockPosition().offset(radius, radius, radius)).map(BlockPos::immutable).collect(Collectors.toList())) {
                if (!iguana.level().isEmptyBlock(pos) && shouldMoveTo(iguana.level(), pos)) {
                    if(!iguana.isInWater() || isBlockTouchingWater(pos)){
                        allBlocks.add(pos);
                    }
                }
            }
            if (!allBlocks.isEmpty()) {
                allBlocks.sort(this.targetSorter);
                for(BlockPos pos : allBlocks){
                    if(hasLineOfSightBlock(pos)){
                        this.destinationBlock = pos;
                        return;
                    }
                }
            }
            destinationBlock = null;
        }

        private boolean isBlockTouchingWater(BlockPos pos) {
            for(Direction dir : Direction.values()){
                if(iguana.level().getFluidState(pos.relative(dir)).is(FluidTags.WATER)){
                    return true;
                }
            }
            return false;
        }

        private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
            return blockpos.distSqr(ModBlockPos.fromCoords(positionVec.x(), blockpos.getY(), positionVec.z())) < distance * distance;
        }

        protected boolean getIsAboveDestination() {
            return this.isAboveDestinationBear;
        }

        private void breakBlock() {
            if (shouldMoveTo(iguana.level(), destinationBlock)) {
                BlockState state = iguana.level().getBlockState(destinationBlock);
                if(!iguana.level().isEmptyBlock(destinationBlock) && net.minecraftforge.common.ForgeHooks.canEntityDestroy(iguana.level(), destinationBlock, iguana) && state.getDestroySpeed(iguana.level(), destinationBlock) >= 0){
                    MarineIguanaEntity.this.level().destroyBlock(destinationBlock,false);
                    MarineIguanaEntity.this.playSound(SoundEvents.WET_GRASS_BREAK, 1.0F, 1.0F);
                    spawnAtLocation(ModItems.SEAGRASS_BALL.get());
                }
            }
        }

        private boolean hasLineOfSightBlock(BlockPos destinationBlock) {
            Vec3 Vector3d = new Vec3(iguana.getX(), iguana.getEyeY(), iguana.getZ());
            Vec3 blockVec = net.minecraft.world.phys.Vec3.atCenterOf(destinationBlock);
            BlockHitResult result = iguana.level().clip(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, iguana));
            return result.getBlockPos().equals(destinationBlock);
        }


        protected boolean shouldMoveTo(LevelReader worldIn, BlockPos pos) {
            Item blockItem = worldIn.getBlockState(pos).getBlock().asItem();
            return worldIn.getBlockState(pos).is(Blocks.SEAGRASS) || worldIn.getBlockState(pos).is(Blocks.TALL_SEAGRASS);
        }

        public record BlockSorter(Entity entity) implements Comparator<BlockPos> {
            @Override
            public int compare(BlockPos pos1, BlockPos pos2) {
                final double distance1 = this.getDistance(pos1);
                final double distance2 = this.getDistance(pos2);
                return Double.compare(distance1, distance2);
            }

            private double getDistance(BlockPos pos) {
                final double deltaX = this.entity.getX() - (pos.getX() + 0.5);
                final double deltaY = this.entity.getY() + this.entity.getEyeHeight() - (pos.getY() + 0.5);
                final double deltaZ = this.entity.getZ() - (pos.getZ() + 0.5);
                return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
            }
        }
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 4, this::predicate));
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
