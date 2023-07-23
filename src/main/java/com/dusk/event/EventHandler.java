package com.dusk.event;

import com.dusk.Dusk;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.Level.OVERWORLD;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static int    tickCounter   = 0;
    private static double spawnModifier = 1.0d;

    public static void onWorldTick(final ServerLevel world)
    {
        if (world.isClientSide())
        {
            return;
        }

        if ((world.dimension() != OVERWORLD))
        {
            return;
        }

        if (++tickCounter != 20)
        {
            return;
        }
        tickCounter = 0;

        adjustSpawnModifier(world.getDayTime() % 24000);
    }

    /**
     * Change vanilla values
     */
    private static void adjustClassification()
    {
        ((ISpawnGroupDataSetter) (Object) MobCategory.MONSTER).setMaxCap((int) (Dusk.config.getCommonConfig().baseMonsterCap * spawnModifier));
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
      final ServerPlayer serverPlayerEntity,
      final BlockPos pos,
      final CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir)
    {
        if (Dusk.config.getCommonConfig().disableSleep || (serverPlayerEntity.level.getDayTime() % 24000) < Dusk.config.getCommonConfig().minSleepTime)
        {
            cir.setReturnValue(Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW));
        }

        serverPlayerEntity.setRespawnPosition(serverPlayerEntity.level.dimension(), pos, serverPlayerEntity.getYHeadRot(), false, true);
    }
}
