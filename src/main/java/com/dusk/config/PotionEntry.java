package com.dusk.config;

import net.minecraft.resources.ResourceLocation;

public class PotionEntry
{
    public final ResourceLocation potionID;
    public final int              level;
    public final int              secondsDuration;
    public final int              weight;

    public PotionEntry(final ResourceLocation potionID, final int level, final int secondsDuration, final int weight)
    {
        this.potionID = potionID;
        this.level = level;
        this.secondsDuration = secondsDuration;
        this.weight = weight;
    }
}
