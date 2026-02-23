package org.okunev.disableportal.api.FoliaCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.okunev.disableportal.api.utils.SchedulerAdapter;

public class FoliaScheduler implements SchedulerAdapter {

    private final JavaPlugin plugin;
    private final boolean isFolia;

    public FoliaScheduler(JavaPlugin plugin, boolean isFolia) {
        this.plugin = plugin;
        this.isFolia = isFolia;
    }

    @Override
    public void runAsyncTask(Runnable task) {
        if (isFolia) {
            plugin.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    @Override
    public void runTask(Runnable task) {
        if (isFolia) {
            plugin.getServer().getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    @Override
    public void runTaskLater(Runnable task, long delayTicks) {
        if (isFolia) {
            plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    @Override
    public void runTaskTimer(Runnable task, long delayTicks, long periodTicks) {
        if (isFolia) {
            long safeDelay = (delayTicks <= 0) ? 1 : delayTicks;
            plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                    plugin,
                    scheduledTask -> task.run(),
                    safeDelay,
                    periodTicks
            );
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
        }
    }

    @Override
    public void runTaskAtLocation(Location location, Runnable task) {
        if (isFolia) {
            plugin.getServer().getRegionScheduler().run(plugin, location, scheduledTask -> task.run());
        } else {
            runTask(task);
        }
    }

    @Override
    public void runTaskForEntity(Entity entity, Runnable task) {
        if (isFolia) {
            entity.getScheduler().run(plugin, scheduledTask -> task.run(), null);
        } else {
            runTask(task);
        }
    }
}
