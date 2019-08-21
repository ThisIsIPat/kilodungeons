package me.phein.kiloplugins.mc.kilodungeons.command;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class KiloDungeonsNotifierCommandExecutor implements CommandExecutor {

    private final Set<String> senderNames = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (!senderNames.contains(sender.getName())) {
                // TODO: Specify what kind of dungeon you want to be notified to
                sender.sendMessage(ChatColor.GRAY + "You will now be notified when a dungeon spawns.");
                senderNames.add(sender.getName());
            } else {
                sender.sendMessage(ChatColor.GRAY + "You will not be notified anymore when a dungeon spawns.");
                senderNames.remove(sender.getName());
            }
        } else {
            sender.sendMessage("This command works in the client on players only.");
        }
        return true;
    }

    public void notifyDungeonGeneration(String dungeonName, int x, int y, int z) {
        for (String senderName : senderNames) {
            Player sender = Bukkit.getPlayerExact(senderName);

            if (sender == null) {
                continue;
            }

            sender.sendMessage(ChatColor.GRAY + "A dungeon \"" + ChatColor.BOLD + dungeonName + ChatColor.GRAY + "\" spawned withSeed the coordinates:");
            ComponentBuilder builder = new ComponentBuilder(ChatColor.GOLD + "x " + ChatColor.YELLOW + x + "\n" +
                    ChatColor.GOLD + "y " + ChatColor.YELLOW + y + "\n" +
                    ChatColor.GOLD + "z " + ChatColor.YELLOW + z);
            builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/minecraft:tp " + x + " " + y + " " + z));
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click to tp to these coordinates!")}));

            sender.spigot().sendMessage(builder.create());
        }
    }
}
