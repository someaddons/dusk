package com.dusk.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration
{
    public final ForgeConfigSpec.ConfigValue<Integer> nightSpawnMod;
    public final ForgeConfigSpec.ConfigValue<Integer> minSleepTime;
    public final ForgeConfigSpec.ConfigValue<Boolean> disableSleep;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Dusk settings");
        builder.comment("Percentage of how many more monster can appear at night, default: 30");
        nightSpawnMod = builder.defineInRange("nightSpawnMod", 30, 1, 5000);

        builder.comment("Min time required to sleep, vanilla min time:12500, Midnight is 18000");
        minSleepTime = builder.defineInRange("minSleepTime", 14500, 1, 23000);

        builder.comment("Disable sleeping?, default: false");
        disableSleep = builder.define("disableSleep", false);

        // Escapes the current category level
        builder.pop();
    }
}
