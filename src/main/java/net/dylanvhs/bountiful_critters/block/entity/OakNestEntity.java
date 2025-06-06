package net.dylanvhs.bountiful_critters.block.entity;

import com.google.common.collect.Lists;
import net.dylanvhs.bountiful_critters.block.ModBlockEntities;
import net.dylanvhs.bountiful_critters.block.custom.OakNestBlock;
import net.dylanvhs.bountiful_critters.entity.custom.flying.HoopoeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.dylanvhs.bountiful_critters.block.property.ModProperties.HOOPOES;
import static net.dylanvhs.bountiful_critters.block.property.ModProperties.HOOPOE_EGGS;

public class OakNestEntity extends BlockEntity {
    public static final String MIN_OCCUPATION_TICKS_KEY = "MinOccupationTicks";
    public static final String ENTITY_DATA_KEY = "EntityData";
    public static final String TICKS_IN_NEST_KEY = "TicksInNest";
    public static final String HOOPOES_KEY = "Hoopoes";
    private final List<Hoopoe> hoopoes = Lists.newArrayList();
    private int day = -1;
    private int pacifyTicks = 0;

    public OakNestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OAK_NEST.get(), pos, state);
    }

    public boolean hasNoHoopoes() {
        return this.hoopoes.isEmpty();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isFullOfHoopoes() {
        return this.hoopoes.size() == 3;
    }

    public void angerHoopoes(@Nullable Player player, BlockState state, NestState nestState) {
        if (this.pacifyTicks > 0) return;
        List<Entity> list = this.tryReleaseHoopoe(state, nestState);
        if (player != null) {
            for (Entity entity : list) {
                if (!(entity instanceof HoopoeEntity hoopoe)) continue;
                if (!(player.position().distanceToSqr(entity.position()) <= 16.0)) continue;
                hoopoe.setTarget(player);
                hoopoe.setCannotEnterNestTicks(400);
            }
        }
    }

    public static void tickLayEgg(OakNestEntity oakNestEntity, Level world, BlockPos blockPos, BlockState state) {
        long day = world.getDayTime() / 24000L;
        if (oakNestEntity.day == -1 || day != oakNestEntity.day && day == 0) {
            oakNestEntity.day = (int) day;
        }
        if (state.getValue(HOOPOES) > 0 && oakNestEntity.day < day) {
            oakNestEntity.day++;
            world.setBlockAndUpdate(blockPos, state.setValue(HOOPOE_EGGS, Math.min(5, state.getValue(HOOPOE_EGGS) + state.getValue(HOOPOES))));
        }
    }

    private List<Entity> tryReleaseHoopoe(BlockState state, NestState nestState) {
        ArrayList<Entity> list = Lists.newArrayList();
        this.hoopoes.removeIf(hoopoe -> {
            assert this.level != null;
            return OakNestEntity.releaseHoopoe(this.level, this.worldPosition, state, hoopoe, list, nestState);
        });
        if (!list.isEmpty()) {
            super.setChanged();
        }
        return list;
    }

    public void tryEnterNest(Entity entity) {
        this.tryEnterNest(entity, 0);
    }

    public void tryEnterNest(Entity entity, int ticksInDwelling) {
        if (this.hoopoes.size() >= 3) {
            return;
        }
        entity.stopRiding();
        entity.ejectPassengers();
        CompoundTag nbtCompound = new CompoundTag();
        entity.save(nbtCompound);
        BlockPos blockPos = this.getBlockPos();
        this.addHoopoe(nbtCompound, ticksInDwelling);
        if (this.level != null) {
            this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, this.getBlockState()));
        }
        entity.discard();
        super.setChanged();
    }

    public void addHoopoe(CompoundTag nbtCompound, int ticksInDwelling) {
        assert this.level != null;
        this.hoopoes.add(new Hoopoe(nbtCompound, ticksInDwelling, 1200));
    }

    private static boolean releaseHoopoe(Level world, BlockPos pos, BlockState state, Hoopoe hoopoe, @Nullable List<Entity> entities, NestState nestState) {
        if (world.isDay() && nestState != NestState.EMERGENCY) {
            return false;
        }
        CompoundTag nbtCompound = hoopoe.entityData.copy();
        nbtCompound.put("NestPos", NbtUtils.writeBlockPos(pos));
        Direction direction = state.getValue(OakNestBlock.FACING);
        BlockPos blockPos = pos.relative(direction);
        boolean bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();

        if (bl && nestState != NestState.EMERGENCY) return false;
        Entity newEntity = EntityType.loadEntityRecursive(nbtCompound, world, entity -> entity);
        if (newEntity != null) {
            if (newEntity instanceof HoopoeEntity birtEntity) {
                OakNestEntity.ageHoopoe(Hoopoe.ticksInNest, birtEntity);
                if (entities != null) entities.add(birtEntity);
                float f = newEntity.getBbWidth();
                double d = bl ? 0.0 : 0.55 + (double)(f / 2.0f);
                double x = (double)pos.getX() + 0.5 + d * (double)direction.getStepX();
                double y = (double)pos.getY() + 0.5 - (double)(newEntity.getBbHeight() / 2.0f);
                double z = (double)pos.getZ() + 0.5 + d * (double)direction.getStepZ();
                newEntity.moveTo(x, y, z, newEntity.getYRot(), newEntity.getXRot());
            } else {
                return false;
            }
            world.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newEntity, world.getBlockState(pos)));
            return world.addFreshEntity(newEntity);
        }
        return false;
    }

    private static void ageHoopoe(int ticks, HoopoeEntity hoopoe) {
        int i = hoopoe.getAge();
        if (i < 0) hoopoe.setAge(Math.min(0, i + ticks));
        else if (i > 0) hoopoe.setAge(Math.max(0, i - ticks));
    }

    private static void tickHoopoes(Level world, BlockPos pos, BlockState state, List<Hoopoe> hoopoe) {
        boolean bl = false;
        Iterator<Hoopoe> iterator = hoopoe.iterator();
        world.setBlockAndUpdate(pos, state.setValue(HOOPOES, hoopoe.size()));
        while (iterator.hasNext()) {
            Hoopoe hoopoe1 = iterator.next();
            if (Hoopoe.ticksInNest > hoopoe1.minOccupationTicks) {
                if (OakNestEntity.releaseHoopoe(world, pos, state, hoopoe1, null, NestState.HOOPOES_RELEASED)) {
                    bl = true;
                    iterator.remove();
                }
            }
            ++Hoopoe.ticksInNest;
        }
        if (bl) OakNestEntity.setChanged(world, pos, state);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, OakNestEntity blockEntity) {
        OakNestEntity.tickHoopoes(world, pos, state, blockEntity.hoopoes);
        OakNestEntity.tickLayEgg(blockEntity, world, pos, state);
        if (blockEntity.pacifyTicks > 0) blockEntity.pacifyTicks--;
        if (!blockEntity.hoopoes.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            world.playSound(null, d, e, f, SoundEvents.CHICKEN_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.hoopoes.clear();
        ListTag nbtList = nbt.getList(HOOPOES_KEY, 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            Hoopoe hoopoe = new Hoopoe(nbtCompound.getCompound(ENTITY_DATA_KEY), nbtCompound.getInt(TICKS_IN_NEST_KEY), nbtCompound.getInt(MIN_OCCUPATION_TICKS_KEY));
            this.hoopoes.add(hoopoe);
        }
        this.day = nbt.getInt("Day");
        this.pacifyTicks = nbt.getInt("PacifyTicks");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put(HOOPOES_KEY, this.getHoopoes());
        nbt.putInt("Day", this.day);
        nbt.putInt("PacifyTicks", this.pacifyTicks);
    }

    public ListTag getHoopoes() {
        ListTag nbtList = new ListTag();
        for (Hoopoe hoopoe : this.hoopoes) {
            CompoundTag nbtCompound = hoopoe.entityData.copy();
            nbtCompound.remove("UUID");
            CompoundTag nbtCompound2 = new CompoundTag();
            nbtCompound2.put(ENTITY_DATA_KEY, nbtCompound);
            nbtCompound2.putInt(MIN_OCCUPATION_TICKS_KEY, hoopoe.minOccupationTicks);
            nbtList.add(nbtCompound2);
        }
        return nbtList;
    }

    public enum NestState {
        HOOPOES_RELEASED,
        EMERGENCY
    }

    static class Hoopoe {
        final CompoundTag entityData;
        static int ticksInNest;
        final int minOccupationTicks;

        Hoopoe(CompoundTag entityData, int ticksInNest, int minOccupationTicks) {
            this.entityData = entityData;
            OakNestEntity.Hoopoe.ticksInNest = ticksInNest;
            this.minOccupationTicks = minOccupationTicks;
        }
    }
}