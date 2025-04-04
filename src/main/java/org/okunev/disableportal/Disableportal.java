package org.okunev.disableportal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(Disableportal.MODID)
@Mod.EventBusSubscriber(modid = Disableportal.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Disableportal {

    public static final Logger LOGGER = LogManager.getLogger(Disableportal.class);
    public static final String MODID = "disableportal";
    private static final String PROTOCOL_VERSION = "1";
    private static int messageID = 0;
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue();

    public Disableportal() {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            // logic server
        }
    }
}