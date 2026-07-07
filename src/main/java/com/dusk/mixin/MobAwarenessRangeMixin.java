package com.dusk.mixin;

import com.dusk.Dusk;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MobAwarenessRangeMixin extends TargetGoal
{
    @Shadow
    protected TargetingConditions targetConditions;

    public MobAwarenessRangeMixin(final Mob p_26140_, final boolean p_26141_)
    {
        super(p_26140_, p_26141_);
    }

    @Inject(method = "findTarget", at = @At("HEAD"))
    private void adjustSearchRange(final CallbackInfo ci)
    {
        if (mob instanceof Enemy && mob.level().isNight() && !mob.level().dimensionType().hasFixedTime())
        {
            targetConditions.range(getFollowDistance());
        }
    }

    @Override
    protected double getFollowDistance()
    {
        if (mob instanceof Enemy && mob.level().isNight() && !mob.level().dimensionType().hasFixedTime())
        {
            return super.getFollowDistance() * Dusk.config.getCommonConfig().nightAwarenessRangeMultiplier;
        }
        return super.getFollowDistance();
    }
}
