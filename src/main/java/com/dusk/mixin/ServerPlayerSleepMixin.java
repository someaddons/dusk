package com.dusk.mixin;

import com.dusk.event.EventHandler;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerSleepMixin
{
    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    public void on(final BlockPos pos, final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir)
    {
        EventHandler.onPlayerSleep((ServerPlayerEntity)(Object)this ,pos, cir);
    }
}
