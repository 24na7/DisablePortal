package org.okunev.disableportal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.okunev.disableportal.commands.PortalCommand;
import org.okunev.disableportal.listeners.PortalListener;
import org.okunev.disableportal.manager.PortalManager;

public class Disableportal extends JavaPlugin {

    private static Disableportal instance;
    private PortalManager portalManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        portalManager = new PortalManager(this);
        getCommand("disableportal").setExecutor(new PortalCommand(this));

        Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
        Bukkit.getScheduler().runTaskTimer(this, this::checkExistingPortals, 0L, 20L);

        getLogger().info("disableportal enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("disableportal disabled!");
    }

    private void checkExistingPortals() {
    }

    public static Disableportal getInstance() {
        return instance;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }
}
