package com.dusk.event;

import com.dusk.Dusk;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static int    tickCounter   = 0;
    private static double spawnModifier = 1.0d;

    public static void onWorldTick(final ServerWorld world)
    {
        if (world.isClient())
        {
            return;
        }

        if ((world.getRegistryKey() != World.OVERWORLD))
        {
            return;
        }

        if (++tickCounter != 20)
        {
            return;
        }
        tickCounter = 0;

        adjustSpawnModifier(world.getTimeOfDay() % 24000);
    }

    /**
     * Change vanilla values
     */
    private static void adjustClassification()
    {
        ((ISpawnGroupDataSetter) (Object) SpawnGroup.MONSTER).setMaxCap((int) (Dusk.config.getCommonConfig().baseMonsterCap * spawnModifier));
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
            spawnModifier += Dusk.config.getCommonConfig().nightSpawnMod / INTERVAL;
            adjustClassification();
        }
        else if (spawnModifier > 1.0d && (time > DROP_START || time < RAISE_START))
        {
            spawnModifier -= Dusk.config.getCommonConfig().nightSpawnMod / INTERVAL;
            spawnModifier = Math.max(spawnModifier, 1.0d);
            adjustClassification();
        }
    }

    public static void onPlayerSleep(
      final ServerPlayerEntity serverPlayerEntity,
      final BlockPos pos,
      final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir)
    {
        if (Dusk.config.getCommonConfig().disableSleep || serverPlayerEntity.world.getTimeOfDay() < Dusk.config.getCommonConfig().minSleepTime)
        {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW));
        }

        serverPlayerEntity.setSpawnPoint(serverPlayerEntity.world.getRegistryKey(), pos, serverPlayerEntity.getYaw(), false, true);
    }
}
