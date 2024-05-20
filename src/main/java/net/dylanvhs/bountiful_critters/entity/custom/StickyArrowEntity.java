package net.dylanvhs.bountiful_critters.entity.custom;

import net.dylanvhs.bountiful_critters.block.ModBlocks;
import net.dylanvhs.bountiful_critters.entity.ModEntities;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

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
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        BlockPos blockPos = BlockPos.ZERO;
        BlockState blockstate = ModBlocks.SEAGRASS_BALL_PLACED.get().defaultBlockState();
        this.level().setBlock(blockPos, blockstate, 2);
        this.level().broadcastEntityEvent(this, (byte)3);
        this.discard();
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        BlockPos blockPos = BlockPos.ZERO;
        BlockState blockstate = ModBlocks.SEAGRASS_BALL_PLACED.get().defaultBlockState();
        this.level().setBlock(blockPos, blockstate, 2);
        this.level().broadcastEntityEvent(this, (byte)3);
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.STICKY_ARROW.get());
    }
}
