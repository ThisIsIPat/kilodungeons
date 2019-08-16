package me.phein.kiloplugins.mc.kilodungeons.config;

import me.phein.kiloplugins.mc.kilodungeons.KiloDungeonsPlugin;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.Config;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release.YmlConfig;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Supplies the newest version of Config
 */
public class ConfigManager {
    private final JavaPlugin plugin;

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
                    return new me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig(configFile);
                case "0.1":
                    return new me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release.YmlConfig(configFile);
                default:
                    return null;
            }
        } catch (ConfigException e) {
            throw new RuntimeException("Critical error occurred while attempting to load the config", e);
        }
    }
}
