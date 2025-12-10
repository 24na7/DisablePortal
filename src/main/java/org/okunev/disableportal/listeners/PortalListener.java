package org.okunev.disableportal.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.okunev.disableportal.Disableportal;

import java.util.HashSet;
import java.util.Set;

public class PortalListener implements Listener {

    private final Disableportal plugin;
    private final Set<Material> portalBlocks = new HashSet<>();
    private final Set<Material> igniterItems = new HashSet<>();

    public PortalListener(Disableportal plugin) {
        this.plugin = plugin;

        portalBlocks.add(Material.NETHER_PORTAL);
        portalBlocks.add(Material.END_PORTAL);
        portalBlocks.add(Material.END_GATEWAY);

        igniterItems.add(Material.FLINT_AND_STEEL);
        igniterItems.add(Material.FIRE_CHARGE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE) {
            if (!plugin.getPortalManager().isNetherPortalEnabled()) {
                event.setCancelled(true);

                for (Player player : event.getWorld().getPlayers()) {
                    if (player.getLocation().distance(event.getBlocks().get(0).getLocation()) < 20) {
                        player.sendMessage(getMessage("messages.nether-create-disabled"));
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerIgnite(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (block == null || item == null) return;

        if (igniterItems.contains(item.getType())) {
            if (block.getType() == Material.OBSIDIAN) {
                if (isNetherPortalFrame(block)) {
                    if (!plugin.getPortalManager().isNetherPortalEnabled()) {
                        event.setCancelled(true);
                        player.sendMessage(getMessage("messages.nether-create-disabled"));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEndPortalActivate(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (block == null || item == null) return;

        if (block.getType() == Material.END_PORTAL_FRAME) {
            if (item.getType() == Material.ENDER_EYE) {
                EndPortalFrame frame = (EndPortalFrame) block.getBlockData();

                if (frame.hasEye()) return;

                if (!plugin.getPortalManager().isEndPortalEnabled()) {
                    event.setCancelled(true);
                    player.sendMessage(getMessage("messages.end-activate-disabled"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        if (event.getCause() == PlayerPortalEvent.TeleportCause.NETHER_PORTAL) {
            if (!plugin.getPortalManager().isNetherPortalEnabled()) {
                event.setCancelled(true);
                player.sendMessage(getMessage("messages.nether-use-disabled"));
            }
        } else if (event.getCause() == PlayerPortalEvent.TeleportCause.END_PORTAL) {
            if (!plugin.getPortalManager().isEndPortalEnabled()) {
                event.setCancelled(true);
                player.sendMessage(getMessage("messages.end-use-disabled"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLavaFlow(BlockFromToEvent event) {
        if (event.getBlock().getType() == Material.LAVA) {
            Block toBlock = event.getToBlock();

            if (canLavaCreatePortal(toBlock)) {
                if (!plugin.getPortalManager().isNetherPortalEnabled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalUse(PlayerInteractEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;

        if (portalBlocks.contains(block.getType())) {
            if (block.getType() == Material.NETHER_PORTAL) {
                if (!plugin.getPortalManager().isNetherPortalEnabled()) {
                    event.setCancelled(true);
                    player.sendMessage(getMessage("messages.nether-use-disabled"));
                }
            } else if (block.getType() == Material.END_PORTAL || block.getType() == Material.END_GATEWAY) {
                if (!plugin.getPortalManager().isEndPortalEnabled()) {
                    event.setCancelled(true);
                    player.sendMessage(getMessage("messages.end-use-disabled"));
                }
            }
        }
    }

    private String getMessage(String path) {
        String message = plugin.getConfig().getString(path, "");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private boolean isNetherPortalFrame(Block obsidianBlock) {
        int obsidianCount = 0;
        int airCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block check = obsidianBlock.getRelative(x, 0, z);
                if (check.getType() == Material.OBSIDIAN) {
                    obsidianCount++;
                } else if (check.getType() == Material.AIR || check.getType() == Material.FIRE) {
                    airCount++;
                }
            }
        }

        return obsidianCount >= 4 && airCount >= 3;
    }

    private boolean canLavaCreatePortal(Block block) {
        if (block.getType() != Material.AIR && block.getType() != Material.FIRE) {
            return false;
        }
        return checkObsidianFrame(block);
    }

    private boolean checkObsidianFrame(Block center) {
        if (checkVerticalFrame(center, true) || checkVerticalFrame(center, false)) {
            return true;
        }

        return false;
    }

    private boolean checkVerticalFrame(Block center, boolean isXAxis) {
        for (int h = -1; h <= 1; h++) {
            for (int w = -1; w <= 1; w++) {
                Block check;
                if (isXAxis) {
                    check = center.getRelative(w, h, 0);
                } else {
                    check = center.getRelative(0, h, w);
                }

                // Inner part should be air or fire
                if (Math.abs(w) <= 1 && Math.abs(h) <= 1) {
                    if (check.getType() != Material.AIR && check.getType() != Material.FIRE) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}