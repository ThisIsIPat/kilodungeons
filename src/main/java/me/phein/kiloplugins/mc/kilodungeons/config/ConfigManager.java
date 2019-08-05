package me.phein.kiloplugins.mc.kilodungeons.config;

import me.phein.kiloplugins.mc.kilodungeons.KiloDungeonsPlugin;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1a.Config;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1a.YmlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Supplies the newest version of Config
 */
public class ConfigManager {
    private JavaPlugin plugin;

    public ConfigManager(KiloDungeonsPlugin plugin) {
        this.plugin = plugin;
    }

    public Config getConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        String version = plugin.getConfig().getString("version");

        try {
            if (version == null) {
                return new YmlConfig(configFile); // Always replace with newest version
            }
            // TODO Handle version conversions
            switch (version) {
                case "0.1a":
                    return new YmlConfig(configFile);
                case "1.0":
                    return null; // TODO for release version
                default:
                    return null;
            }
        } catch (IOException e) {
            // TODO: Proper exception handling
            e.printStackTrace();
            return null;
        }
    }
}
