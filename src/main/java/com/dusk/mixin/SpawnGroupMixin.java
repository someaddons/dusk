package com.dusk.mixin;

import com.dusk.event.ISpawnGroupDataSetter;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobCategory.class)
public class SpawnGroupMixin implements ISpawnGroupDataSetter
{
    @Shadow
    @Final
    @Mutable
    private int max;

    @Override
    public void setMaxCap(final int max)
    {
        this.max = max;
    }
}
