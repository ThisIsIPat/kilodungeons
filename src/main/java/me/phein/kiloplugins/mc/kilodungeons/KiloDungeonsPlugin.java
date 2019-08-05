package me.phein.kiloplugins.mc.kilodungeons;

import me.phein.kiloplugins.mc.kilodungeons.config.ConfigManager;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned.DrownedDungeonPopulator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;

public class KiloDungeonsPlugin extends JavaPlugin implements Listener {
    private ConfigManager configManager;

    @Override
    public void onLoad() {
        this.configManager = new ConfigManager(this);
    }

    // TODO: Instead provide default config.yml in resources and move this method to ConfigManager or Config
    /*private void createConfig(File configFile) {
        getConfig().set("ConfigVersion", CONFIG_VERSION);
        getConfig().set("DrownedRarity", DROWNED_RARITY);
        getConfig().set("DrownedTreasureChance", DROWNED_TREASURE_CHANCE);

        try {
            getConfig().save(configFile);
        }
        catch (IOException ex) {
            getLogger().warning("Failed to save default config file...\n" + ex.toString());
        }
    }*/

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        BlockPopulator drownedDungeonPopulator = new DrownedDungeonPopulator(event.getWorld(), this.getLogger(), configManager.getConfig());
        event.getWorld().getPopulators().add(drownedDungeonPopulator);
    }
}
