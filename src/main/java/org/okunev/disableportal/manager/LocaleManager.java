package org.okunev.disableportal.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.okunev.disableportal.Disableportal;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocaleManager {

    private final Disableportal plugin;
    private final Map<String, FileConfiguration> locales = new HashMap<>();
    private String defaultLocale;
    private final Map<String, String> playerLocales = new HashMap<>();

    public LocaleManager(Disableportal plugin) {
        this.plugin = plugin;
        loadLocales();
    }

    private void loadLocales() {
        File localeFolder = new File(plugin.getDataFolder(), "lang");
        if (!localeFolder.exists()) {
            localeFolder.mkdirs();
        }

        saveDefaultLocale("en_us.json");
        saveDefaultLocale("ru_ru.json");

        File[] localeFiles = localeFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (localeFiles != null) {
            for (File file : localeFiles) {
                String localeName = file.getName().replace(".json", "");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                locales.put(localeName, config);
            }
        }

        defaultLocale = plugin.getConfig().getString("locale.default", "en_us");
    }

    private void saveDefaultLocale(String fileName) {
        File localeFile = new File(plugin.getDataFolder(), "lang/" + fileName);
        if (!localeFile.exists()) {
            plugin.saveResource("lang/" + fileName, false);
        }
    }

    public String getMessage(Player player, String key) {
        String locale = getPlayerLocale(player);
        return getMessage(locale, key);
    }

    public String getMessage(String key) {
        return getMessage(defaultLocale, key);
    }

    public String getMessage(String locale, String key) {
        FileConfiguration config = locales.get(locale);
        if (config == null) {
            config = locales.get(defaultLocale);
        }

        if (config == null) {
            return key;
        }

        String message = config.getString(key);
        if (message == null) {
            if (!locale.equals(defaultLocale)) {
                return getMessage(defaultLocale, key);
            }
            return key;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getPlayerLocale(Player player) {
        return defaultLocale;
    }

    public void setPlayerLocale(Player player, String locale) {
        playerLocales.put(player.getName(), locale);
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String locale) {
        this.defaultLocale = locale;
        plugin.getConfig().set("locale.default", locale);
        plugin.saveConfig();
    }
}