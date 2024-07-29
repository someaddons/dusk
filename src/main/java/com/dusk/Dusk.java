package com.dusk;

import com.cupboard.config.CupboardConfig;
import com.dusk.config.CommonConfiguration;
import com.dusk.event.EventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.dusk.Dusk.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Dusk
{
    public static final String MODID = "dusk";

    private static final Logger                              LOGGER = LogManager.getLogger();
    public static        CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());

    public Dusk(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(EventHandler.class);
    }
}
