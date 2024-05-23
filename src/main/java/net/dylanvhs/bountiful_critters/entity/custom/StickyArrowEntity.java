package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class StickyArrowEntity extends Arrow {
    public StickyArrowEntity(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public StickyArrowEntity(EntityType type, double x, double y, double z, Level worldIn) {
        this(type, worldIn);
        this.setPos(x, y, z);
    }

    public StickyArrowEntity(Level worldIn, LivingEntity shooter) {
        this(ModEntities.STICKY_ARROW.get(), shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), worldIn);
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    public StickyArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ModEntities.STICKY_ARROW.get(), world);
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        Player shooter = (Player) this.getOwner();
        Level world = level();

        // That's supposed to work but no idea why it doesn't
        BlockState blockstate = ModBlocks.SEAGRASS_BALL_PLACED.get().defaultBlockState();
        for (Direction dir : Direction.values()) {
            boolean canAttach = MultifaceBlock.canAttachTo(world, dir, entity.blockPosition(), blockstate);
            blockstate = blockstate.setValue(MultifaceBlock.getFaceProperty(dir), canAttach);
            BountifulCritters.LOGGER.info(dir + " " + canAttach);
        }

        if ((entity instanceof LivingEntity && !this.level().isClientSide())) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 5));
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 180, 5));
            ((LivingEntity) entity).level().setBlock(entity.blockPosition(), blockstate, 2);
        }
        if (this.level().isClientSide) {
        Vec3 vec3 = this.getViewVector(0.0F);
        float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
        float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
        float f2 = 1.2F - this.random.nextFloat() * 0.7F;
        this.level().addParticle(ParticleTypes.SMOKE, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.SMOKE, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.SMOKE, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.TOTEM_OF_UNDYING, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.TOTEM_OF_UNDYING, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.TOTEM_OF_UNDYING, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean isInWater() {
        return false;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ModItems.STICKY_ARROW.get());
    }
}
