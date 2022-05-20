package com.dusk.mixin;

import com.dusk.event.ISpawnGroupDataSetter;
import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnGroup.class)
public class SpawnGroupMixin implements ISpawnGroupDataSetter
{
    @Shadow
    @Final
    @Mutable
    private int capacity;

    @Override
    public void setMaxCap(final int max)
    {
        capacity = max;
    }
}
