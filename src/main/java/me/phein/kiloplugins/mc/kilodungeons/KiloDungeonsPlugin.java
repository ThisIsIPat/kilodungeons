package me.phein.kiloplugins.mc.kilodungeons;

import me.phein.kiloplugins.mc.kilodungeons.command.KiloDungeonsCommandExecutor;
import me.phein.kiloplugins.mc.kilodungeons.command.KiloDungeonsNotifierCommandExecutor;
import me.phein.kiloplugins.mc.kilodungeons.config.ConfigManager;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.Config;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned.DrownedDungeonPopulator;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome.SmallDomePopulator;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class KiloDungeonsPlugin extends JavaPlugin implements Listener {

    private static KiloDungeonsPlugin INSTANCE;
    public static KiloDungeonsPlugin getInstance() {
        return INSTANCE;
    }

    private ConfigManager configManager;
    private final List<RuntimeException> exceptions = new LinkedList<>();

    private KiloDungeonsNotifierCommandExecutor notifierCommand = new KiloDungeonsNotifierCommandExecutor();

    @Override
    public void onLoad() {
        KiloDungeonsPlugin.INSTANCE = this;
        this.configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this);

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(notifierCommand, this);
        Bukkit.getPluginCommand("kilodungeons").setExecutor(new KiloDungeonsCommandExecutor(this));
        Bukkit.getPluginCommand("dungeonnotify").setExecutor(notifierCommand);
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        try {
            Config config = configManager.getUpdatedConfig();
            BlockPopulator drownedDungeonPopulator = new SmallDomePopulator(event.getWorld().getSeed(),
                    config.getDrownedDungeonSpawnChance(), config.getDrownedDungeonTreasureChance());
            event.getWorld().getPopulators().add(drownedDungeonPopulator);
        } catch (RuntimeException e) {
            exceptions.add(e);
            throw e;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!exceptions.isEmpty() && player.isOp()) {
            for (RuntimeException exception : exceptions) {
                Throwable exceptionIt = exception;
                while (exceptionIt != null) {
                    player.sendMessage(exceptionIt.getMessage());
                    exceptionIt = exceptionIt.getCause();
                }
            }
            player.sendMessage(ChatColor.RED + "KiloDungeons had some issues being activated properly." + ChatColor.YELLOW + " The errors that occured are shown above.");
            player.sendMessage("");
            player.sendMessage(ChatColor.YELLOW + "View the console for a detailed error report on startup. If you can't fix the errors, please type " + ChatColor.GOLD + ChatColor.BOLD + "/kdun" + ChatColor.YELLOW + " for support.");
            player.sendMessage(ChatColor.GRAY + "This message is only shown to operators on the server.");
        }
    }
}
