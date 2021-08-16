package com.kylergray.respawncell.respawncell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RespawnCellCommand implements CommandExecutor {

    private final RespawnCellManager jailManager;
    private final MessageManager message;
    private Plugin plugin;

    /**
     * Creates a command manager
     * @param jailManager the manager of the jail
     * @param message the message manager
     * @param plugin the plugin to connect the command manager to
     */
    public RespawnCellCommand(RespawnCellManager jailManager,
                              MessageManager message,
                              Plugin plugin) {
        this.jailManager = jailManager;
        this.message = message;
        this.plugin = plugin;
    }

    /**
     * Command manager for Respawn Cell
     * @param sender the command sender
     * @param command the command sent
     * @param label the label for the command
     * @param args the arguments associated with the command
     * @return whether to echo the command
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("respawncell.admin")) {
                if (args.length == 2 && args[0].equalsIgnoreCase("release")) {
                    jailManager.releasePlayer(args[1]);
                    p.sendMessage(message.getMessage("release-message")
                            .replace("{Player}", args[1]));
                } else if (args.length == 3 && args[0].equalsIgnoreCase("jail")) {
                    jailManager.setPlayerJail(args[1], Integer.parseInt(args[2]));
                    p.sendMessage(message.getMessage("jail-message")
                            .replace("{Player}", args[1]));
                } else if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
                    jailManager.updateLocation(p.getLocation(), "respawn");
                    p.sendMessage(message.getMessage("location-update-message")
                            .replace("{Location}", "Spawn"));
                } else if (args.length == 1 && args[0].equalsIgnoreCase("setrelease")) {
                    jailManager.updateLocation(p.getLocation(), "release");
                    p.sendMessage(message.getMessage("location-update-message")
                            .replace("{Location}", "Release"));
                } else {
                    p.sendMessage(message.getMessage("command-error"));
                }
            } else {
                p.sendMessage(message.getMessage("no-permission"));
            }
        }
        return true;
    }

}
