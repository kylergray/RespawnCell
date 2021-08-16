package com.kylergray.respawncell.respawncell;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RespawnCell extends JavaPlugin {

    private RespawnCellManager jailManager;

    @Override
    public void onEnable() {
        DataManager data = new DataManager(this, "data.yml");
        MessageManager message = new MessageManager(this.getConfig());
        jailManager = new RespawnCellManager(
                this.getServer(),
                data,
                this.getConfig(),
                message,
                this);
        this.getCommand("respawncell")
                .setExecutor(new RespawnCellCommand(jailManager,
                        message,
                        this));
        this.getCommand("respawncell")
                .setTabCompleter(new RespawnCellTabCompletor(this.getServer()));
        this.saveDefaultConfig();
        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(this,
                        jailManager,
                        20L,
                        1200L);
        getServer().getPluginManager().registerEvents(new JailPlayerEvent(jailManager), this);
    }

    @Override
    public void onDisable() {
        jailManager.stopBossBar();
        jailManager.updateConfig();
        this.saveConfig();
    }
}
