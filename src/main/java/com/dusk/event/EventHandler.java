package com.dusk.event;

import com.dusk.Dusk;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static int    tickCounter   = 0;
    private static double spawnModifier = 1.0d;

    @SubscribeEvent
    public static void onWorldTick(final TickEvent.WorldTickEvent event)
    {
        if (event.world.isClientSide() || event.phase == TickEvent.Phase.START)
        {
            return;
        }

        if ((event.world.dimension() != World.OVERWORLD))
        {
            return;
        }

        if (++tickCounter != 20)
        {
            return;
        }
        tickCounter = 0;

        adjustSpawnModifier(event.world.getDayTime());
    }

    /**
     * Original max
     */
    private static final int MONSTER_MAX_DEFAULT = EntityClassification.MONSTER.max;

    /**
     * Change vanilla values
     */
    private static void adjustClassification()
    {
        EntityClassification.MONSTER.max = (int) (MONSTER_MAX_DEFAULT * spawnModifier);
    }

    /**
     * Linear raise of modifier
     */
    private static final int RAISE_START = 12000;
    private static final int RAISE_STOP  = 16000;
    private static final int DROP_START  = 20000;

    private static final double INTERVAL = ((RAISE_STOP - RAISE_START) / 20.0d) * 100.0d;

    // 1800 Midnight 24000(0) sunrise 13000 night 12500 sleep time -> Raise 12000 -> 16000, drop >20000 -> <12000
    private static void adjustSpawnModifier(final long time)
    {
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

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        if (Dusk.config.getCommonConfig().disableSleep.get() || event.getPlayer().level.getDayTime() < Dusk.config.getCommonConfig().minSleepTime.get())
        {
            event.setResult(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
        }

        if (event.getPlayer().level.isClientSide())
        {
            return;
        }

        ((ServerPlayerEntity) event.getPlayer()).setRespawnPosition(event.getPlayer().level.dimension(), event.getPos(), event.getPlayer().yRot, false, true);
    }
}
