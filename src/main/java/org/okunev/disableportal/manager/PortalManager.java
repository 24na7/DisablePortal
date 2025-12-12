package org.okunev.disableportal.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.okunev.disableportal.Disableportal;

public class PortalManager {

    private final Disableportal plugin;
    private boolean netherPortalEnabled;
    private boolean endPortalEnabled;

    public PortalManager(Disableportal plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        config.addDefault("portals.nether.enabled", true);
        config.addDefault("portals.end.enabled", true);

        config.addDefault("locale.default", "en_us");
        config.addDefault("locale.auto-detect", true);

        config.options().copyDefaults(true);
        plugin.saveConfig();

        netherPortalEnabled = config.getBoolean("portals.nether.enabled", true);
        endPortalEnabled = config.getBoolean("portals.end.enabled", true);
    }

    public void saveConfig() {
        FileConfiguration config = plugin.getConfig();
        config.set("portals.nether.enabled", netherPortalEnabled);
        config.set("portals.end.enabled", endPortalEnabled);
        plugin.saveConfig();
    }

    public boolean isNetherPortalEnabled() {
        return netherPortalEnabled;
    }

    public void setNetherPortalEnabled(boolean enabled) {
        this.netherPortalEnabled = enabled;
        saveConfig();
    }

    public boolean isEndPortalEnabled() {
        return endPortalEnabled;
    }

    public void setEndPortalEnabled(boolean enabled) {
        this.endPortalEnabled = enabled;
        saveConfig();
    }
}