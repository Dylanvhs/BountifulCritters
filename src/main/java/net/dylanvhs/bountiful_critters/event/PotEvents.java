package net.dylanvhs.bountiful_critters.event;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.dylanvhs.bountiful_critters.entity.custom.land.BluntHeadedTreeSnakeEntity;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BountifulCritters.MOD_ID)
public class PotEvents {

    @SubscribeEvent
    public static void potBreak(BlockEvent.BreakEvent event) {
        Level world = event.getPlayer().level();
        BlockPos pos = event.getPos();

        if (world.getBlockState(pos).is(Blocks.DECORATED_POT) && PotAccess.hasSnake(pos) && !world.isClientSide()) {
            world.addFreshEntity(PotAccess.getSnake(world, pos));
            PotAccess.setSnake(pos, (BluntHeadedTreeSnakeEntity) null);
        }
    }

    // This will be called from time to time on a pot containing a Snake. Not an actual event, so don't remove the // on the next line
    // Apparently I can only call this on the server so not sure sounds will work but if you feel daring you can do packets
    //SubscribeEvent
    public static void potSnakeTick(Level world, BlockPos pos) {
        if (world.getBlockState(pos).is(Blocks.DECORATED_POT) && PotAccess.hasSnake(pos) && !world.isClientSide()) {
            world.playSound(null, pos, ModSounds.SNAKE_HISS.get(), SoundSource.BLOCKS);
        }
    }
}
