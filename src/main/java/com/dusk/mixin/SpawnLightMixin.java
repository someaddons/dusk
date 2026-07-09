package com.dusk.mixin;

import com.dusk.Dusk;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Monster.class)
public class SpawnLightMixin
{
    @Redirect(method = "isDarkEnoughToSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;monsterSpawnBlockLightLimit()I"))
    private static int on(final DimensionType instance, ServerLevelAccessor serverLevel, BlockPos p_219011_, RandomSource p_219012_)
    {
        if (!serverLevel.dimensionType().hasFixedTime() && serverLevel.getLevel().getGameRules().get(GameRules.ADVANCE_TIME))
        {
            if (serverLevel.getLevel().isDarkOutside())
            {
                return Dusk.config.getCommonConfig().nightSpawnMaxBlockLight;
            }
        }

        return instance.monsterSpawnBlockLightLimit();
    }

    @Redirect(method = "isDarkEnoughToSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/valueproviders/IntProvider;sample(Lnet/minecraft/util/RandomSource;)I"))
    private static int on(final IntProvider instance, final RandomSource randomSource, ServerLevelAccessor serverLevel, BlockPos p_219011_, RandomSource p_219012_)
    {
        if (!serverLevel.dimensionType().hasFixedTime() && serverLevel.getLevel().getGameRules().get(GameRules.ADVANCE_TIME))
        {
            if (serverLevel.getLevel().isDarkOutside())
            {
                return instance.sample(randomSource) + Dusk.config.getCommonConfig().nightSpawnMaxBlockLight;
            }
        }

        return instance.sample(randomSource);
    }
}
