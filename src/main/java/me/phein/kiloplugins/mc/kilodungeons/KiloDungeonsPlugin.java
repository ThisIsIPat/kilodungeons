package me.phein.kiloplugins.mc.kilodungeons;

import me.phein.kiloplugins.mc.kilodungeons.command.KiloDungeonsCommandExecutor;
import me.phein.kiloplugins.mc.kilodungeons.config.ConfigManager;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned.DrownedDungeonPopulator;
import org.bstats.bukkit.Metrics;
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

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this);

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginCommand("kilodungeons").setExecutor(new KiloDungeonsCommandExecutor());
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        BlockPopulator drownedDungeonPopulator = new DrownedDungeonPopulator(event.getWorld(), this.getLogger(), configManager.getUpdatedConfig());
        event.getWorld().getPopulators().add(drownedDungeonPopulator);
    }
}
