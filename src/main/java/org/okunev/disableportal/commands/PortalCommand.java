package org.okunev.disableportal.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.okunev.disableportal.Disableportal;
import org.okunev.disableportal.manager.PortalManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortalCommand implements CommandExecutor, TabCompleter {

    private final Disableportal plugin;

    public PortalCommand(Disableportal plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PortalManager manager = plugin.getPortalManager();

        if (args.length == 0) {
            sendMessage(sender, "messages.usage");
            return true;
        }

        if (!sender.hasPermission("disableportal.admin")) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            sendMessage(sender, "messages.status-title");
            sender.sendMessage(getMessage(sender, "messages.nether-portals") +
                    (manager.isNetherPortalEnabled() ?
                            getMessage(sender, "messages.enabled") :
                            getMessage(sender, "messages.disabled")));
            sender.sendMessage(getMessage(sender, "messages.end-portals") +
                    (manager.isEndPortalEnabled() ?
                            getMessage(sender, "messages.enabled") :
                            getMessage(sender, "messages.disabled")));
            return true;
        }

        if (args[0].equalsIgnoreCase("locale")) {
            if (args.length < 2) {
                sendMessage(sender, "messages.locale-usage");
                return true;
            }

            String locale = args[1].toLowerCase();
            if (locale.equals("en_us") || locale.equals("ru_ru")) {
                plugin.getLocaleManager().setDefaultLocale(locale);
                sendMessage(sender, "messages.locale-changed");
            } else {
                sendMessage(sender, "messages.locale-invalid");
            }
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /disableportal <nether/end> <true/false>");
            return true;
        }

        boolean enabled;
        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equals("1")) {
            enabled = true;
        } else if (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") || args[1].equals("0")) {
            enabled = false;
        } else {
            sendMessage(sender, "messages.invalid-value");
            return true;
        }

        if (args[0].equalsIgnoreCase("nether")) {
            manager.setNetherPortalEnabled(enabled);
            sendMessage(sender, enabled ? "messages.nether-enabled" : "messages.nether-disabled");
        } else if (args[0].equalsIgnoreCase("end")) {
            manager.setEndPortalEnabled(enabled);
            sendMessage(sender, enabled ? "messages.end-enabled" : "messages.end-disabled");
        } else {
            sendMessage(sender, "messages.unknown-portal");
        }

        return true;
    }

    private void sendMessage(CommandSender sender, String path) {
        String message;
        if (sender instanceof Player) {
            message = plugin.getLocaleManager().getMessage((Player) sender, path);
        } else {
            message = plugin.getLocaleManager().getMessage(path);
        }

        if (!message.isEmpty()) {
            sender.sendMessage(message);
        }
    }

    private String getMessage(CommandSender sender, String path) {
        if (sender instanceof Player) {
            return plugin.getLocaleManager().getMessage((Player) sender, path);
        } else {
            return plugin.getLocaleManager().getMessage(path);
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> options = Arrays.asList("nether", "end", "status", "locale");
            for (String option : options) {
                if (option.startsWith(args[0].toLowerCase())) {
                    completions.add(option);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("nether") || args[0].equalsIgnoreCase("end")) {
                List<String> options = Arrays.asList("true", "false", "on", "off");
                for (String option : options) {
                    if (option.startsWith(args[1].toLowerCase())) {
                        completions.add(option);
                    }
                }
            } else if (args[0].equalsIgnoreCase("locale")) {
                List<String> options = Arrays.asList("en_us", "ru_ru");
                for (String option : options) {
                    if (option.startsWith(args[1].toLowerCase())) {
                        completions.add(option);
                    }
                }
            }
        }

        return completions;
    }
}