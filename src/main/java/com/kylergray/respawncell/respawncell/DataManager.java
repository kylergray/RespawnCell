package com.kylergray.respawncell.respawncell;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private Plugin plugin;
    private String dataPath;
    private FileConfiguration pluginData;
    private File dataFile;

    public DataManager(Plugin plugin, String configPath) {
        this.plugin = plugin;
        this.dataPath = configPath;
        pluginData = null;
        dataFile = null;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), dataPath);
        }
        this.pluginData = YamlConfiguration.loadConfiguration(dataFile);
        InputStream defaultStream = plugin.getResource(dataPath);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            pluginData.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (pluginData == null) {
            reloadConfig();
        }
        return pluginData;
    }

    public void saveConfig() {
        if (pluginData != null && dataFile != null) {
            try {
                getConfig().save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + dataFile, e);
            }
        }
    }

    public void saveDefaultConfig() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), dataPath);
        }
        if (!this.dataFile.exists()) {
            plugin.saveResource(dataPath, false);
        }
    }
}
