package com.dusk.event;

import com.dusk.Dusk;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler {
    private static int tickCounter = 0;
    private static double spawnModifier = 1.0d;

    @SubscribeEvent
    public static void onWorldTick(final TickEvent.LevelTickEvent event)
    {
        if (event.level.isClientSide() || event.phase == TickEvent.Phase.START)
        {
            return;
        }

        if ((event.level.dimension() != Level.OVERWORLD))
        {
            return;
        }

        if (++tickCounter != 20)
        {
            return;
        }
        tickCounter = 0;

        adjustSpawnModifier(event.level.getDayTime() % 24000);
    }

    /**
     * Change vanilla values
     */
    private static void adjustClassification() {
        MobCategory.MONSTER.max = (int) (Dusk.config.getCommonConfig().baseMonsterCap.get() * spawnModifier);
    }

    /**
     * Linear raise of modifier
     */
    private static final int RAISE_START = 12000;
    private static final int RAISE_STOP = 16000;
    private static final int DROP_START = 20000;

    private static final double INTERVAL = ((RAISE_STOP - RAISE_START) / 20.0d) * 100.0d;

    // 1800 Midnight 24000(0) sunrise 13000 night 12500 sleep time -> Raise 12000 -> 16000, drop >20000 -> <12000
    private static void adjustSpawnModifier(final long time) {
        if (time <= RAISE_STOP && time >= RAISE_START)
        {
            spawnModifier += Dusk.config.getCommonConfig().nightSpawnMod.get() / INTERVAL;
            adjustClassification();
        }
        else if (spawnModifier > 1.0d && (time > DROP_START || time < RAISE_START))
        {
            spawnModifier -= Dusk.config.getCommonConfig().nightSpawnMod.get() / INTERVAL;
            spawnModifier = Math.max(spawnModifier, 1.0d);
            adjustClassification();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        if (Dusk.config.getCommonConfig().disableSleep.get() || event.getEntity().level.getDayTime() < Dusk.config.getCommonConfig().minSleepTime.get())
        {
            event.setResult(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
            event.setCanceled(true);
        }

        if (event.getEntity().level.isClientSide())
        {
            return;
        }

        ((ServerPlayer) event.getEntity()).setRespawnPosition(event.getEntity().level.dimension(), event.getPos(), event.getEntity().getYRot(), false, true);
    }
}
