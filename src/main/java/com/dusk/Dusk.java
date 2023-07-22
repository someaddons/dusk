package com.dusk;

import com.cupboard.config.CupboardConfig;
import com.dusk.config.CommonConfiguration;
import com.dusk.event.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class Dusk implements ModInitializer
{
    public static final String MODID = "dusk";

    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());

    @Override
    public void onInitialize()
    {
        ServerTickEvents.END_WORLD_TICK.register(EventHandler::onWorldTick);
    }
}
