package com.kylergray.respawncell.respawncell;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager {

    private final FileConfiguration config;

    /**
     * Creates a MessaageManager based upon the given configuration file
     * @param config the configuration file to read messages from
     */
    public MessageManager(FileConfiguration config) {
        this.config = config;
    }

    /**
     * Gets a message for the given path from the configuration file without the prefix but encoded
     * with colors
     * @param path the path to the message in the config
     * @return the encoded message with color
     */
    public String getMessageNoPrefix(String path) {
        String color = config.getString("messages.default-message-color");
        String message = config.getString("messages." + path);
        String fullMessage = color + message;
        return ChatColor.translateAlternateColorCodes('&', fullMessage);
    }

    /**
     * Gets a message for the given path from the configuration file with the prefix and encoded
     * with colors
     * @param path the path to the message in the config
     * @return the encoded message with color and a prefix
     */
    public String getMessage(String path) {
        String prefix = config.getString("messages.prefix");
        String color = config.getString("messages.default-message-color");
        String message = config.getString("messages." + path);
        String fullMessage = prefix + color + message;
        return ChatColor.translateAlternateColorCodes('&', fullMessage);
    }

}
