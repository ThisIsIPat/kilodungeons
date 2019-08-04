package me.phein.kiloplugins.mc.kilodungeons;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class KiloDungeonsPlugin extends JavaPlugin implements Listener {
    static int RarityMinimum = 30;
    static int DrownedRarity = 100;
    static int DrownedTreasureChance = 10;
    private static final String ConfigVersion = "1.1";

    static KiloDungeonsPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    private void createConfig(File configFile) {
        getConfig().set("ConfigVersion", ConfigVersion);
        getConfig().set("DrownedRarity", DrownedRarity);
        getConfig().set("DrownedTreasureChance", DrownedTreasureChance);

        try {
            getConfig().save(configFile);
        }
        catch (IOException ex) {
            getLogger().warning("Failed to save default config file...\n" + ex.toString());
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        boolean dataFolder;

        if(!getDataFolder().exists())dataFolder = getDataFolder().mkdirs();
        else dataFolder = true;

        if(!dataFolder) {
            getLogger().warning("Failed to Find or Create the Data Directory!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        else {
            File configFile = new File(getDataFolder().getPath() + "/config.yml");

            if(!configFile.exists()) {
                createConfig(configFile);
            }
            else {
                try {
                    getConfig().load(configFile);
                }
                catch (FileNotFoundException ex1) {
                    getLogger().warning("Failed to load config file: File does not exist!");
                }
                catch (IOException ex2) {
                    getLogger().warning("Failed to read config file!\n" + ex2.toString());
                }
                catch (InvalidConfigurationException ex3) {
                    getLogger().warning("Invalid config file!" + ex3.toString());
                }
                finally {
                    String confVer = getConfig().getString("ConfigVersion");

                    if(!confVer.equals(ConfigVersion)) {
                        createConfig(configFile);
                    }
                    else {
                        DrownedRarity = getConfig().getInt("DrownedRarity");
                        DrownedTreasureChance = getConfig().getInt("DrownedTreasureChance");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        event.getWorld().getPopulators().add(new DrownedDungeonPopulator());
    }
}
