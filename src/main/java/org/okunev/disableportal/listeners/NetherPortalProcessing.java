package org.okunev.disableportal.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class NetherPortalProcessing extends JavaPlugin {

    public static boolean isPortalFrame(Block block) {
        if (checkPortalConfig(block, true, true) || checkPortalConfig(block, true, false)) {
            return true;
        }

        if (checkPortalConfig(block, false, true) || checkPortalConfig(block, false, false)) {
            return true;
        }

        return false;
    }

    private static boolean checkPortalConfig(Block center, boolean isXAxis, boolean isPositive) {
        int width = isXAxis ? 4 : 5;
        int height = isXAxis ? 5 : 4;

        Set<Block> corners = new HashSet<>();

        if (isXAxis) {
            if (isPositive) {
                corners.add(center.getRelative(1, 2, 0));
                corners.add(center.getRelative(-1, 2, 0));
                corners.add(center.getRelative(1, -2, 0));
                corners.add(center.getRelative(-1, -2, 0));
            } else {
                corners.add(center.getRelative(0, 2, 1));
                corners.add(center.getRelative(0, 2, -1));
                corners.add(center.getRelative(0, -2, 1));
                corners.add(center.getRelative(0, -2, -1));
            }
        } else {
            if (isPositive) {
                corners.add(center.getRelative(0, 2, 1));
                corners.add(center.getRelative(0, 2, -1));
                corners.add(center.getRelative(0, -2, 1));
                corners.add(center.getRelative(0, -2, -1));
            } else {
                corners.add(center.getRelative(1, 2, 0));
                corners.add(center.getRelative(-1, 2, 0));
                corners.add(center.getRelative(1, -2, 0));
                corners.add(center.getRelative(-1, -2, 0));
            }
        }

        for (Block corner : corners) {
            if (corner.getType() != Material.OBSIDIAN) {
                return false;
            }
        }

        return true;
    }
}
