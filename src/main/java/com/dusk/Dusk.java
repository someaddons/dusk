package com.dusk;

import com.dusk.config.Configuration;
import com.dusk.event.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class Dusk implements ModInitializer
{
    public static final String MODID = "dusk";

    public static final Logger        LOGGER = LogManager.getLogger();
    public static        Configuration config;

    @Override
    public void onInitialize()
    {
        config = new Configuration();
        config.load();
        LOGGER.info("Dusk initialized");

        ServerTickEvents.END_WORLD_TICK.register(EventHandler::onWorldTick);
    }
}
