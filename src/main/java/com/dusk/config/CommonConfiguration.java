package com.dusk.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public int     nightSpawnMod          = 30;
    public int     sleepDisableStartTime  = 10000;
    public int     sleepDisableEndTime    = 14000;
    public boolean enableSleepRestriction = true;
    public int     baseMonsterCap         = 70;
    public boolean disableSleep           = false;

    public CommonConfiguration()
    {

    }

    @Override
    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:",
            "Sets a time-window in which sleeping is disabled. Normal vanilla sleep time start at 12500. By default this mod delays it to " + sleepDisableEndTime
                + " which allows some mobs to spawn."
                + " To understand minecraft time values check: https://minecraft.wiki/w/Daylight_cycle#Daytime");
        entry3.addProperty("enabled", enableSleepRestriction);
        entry3.addProperty("sleepDisableStartTime", sleepDisableStartTime);
        entry3.addProperty("sleepDisableEndTime", sleepDisableEndTime);
        root.add("sleepTime", entry3);

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Disable sleeping?, default: false");
        entry4.addProperty("disableSleep", disableSleep);
        root.add("disableSleep", entry4);

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Percentage of how many more monster can appear at night, default: 30");
        entry.addProperty("nightSpawnMod", nightSpawnMod);
        root.add("nightSpawnMod", entry);

        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:",
            "Base monster cap per player the nightSpawnMod modifier increases this until midnight after which it decreases again until morning. Increase this to see more monsters in the world or to match other mods changing the base, default(Vanilla): 70");
        entry2.addProperty("baseMonsterCap", baseMonsterCap);
        root.add("baseMonsterCap", entry2);

        return root;
    }

    @Override
    public void deserialize(final JsonObject data)
    {
        nightSpawnMod = data.get("nightSpawnMod").getAsJsonObject().get("nightSpawnMod").getAsInt();
        baseMonsterCap = data.get("baseMonsterCap").getAsJsonObject().get("baseMonsterCap").getAsInt();
        sleepDisableStartTime = data.get("sleepTime").getAsJsonObject().get("sleepDisableStartTime").getAsInt();
        sleepDisableEndTime = data.get("sleepTime").getAsJsonObject().get("sleepDisableEndTime").getAsInt();
        enableSleepRestriction = data.get("sleepTime").getAsJsonObject().get("enabled").getAsBoolean();
        disableSleep = data.get("disableSleep").getAsJsonObject().get("disableSleep").getAsBoolean();
    }
}
