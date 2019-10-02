package me.phein.kiloplugins.mc.kilodungeons.command;

import me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome.SmallDomeDungeon;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome.SmallDomeGenerator;
import me.phein.kiloplugins.mc.kilodungeons.util.conversation.InventoryOptionMenu;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Command to force dungeon generation at given coordinates.
 *
 * @author IPat
 */
public class KiloDungeonsGenerateCommandExecutor implements CommandExecutor {
    private final double treasureChance;

    public KiloDungeonsGenerateCommandExecutor(double treasureChance) {
        this.treasureChance = treasureChance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location location;

        if (args.length == 0) {
            location = dungeonLocationWithoutArguments(sender);
        } else if (args.length == 3) {
            if (sender instanceof Player) {
                location = dungeonLocationWithUnparsedArguments(((Player) sender).getWorld(), args[0], args[1], args[2]);
            } else {
                location = null;
            }
        } else if (args.length == 4) {
            location = dungeonLocationWithUnparsedArguments(args[0], args[1], args[2], args[3]);
        } else {
            location = null;
        }

        if (location == null) {
            sendErrorHelpMessage("Please provide the world name and coordinates the dungeon should spawn at.",
                    sender, label);
            return true;
        }

        new InventoryOptionMenu("dungeonCategory", "Choose a dungeon category")
                .with("Small Dome Dungeon", Material.IRON_NUGGET, e1 ->
                        new InventoryOptionMenu("smalldome", "Choose a dungeon")
                                .with("Desert", new String[]{"Small Dome Dungeon"}, Material.SAND,
                                        e2 -> handleDungeonGeneration(SmallDomeDungeon.DESERT.createGenerator(location, treasureChance), (Player) e2.getWhoClicked()))
                                .with("Ocean", new String[]{"Small Dome Dungeon"}, Material.TUBE_CORAL_BLOCK,
                                        e2 -> handleDungeonGeneration(SmallDomeDungeon.OCEAN.createGenerator(location, treasureChance), (Player) e2.getWhoClicked()))
                                .open((Player) e1.getWhoClicked()))
                .open((Player) sender);

        return true;
    }

    private void handleDungeonGeneration(SmallDomeGenerator generator, Player player) {
        if (!generator.generate()) {
            player.sendMessage("Dungeon was not generated! This is most likely to another plugin cancelling the generation.");
        } else {
            player.sendMessage("Dungeon generated successfully.");
        }
    }

    private Location dungeonLocationWithoutArguments(CommandSender sender) {
        if (sender instanceof Entity) {
            return ((Entity) sender).getLocation();
        } else {
            return null;
        }
    }

    private Location dungeonLocationWithUnparsedArguments(String worldName, String xArg, String yArg, String zArg) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            return null;
        } else {
            return dungeonLocationWithUnparsedArguments(world, xArg, yArg, zArg);
        }
    }

    private Location dungeonLocationWithUnparsedArguments(World world, String xArg, String yArg, String zArg) {
        double x, y, z;
        try {
            x = Double.parseDouble(xArg);
            y = Double.parseDouble(yArg);
            z = Double.parseDouble(zArg);
        } catch (NumberFormatException e) {
            return null;
        }

        return new Location(world, x, y, z);
    }

    private void sendErrorHelpMessage(String help, CommandSender commandSender, String commandLabel) {
        if (commandSender instanceof Entity) {
            commandSender.sendMessage(ChatColor.RED + help
                    + ChatColor.YELLOW + ChatColor.BOLD + " | "
                    + ChatColor.DARK_GRAY + "/" + commandLabel + ChatColor.GRAY + " world x y z");
        } else {
            commandSender.sendMessage(help);
            commandSender.sendMessage("/" + commandLabel + " world x y z");
        }
    }
}
