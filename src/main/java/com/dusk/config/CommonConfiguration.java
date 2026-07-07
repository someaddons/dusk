package com.dusk.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CommonConfiguration implements ICommonConfig
{
    public int     nightSpawnMod                 = 30;
    public int     sleepDisableStartTime         = 10000;
    public int     sleepDisableEndTime           = 14000;
    public boolean enableSleepRestriction        = true;
    public int     baseMonsterCap                = 70;
    public boolean disableSleep                  = false;
    public int     nightSpawnMaxBlockLight       = 1;
    public double  nightAwarenessRangeMultiplier = 1.2;
    public boolean enableNightBlessings          = true;
    public int     blessingChancePercent         = 20;

    private final Random            random          = new Random();
    public        List<PotionEntry> potionEntryList = new ArrayList<>();

    public CommonConfiguration()
    {
        addPotionEntry(new PotionEntry(ResourceLocation.tryParse("minecraft:regeneration"), 1, 30, 5));
        addPotionEntry(new PotionEntry(ResourceLocation.tryParse("minecraft:resistance"), 1, 30, 5));
        addPotionEntry(new PotionEntry(ResourceLocation.tryParse("minecraft:absorption"), 1, 30, 5));
        addPotionEntry(new PotionEntry(ResourceLocation.tryParse("minecraft:glowing"), 1, 30, 5));
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
        entry4.addProperty("desc:", "Disables sleeping globally, default: false");
        entry4.addProperty("disableSleep", disableSleep);
        root.add("disableSleep", entry4);

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Percentage of how many more monster can appear at night, default: 30");
        entry.addProperty("nightSpawnMod", nightSpawnMod);
        root.add("nightSpawnMod", entry);

        final JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:", "Set the maximum light level at which mobs can spawn during night, affects only dimensions with a night cycle, default: 3 Vanilla: 0");
        entry5.addProperty("nightSpawnMaxBlockLight", nightSpawnMaxBlockLight);
        root.add("nightSpawnMaxBlockLight", entry5);

        final JsonObject entry6 = new JsonObject();
        entry6.addProperty("desc:", "Set the detection range multiplier for hostiles at night, default: 1.2 Vanilla: 1.0");
        entry6.addProperty("nightAwarenessRangeMultiplier", nightAwarenessRangeMultiplier);
        root.add("nightAwarenessRangeMultiplier", entry6);

        final JsonObject entry7 = new JsonObject();
        entry7.addProperty("desc:", "Enables mobs to receive a potion blessing at the start of combat during the night.");
        entry7.addProperty("enabled", enableNightBlessings);
        entry7.addProperty("blessingChancePercent", blessingChancePercent);

        final JsonArray potions = new JsonArray();
        final HashSet<ResourceLocation> used = new HashSet<>();
        for (final PotionEntry potionEntry : potionEntryList)
        {
            if (!used.contains(potionEntry.potionID))
            {
                used.add(potionEntry.potionID);
                final JsonObject singlePotion = new JsonObject();
                singlePotion.addProperty("potionID", potionEntry.potionID.toString());
                singlePotion.addProperty("level", potionEntry.level);
                singlePotion.addProperty("secondsDuration", potionEntry.secondsDuration);
                singlePotion.addProperty("weight", potionEntry.weight);
                potions.add(singlePotion);
            }
        }
        entry7.add("potions", potions);

        root.add("nightBlessings", entry7);

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
        nightSpawnMaxBlockLight = data.get("nightSpawnMaxBlockLight").getAsJsonObject().get("nightSpawnMaxBlockLight").getAsInt();
        nightAwarenessRangeMultiplier = data.get("nightAwarenessRangeMultiplier").getAsJsonObject().get("nightAwarenessRangeMultiplier").getAsDouble();

        blessingChancePercent = data.get("nightBlessings").getAsJsonObject().get("blessingChancePercent").getAsInt();
        enableNightBlessings = data.get("nightBlessings").getAsJsonObject().get("enabled").getAsBoolean();

        JsonArray potions = data.get("nightBlessings").getAsJsonObject().get("potions").getAsJsonArray();
        for (final JsonElement jsonElement : potions)
        {
            final ResourceLocation potionID = ResourceLocation.tryParse(jsonElement.getAsJsonObject().get("potionID").getAsString());
            final int level = Math.max(1, jsonElement.getAsJsonObject().get("level").getAsInt());
            final int secondsDuration = jsonElement.getAsJsonObject().get("secondsDuration").getAsInt();
            final int weight = Math.max(1, jsonElement.getAsJsonObject().get("weight").getAsInt());
            addPotionEntry(new PotionEntry(potionID, level, secondsDuration, weight));
        }
    }

    private void addPotionEntry(final PotionEntry regeneration)
    {
        for (int i = 0; i < regeneration.weight; i++)
        {
            potionEntryList.add(regeneration);
        }
    }

    public PotionEntry getRandomPotionEntry()
    {
        if (potionEntryList.isEmpty() || random.nextInt(100) >= blessingChancePercent)
        {
            return null;
        }

        return potionEntryList.get(random.nextInt(potionEntryList.size()));
    }
}
