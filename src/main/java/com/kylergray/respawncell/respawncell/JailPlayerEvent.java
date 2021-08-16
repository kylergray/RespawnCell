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

    public JailPlayerEvent(RespawnCellManager jailManager) {
        this.jailManager = jailManager;
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int additionalTime = player.getStatistic(Statistic.PLAYER_KILLS);
        jailManager.setPlayerJail(player.getPlayerListName(), 5 + additionalTime);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        jailManager.releaseOfflinePlayer(player.getPlayerListName());
        jailManager.refreshBossBar(player.getPlayerListName());
        jailManager.teleportToJail(player.getPlayerListName());
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(jailManager.getLocationFromConfig("respawn"));
    }

}
