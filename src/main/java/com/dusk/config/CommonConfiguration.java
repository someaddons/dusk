package com.dusk.config;

import com.dusk.Dusk;
import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public int     nightSpawnMod  = 30;
    public int     minSleepTime   = 14500;
    public int     baseMonsterCap = 70;
    public boolean disableSleep   = false;

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Percentage of how many more monster can appear at night, default: 30");
        entry.addProperty("nightSpawnMod", nightSpawnMod);
        root.add("nightSpawnMod", entry);

        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:",
          "Base monster cap to act upon, this is the value it'll increase from/reduce to at the start/end of the night. Increase this to see more monsters in the world, default(Vanilla): 70");
        entry2.addProperty("baseMonsterCap", baseMonsterCap);
        root.add("baseMonsterCap", entry2);

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Min time required to sleep, default = 14500, vanilla default time:12500, Midnight is 18000. Max: 23000");
        entry3.addProperty("minSleepTime", minSleepTime);
        root.add("minSleepTime", entry3);

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Disables sleeping entirely, default: false");
        entry4.addProperty("disableSleep", disableSleep);
        root.add("disableSleep", entry4);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        if (data == null)
        {
            Dusk.LOGGER.error("Config file was empty!");
            return;
        }

        try
        {
            nightSpawnMod = data.get("nightSpawnMod").getAsJsonObject().get("nightSpawnMod").getAsInt();
            baseMonsterCap = data.get("baseMonsterCap").getAsJsonObject().get("baseMonsterCap").getAsInt();
            minSleepTime = data.get("minSleepTime").getAsJsonObject().get("minSleepTime").getAsInt();
            disableSleep = data.get("disableSleep").getAsJsonObject().get("disableSleep").getAsBoolean();
        }
        catch (Exception e)
        {
            Dusk.LOGGER.error("Could not parse config file", e);
        }
    }
}
