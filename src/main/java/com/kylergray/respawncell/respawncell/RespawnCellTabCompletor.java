package com.kylergray.respawncell.respawncell;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RespawnCellTabCompletor implements TabCompleter {

    private Server server;

    /**
     * Creates a RespawnCellTabCompletor for the given server
     * @param server the server to initialize tab completion on
     */
    public RespawnCellTabCompletor(Server server) {
        this.server = server;
    }

    /**
     * Implements tab completion for respawncell commands
     * @param sender the sender of the command
     * @param command the command to tab complete for
     * @param alias alias commands for the command
     * @param args the arguments currently passed into the command
     * @return a List of Strings containing all phrases to auto-complete
     */
    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {
        List<String> options = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("respawncell") && args.length == 1) {
            options.add("jail");
            options.add("release");
            options.add("setspawn");
            options.add("setrelease");
        } else if (args.length == 2 &&
                (args[0].equalsIgnoreCase("release") ||
                args[0].equalsIgnoreCase("jail"))) {
            for (Player player : server.getOnlinePlayers()) {
                options.add(player.getPlayerListName());
            }
        } else if (args.length == 3 &&
                args[0].equalsIgnoreCase("jail")) {
            for (int i = 1; i <= 15; i++) {
                options.add(i + "");
            }
        }
        return options;
    }

}
