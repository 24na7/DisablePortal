package org.okunev.disableportal.api.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.okunev.disableportal.Disableportal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class VersionChecker implements Listener {

    private final Disableportal plugin;
    private String latestVersion;
    private String modrinthUrl = "https://modrinth.com/plugin/disableportal";
    private boolean updateAvailable = false;

    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/WKQBe2zm/version";

    public VersionChecker(Disableportal plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void checkVersion() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(MODRINTH_API);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "DisablePortal/" + plugin.getDescription().getVersion());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JsonArray versions = JsonParser.parseString(response.toString()).getAsJsonArray();
                    if (versions.size() > 0) {
                        JsonObject latest = versions.get(0).getAsJsonObject();
                        String version = latest.get("version_number").getAsString();

                        latestVersion = version;
                        String currentVersion = plugin.getDescription().getVersion();

                        if (!currentVersion.equalsIgnoreCase(version)) {
                            updateAvailable = true;

                            String message = plugin.getLocaleManager().getMessage("console.update-available")
                                    .replace("{version}", version)
                                    .replace("{your_version}", currentVersion)
                                    .replace("{modrinth_url}", modrinthUrl);

                            plugin.getLogger().warning(message);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("disableportal.admin") && updateAvailable) {
            plugin.getSchedulerAdapter().runTaskLater(() -> {
                String message = plugin.getLocaleManager().getMessage(player, "admin.update-available")
                        .replace("{version}", latestVersion)
                        .replace("{your_version}", plugin.getDescription().getVersion())
                        .replace("{modrinth_url}", modrinthUrl);

                player.sendMessage(message);
            }, 40L);
        }
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getModrinthUrl() {
        return modrinthUrl;
    }
}