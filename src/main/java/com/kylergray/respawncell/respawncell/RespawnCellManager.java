package com.kylergray.respawncell.respawncell;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RespawnCellManager implements Runnable {

    private DataManager data;
    private final FileConfiguration config;
    private final Server server;
    private final Map<String, Integer> jail;
    private final Map<String, BossBar> timers;
    private final Plugin plugin;
    private final MessageManager message;
    private static final int TIME_INTERVAL = 1;

    public RespawnCellManager(Server server,
                              DataManager data,
                              FileConfiguration config,
                              MessageManager message,
                              Plugin plugin) {
        this.data = data;
        this.config = config;
        this.server = server;
        this.plugin = plugin;
        this.message = message;
        jail = new HashMap<>();
        timers = new HashMap<>();
        List<String> jailedPlayers = data.getConfig().getStringList("jail.players");
        List<Integer> jailedTimes = data.getConfig().getIntegerList("jail.times");
        if (jailedPlayers.size() == jailedTimes.size()) {
            for (int i = 0; i < jailedPlayers.size(); i++) {
                jail.put(jailedPlayers.get(i), jailedTimes.get(i));
            }
        }
    }

    @Override
    public void run() {
        for (String player : jail.keySet()) {
            updateBossBar(player);
        }
        updateConfig(TIME_INTERVAL);
    }

    /**
     * Determines whether the given player is currently in jail
     * @param playerName the name of the player check if they are jailed
     * @return boolean value determining whether the player is in jail
     */
    public boolean isPlayerJailed(String playerName) {
        return getPlayerJailTime(playerName) != -1;
    }

    /**
     * Gets how much longer a player has in jail in minutes. If the player is not currently in
     * jail -1 will be returned
     * @param playerName the name of the player to get the jail time for
     * @return the integer value for the length of jail time in minutes
     */
    public int getPlayerJailTime(String playerName) {
        if (!jail.containsKey(playerName)) {
                return -1;
        }
        return jail.get(playerName);
    }

    /**
     * Puts the given player in jail for the specified amount of time. The player will be teleported
     * to jail if they are not already in jail. If the player is in jail, their time will be
     * updated to match
     * @param playerName the name of the player to put into jail
     * @param time the length of time in minutes the player should be in jail
     */
    public void setPlayerJail(String playerName, int time) {
        time = Math.min(time, 15);
        if (!isPlayerJailed(playerName)) {
            Location jailLocation = getLocationFromConfig("respawn");
            Player jailedPlayer = server.getPlayer(playerName);
            jailedPlayer.teleport(jailLocation);
            String jailMessage = message.getMessage("jailed-message");
            jailMessage = jailMessage.replace("{Time}", time + "");
            jailedPlayer.sendMessage(jailMessage);
        }
        jail.put(playerName, time);
        updateBossBar(playerName);
    }

    /**
     * Teleports the given player to jail if they are online
     * @param playerName the name of the player to teleport to jail
     */
    public void teleportToJail(String playerName) {
        Player player = server.getPlayer(playerName);
        if (player != null && isPlayerJailed(playerName)) {
            player.teleport(getLocationFromConfig("respawn"));
        }
    }

    /**
     * releases the given player from jail if they are in jail. This will remove their timer,
     * teleport them to the release point, and alert them they have been freed. If the player
     * is not online, they will be tracked and released once they re-connect
     * @param playerName the name of the player to free
     */
    public void releasePlayer(String playerName) {
        if (isPlayerJailed(playerName)) {
            jail.remove(playerName);
            timers.get(playerName).removeAll();
            timers.remove(playerName);
            Location releaseLocation = getLocationFromConfig("release");
            Player jailedPlayer = server.getPlayer(playerName);
            if (jailedPlayer == null) {
                List<String> offline = data.getConfig().getStringList("offline");
                offline.add(playerName);
                data.getConfig().set("offline", offline);
                data.saveConfig();
            } else {
                jailedPlayer.teleport(releaseLocation);
                jailedPlayer.sendMessage(message.getMessage("freed-message"));
            }
            updateConfig();
        }
    }

    /**
     * Releases a player who was offline when they were freed. If they are now online, they will
     * be teleported to the release location and freed
     * @param playerName the name of the player to free
     */
    public void releaseOfflinePlayer(String playerName) {
        List<String> offline = data.getConfig().getStringList("offline");
        if (offline.contains(playerName)) {
            Player player = server.getPlayer(playerName);
            if (player != null) {
                player.teleport(getLocationFromConfig("release"));
                player.sendMessage(message.getMessage("freed-message"));
                offline.remove(playerName);
                data.getConfig().set("offline", offline);
                data.saveConfig();
            }
        }
    }

    /**
     * Updates the location in the config file of the given location name to the current location
     * given
     * @param location the location to change the configuration file to save
     * @param path the name of the location to save the location under
     */
    public void updateLocation(Location location, String path) {
        data.getConfig().set("locations." + path + ".world", location.getWorld().getName());
        data.getConfig().set("locations." + path + ".x", location.getX());
        data.getConfig().set("locations." + path + ".y", location.getY());
        data.getConfig().set("locations." + path + ".z", location.getZ());
        data.getConfig().set("locations." + path + ".pitch", location.getPitch());
        data.getConfig().set("locations." + path + ".yaw", location.getYaw());
        data.saveConfig();
    }

    /**
     * Stops all boss bars from running, removing all players associated with them
     */
    public void stopBossBar() {
        for (String player : timers.keySet()) {
            BossBar bossBar = timers.get(player);
            bossBar.removeAll();
            timers.remove(player);
        }
    }

    /**
     * Gets the location for a given path from the config
     * @param path the path to the desired location in the configuration
     * @return the Location matching the data saved in the configuration file for the path given
     */
    public Location getLocationFromConfig(String path) {
        return new Location(
                server.getWorld(data.getConfig().getString("locations." + path + ".world")),
                data.getConfig().getDouble("locations." + path + ".x"),
                data.getConfig().getDouble("locations." + path + ".y"),
                data.getConfig().getDouble("locations." + path + ".z"),
                (float) data.getConfig().getDouble("locations." + path + ".yaw"),
                (float) data.getConfig().getDouble("locations." + path + ".pitch"));
    }

    /**
     * Updates the config with no minute changes
     */
    public void updateConfig() {
        updateConfig(0);
    }

    /**
     * Updates the data config progressing forward all jailed players time by the specified amount
     * @param minutes the amount to take off of player's jail time
     */
    private void updateConfig(int minutes) {
        List<String> jailedPlayers = new ArrayList<>();
        List<Integer> jailedTimes = new ArrayList<>();
        for (String player : jail.keySet()) {
            int time = jail.get(player);
            if (time <= 0) {
                releasePlayer(player);
            } else {
                jail.put(player, time - minutes);
            }
            jailedPlayers.add(player);
            jailedTimes.add(jail.get(player));
        }
        data.getConfig().set("jail.players", jailedPlayers);
        data.getConfig().set("jail.times", jailedTimes);
        data.saveConfig();
    }

    /**
     * Updates the boss bar of a player to the current amount of time on their sentence
     * @param player the player to update the boss bar for
     */
    private void updateBossBar(String player) {
        updateBossBar(player, jail.get(player));
    }

    /**
     * Reloads a players boss bar, removing an old instance and replacing it with a current one
     * in case the player has disconnected or the boss bar connection has been interrupted
     * @param player the player to update the boss bar for
     */
    public void refreshBossBar(String player) {
        if (timers.containsKey(player)) {
            BossBar bossBar = timers.get(player);
            bossBar.removeAll();
            Player p = server.getPlayer(player);
            if (p != null) {
                bossBar.addPlayer(p);
            }
        }
    }

    /**
     * Updates the boss bar of a player to the given amount of time on their sentence
     * @param player the player to update the boss bar for
     * @param time the amount of time to display on the boss bar
     */
    private void updateBossBar(String player, int time) {
        if (time > 0) {
            BossBar bossBar = timers.containsKey(player) ? timers.get(player) :
                    Bukkit.createBossBar(
                            "",
                            BarColor.RED,
                            BarStyle.SOLID);
            bossBar.setTitle(message.getMessageNoPrefix("bossbar-timer")
                    .replace("{Time}", time + ""));
            bossBar.setProgress(time / 15.0);
            Player p = server.getPlayer(player);
            if (p != null) {
                bossBar.addPlayer(p);
            }
            timers.put(player, bossBar);
        } else if (timers.containsKey(player)) {
            timers.get(player).removeAll();
        }
    }
}
