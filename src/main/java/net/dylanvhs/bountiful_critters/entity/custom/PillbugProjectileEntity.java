package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.damage.ModDamageTypes;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PillbugProjectileEntity extends ThrowableItemProjectile {
    //geeeeeeeeeeeeeeeeeeeeeeeeeeet dunked on

    public PillbugProjectileEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PillbugProjectileEntity(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.THROWABLE_PILLBUG.get(), pShooter, pLevel);
    }

    public PillbugProjectileEntity(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.THROWABLE_PILLBUG.get(), pX, pY, pZ, pLevel);
    }
    private static final EntityDataAccessor<Integer> BOUNCES = SynchedEntityData.defineId(PillbugEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOUNCES, bounces);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getBounces());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBounces(compound.getInt("Variant"));
    }

    public int getBounces() {
        return this.entityData.get(BOUNCES);
    }

    public void setBounces(int variant) {
        this.entityData.set(BOUNCES, Integer.valueOf(variant));
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        playSound(ModSounds.PILLBUG_BOUNCE.get(), 0.8F, 1.0F);
        Entity entity = pResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource damageSource = ModDamageTypes.causePillballDamage(entity.level().registryAccess(), owner);
        entity.hurt(damageSource, 5);
        PillbugEntity pillbug = ModEntities.PILLBUG.get().create(level());
        if (pillbug != null) {
            pillbug.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            if (this.hasCustomName()) {
                setCustomName(this.getCustomName());
                level().addFreshEntity(pillbug);
            } else level().addFreshEntity(pillbug);
        }
        this.discard();
    }


    @Override
    protected Item getDefaultItem() {
        return null;
    }
    int bounces = 0;
    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
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
            PillbugEntity pillbug = ModEntities.PILLBUG.get().create(level());
            if (!this.level().isClientSide()) {
                this.level().broadcastEntityEvent(this, (byte) 3);
                pillbug.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                if (this.hasCustomName()) {
                    setCustomName(this.getCustomName());
                    level().addFreshEntity(pillbug);
                } else level().addFreshEntity(pillbug);
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        PillbugEntity pillbug = ModEntities.PILLBUG.get().create(level());
        if(!this.level().isClientSide() && this.isInWater()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            pillbug.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            if (this.hasCustomName()) {
                setCustomName(this.getCustomName());
                level().addFreshEntity(pillbug);
            } else level().addFreshEntity(pillbug);
            this.discard();
        }
    }
}