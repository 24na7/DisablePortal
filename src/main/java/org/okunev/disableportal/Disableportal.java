package org.okunev.disableportal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.okunev.disableportal.commands.PortalCommand;
import org.okunev.disableportal.listeners.PortalListener;
import org.okunev.disableportal.manager.LocaleManager;
import org.okunev.disableportal.manager.PortalManager;
import org.okunev.disableportal.api.FoliaCore.FoliaScheduler;
import org.okunev.disableportal.api.utils.SchedulerAdapter;
import org.okunev.disableportal.api.utils.VersionChecker;

public class Disableportal extends JavaPlugin {

    private static Disableportal instance;
    private PortalManager portalManager;
    private LocaleManager localeManager;
    private SchedulerAdapter schedulerAdapter;
    private VersionChecker versionChecker;
    private boolean isFolia;

    @Override
    public void onEnable() {
        instance = this;

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
            getLogger().info("Folia detected! Using Folia scheduler.");
        } catch (ClassNotFoundException e) {
            isFolia = false;
            getLogger().info("Running on standard Bukkit/Paper.");
        }

        schedulerAdapter = new FoliaScheduler(this, isFolia);

        saveDefaultConfig();
        localeManager = new LocaleManager(this);
        portalManager = new PortalManager(this);

        versionChecker = new VersionChecker(this);

        getCommand("disableportal").setExecutor(new PortalCommand(this));

        Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);

        schedulerAdapter.runTaskTimer(this::checkExistingPortals, 1L, 20L);

        schedulerAdapter.runAsyncTask(() -> {
            versionChecker.checkVersion();

            if (versionChecker.isUpdateAvailable()) {
                String message = localeManager.getMessage("console.update-available")
                        .replace("{version}", versionChecker.getLatestVersion())
                        .replace("{your_version}", getDescription().getVersion())
                        .replace("{modrinth_url}", versionChecker.getModrinthUrl());
                getLogger().warning(message);
            }
        });

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

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public SchedulerAdapter getSchedulerAdapter() {
        return schedulerAdapter;
    }

    public VersionChecker getVersionChecker() {
        return versionChecker;
    }

    public boolean isFolia() {
        return isFolia;
    }
}