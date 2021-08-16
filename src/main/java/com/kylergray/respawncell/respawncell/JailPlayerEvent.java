package com.kylergray.respawncell.respawncell;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class JailPlayerEvent implements Listener {

    private final RespawnCellManager jailManager;

    /**
     * Creates a JailPlayerEvent manager that uses the given RespawnCellManager to manage jail
     * @param jailManager the manager of the desired jail
     */
    public JailPlayerEvent(RespawnCellManager jailManager) {
        this.jailManager = jailManager;
    }

    /**
     * Handles player death event, adding them to jail for the desired length of time
     * @param event the event
     */
    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int additionalTime = player.getStatistic(Statistic.PLAYER_KILLS);
        jailManager.setPlayerJail(player.getPlayerListName(), 5 + additionalTime);
    }

    /**
     * Handles player join event, teleporting them to jail if necessary, refreshing their boss bar,
     * and releasing them from prison if they need to be
     * @param event the event
     */
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        jailManager.releaseOfflinePlayer(player.getPlayerListName());
        jailManager.refreshBossBar(player.getPlayerListName());
        jailManager.teleportToJail(player.getPlayerListName());
    }

    /**
     * Handles player respawn event, teleporting them to jail if necessary
     * @param event the event
     */
    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(jailManager.getLocationFromConfig("respawn"));
    }

}
