package me.phein.kiloplugins.mc.kilodungeons.config;

import me.phein.kiloplugins.mc.kilodungeons.KiloDungeonsPlugin;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.Config;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release.YmlConfig;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigException;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigUnknownVersionException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Supplies the newest version of Config
 */
public class ConfigManager {
    private static final String NEWEST_VERSION = "0.1";
    private final JavaPlugin plugin;

    public ConfigManager(KiloDungeonsPlugin plugin) {
        this.plugin = plugin;
    }

    public Config getUpdatedConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        String version = plugin.getConfig().getString("version");
        try {
            while (version != null && !version.equals(NEWEST_VERSION)) {
                File backupFile = new File(plugin.getDataFolder(), "config-backup-" + version + ".yml");
                configFile.renameTo(backupFile);

                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (version) {
                    case "0.1a":
                        version = "0.1";

                        //plugin.saveResource("config.yml", true); // Disables fromAlpha method to work properly (can't set values in yml anymore) for unknown reasons

                        me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig alpha0_1config =
                                new me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig(backupFile);
                        me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release.YmlConfig.fromAlpha(configFile, alpha0_1config);
                        continue;
                    default:
                        throw new ConfigUnknownVersionException(configFile, version);
                }
            }
            return new YmlConfig(configFile);
        } catch (ConfigException e) {
            throw new RuntimeException("Critical error occurred while attempting to load the config", e);
        }
    }
}
