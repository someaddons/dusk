package com.dusk.mixin;

import com.dusk.event.EventHandler;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerSleepMixin
{
    @Inject(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"), cancellable = true)
    public void on(
        final AbstractBedBlock bedBlock,
        final BlockState bedBlockState,
        final BedRule rule,
        final BlockPos pos,
        final CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir)
    {
        EventHandler.onPlayerSleep((ServerPlayer) (Object) this, cir, pos);
    }

    @Inject(method = "startSleeping", at = @At(value = "HEAD"), cancellable = true)
    public void forcomfortshardcoding(
        final BlockPos pos, final CallbackInfoReturnable<Boolean> cir)
    {
        EventHandler.onPlayerSleep((ServerPlayer) (Object) this, pos, cir);
    }
}
