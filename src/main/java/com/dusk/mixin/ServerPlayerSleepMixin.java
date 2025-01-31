package com.dusk.mixin;

import com.dusk.event.EventHandler;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerSleepMixin
{
    @Inject(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startSleepInBed(Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;"), cancellable = true)
    public void on(
      final BlockPos blockPos,
      final CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir)
    {
        EventHandler.onPlayerSleep((ServerPlayer) (Object) this, blockPos, cir);
    }

    @Inject(method = "startSleeping", at = @At(value = "HEAD"), cancellable = true)
    public void forcomfortshardcoding(
        final BlockPos pos, final CallbackInfo ci)
    {
        EventHandler.onPlayerSleep((ServerPlayer) (Object) this, pos, ci);
    }
}
