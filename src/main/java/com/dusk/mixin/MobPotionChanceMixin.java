package com.dusk.mixin;

import com.cupboard.util.RegistryLookup;
import com.dusk.Dusk;
import com.dusk.config.PotionEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MobPotionChanceMixin extends TargetGoal
{
    @Shadow
    protected LivingEntity target;

    public MobPotionChanceMixin(final Mob p_26140_, final boolean p_26141_)
    {
        super(p_26140_, p_26141_);
    }

    // Potion data: level, duration, weight, add "none" as empty weight
    @Inject(method = "start", at = @At("HEAD"))
    private void rollPotionOnCombatStart(final CallbackInfo ci)
    {
        // Only apply potion effect if it doesnt exist
        if (target instanceof ServerPlayer && Dusk.config.getCommonConfig().enableNightBlessings && target.level().isNight())
        {
            final PotionEntry chosen = Dusk.config.getCommonConfig().getRandomPotionEntry();
            if (chosen == null)
            {
                return;
            }

            final Holder<MobEffect> effect = RegistryLookup.getHolder(target.level(), Registries.MOB_EFFECT, chosen.potionID);
            if (effect != null)
            {
                mob.addEffect(new MobEffectInstance(effect.value(), chosen.secondsDuration * 20, chosen.level - 1));
            }
        }
    }
}
