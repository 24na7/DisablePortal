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

        // Add message defaults
        config.addDefault("messages.no-permission", "&cYou don't have permission to do that!");
        config.addDefault("messages.invalid-value", "&cInvalid value! Use true or false");
        config.addDefault("messages.unknown-portal", "&cUnknown portal type! Use nether or end");
        config.addDefault("messages.usage",
                        "&a/disableportal nether <true/false> &7- Enable/disable Nether portals\n" +
                        "&a/disableportal end <true/false> &7- Enable/disable End portals\n" +
                        "&a/disableportal status &7- Current status");
        config.addDefault("messages.status-title", "&6=== Current Settings ===");
        config.addDefault("messages.nether-portals", "&eNether portals: ");
        config.addDefault("messages.end-portals", "&eEnd portals: ");
        config.addDefault("messages.enabled", "&aEnabled");
        config.addDefault("messages.disabled", "&cDisabled");
        config.addDefault("messages.nether-enabled", "&aNether portals are now available");
        config.addDefault("messages.nether-disabled", "&aNether portals are now blocked");
        config.addDefault("messages.end-enabled", "&aEnd portals are now available");
        config.addDefault("messages.end-disabled", "&aEnd portals are now blocked");
        config.addDefault("messages.nether-create-disabled", "&cCreating Nether portals is disabled!");
        config.addDefault("messages.nether-use-disabled", "&cNether portals are disabled!");
        config.addDefault("messages.end-activate-disabled", "&cActivating End portals is disabled!");
        config.addDefault("messages.end-use-disabled", "&cEnd portals are disabled!");

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