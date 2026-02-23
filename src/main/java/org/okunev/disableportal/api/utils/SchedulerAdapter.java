package org.okunev.disableportal.api.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface SchedulerAdapter {

    void runAsyncTask(Runnable task);
    void runTask(Runnable task);
    void runTaskLater(Runnable task, long delayTicks);
    void runTaskTimer(Runnable task, long delayTicks, long periodTicks);
    void runTaskAtLocation(Location location, Runnable task);
    void runTaskForEntity(Entity entity, Runnable task);
}