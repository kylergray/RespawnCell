package com.kylergray.respawncell.respawncell;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager {

    private final FileConfiguration config;

    public MessageManager(FileConfiguration config) {
        this.config = config;
    }

    public String getMessageNoPrefix(String path) {
        String color = config.getString("messages.default-message-color");
        String message = config.getString("messages." + path);
        String fullMessage = color + message;
        return ChatColor.translateAlternateColorCodes('&', fullMessage);
    }

    public String getMessage(String path) {
        String prefix = config.getString("messages.prefix");
        String color = config.getString("messages.default-message-color");
        String message = config.getString("messages." + path);
        String fullMessage = prefix + color + message;
        return ChatColor.translateAlternateColorCodes('&', fullMessage);
    }

}
