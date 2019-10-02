package me.phein.kiloplugins.mc.kilodungeons.command;

import me.phein.kiloplugins.mc.kilodungeons.event.KiloDungeonSpawnEvent;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class KiloDungeonsNotifierCommandExecutor implements CommandExecutor, Listener {

    private final Set<String> senderNames = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 0) {
                sender.sendMessage(ChatColor.GRAY + "Note: Turning on notifications for other players is not yet supported.\nIf this feature is desired, please tell us on Discord.");
            }
            if (!senderNames.contains(sender.getName())) {
                // TODO: Specify what kind of dungeon you want to be notified to
                sender.sendMessage(ChatColor.GRAY + "You will now be notified when a dungeon spawns.");
                senderNames.add(sender.getName());
            } else {
                sender.sendMessage(ChatColor.GRAY + "You will not be notified anymore when a dungeon spawns.");
                senderNames.remove(sender.getName());
            }
        } else {
            if (args.length != 0) {
                sender.sendMessage("Note: Turning on notifications for other players is not yet supported.\nIf this feature is desired, please tell us on Discord.");
            }
            sender.sendMessage("This command works in the client on players only.");
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDungeon(KiloDungeonSpawnEvent event) {
        for (String senderName : senderNames) {
            Player sender = Bukkit.getPlayerExact(senderName);

            if (sender == null) {
                continue;
            }

            sender.sendMessage(ChatColor.GRAY + "A dungeon \"" + ChatColor.BOLD + event.getDungeon() + ChatColor.GRAY + "\" spawned at the coordinates:");
            ComponentBuilder builder = new ComponentBuilder(ChatColor.GOLD + "x " + ChatColor.YELLOW + event.getDungeonLocation().getBlockX() + " | " +
                    ChatColor.GOLD + "y " + ChatColor.YELLOW + event.getDungeonLocation().getBlockY() + " | " +
                    ChatColor.GOLD + "z " + ChatColor.YELLOW + event.getDungeonLocation().getBlockZ());
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp " +
                    event.getTeleportLocation().getX() + " " +
                    event.getTeleportLocation().getY() + " " +
                    event.getTeleportLocation().getZ()));
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.GRAY + "Teleport to these coordinates?")}));

            sender.spigot().sendMessage(builder.create());
        }
    }
}
