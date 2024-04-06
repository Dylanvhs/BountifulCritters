package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class EmuEntity extends Animal implements GeoAnimatable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final ResourceLocation MODEL_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "geo/emu.geo.json");
    private static final ResourceLocation MODEL_BABY_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "geo/baby_emu.geo.json");
    private static final ResourceLocation TEXTURE_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/emu.png");
    private static final ResourceLocation TEXTURE_BABY_EMU = new ResourceLocation(BountifulCritters.MOD_ID, "textures/entity/baby_emu.png");
    private static final EntityDataAccessor<Integer> MODEL = SynchedEntityData.defineId(EmuEntity.class, EntityDataSerializers.INT);

    public EmuEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
     return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.5D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MODEL, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        if (geoAnimatableAnimationState.isMoving()) {
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.emu.sprint", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        else
            geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.emu.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    public int getModel() {
        return this.entityData.get(MODEL);
    }

    private void setModel(int model) {
        this.entityData.set(MODEL, model);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Model", this.getModel());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setModel(compound.getInt("Model"));
    }

    public ResourceLocation getEmuTexture() {
        if (getModel() == 1) {
            return TEXTURE_BABY_EMU;
        } else return TEXTURE_EMU;
    }


    public ResourceLocation getEmuModel() {
        if(getModel() == 1){
            return MODEL_BABY_EMU;
        } else return MODEL_EMU;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }


}

