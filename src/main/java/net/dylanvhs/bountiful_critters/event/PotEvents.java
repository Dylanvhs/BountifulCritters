package net.dylanvhs.bountiful_critters.event;

import net.dylanvhs.bountiful_critters.BountifulCritters;
import net.dylanvhs.bountiful_critters.entity.PotAccess;
import net.dylanvhs.bountiful_critters.entity.custom.BluntHeadedTreeSnakeEntity;
import net.minecraft.core.BlockPos;
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

        if (world.getBlockState(pos).is(Blocks.DECORATED_POT) && PotAccess.SNAKES.containsKey(pos) && !world.isClientSide()) {
            world.addFreshEntity(PotAccess.getSnake(world, pos));
            PotAccess.setSnake(pos, (BluntHeadedTreeSnakeEntity) null);
        }
    }
}
