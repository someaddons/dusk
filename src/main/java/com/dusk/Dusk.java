package com.dusk;

import com.dusk.config.Configuration;
import com.dusk.event.EventHandler;
import com.dusk.event.ModEventHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.dusk.Dusk.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Dusk {
    public static final String MODID = "dusk";

    private static final Logger LOGGER = LogManager.getLogger();
    public static Configuration config;

    public Dusk() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (c, b) -> true));
        config = new Configuration();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ModEventHandler.class);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Dusk initialized");
    }
}
