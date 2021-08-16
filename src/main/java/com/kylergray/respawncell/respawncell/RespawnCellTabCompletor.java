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

    public RespawnCellTabCompletor(Server server) {
        this.server = server;
    }

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
