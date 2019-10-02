package me.phein.kiloplugins.mc.kilodungeons.event;

import me.phein.kiloplugins.mc.kilodungeons.dungeons.Dungeon;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KiloDungeonSpawnEvent extends Event implements Cancellable {

    private final Dungeon dungeon;
    private final Location dungeonLocation;
    private final Location teleportLocation;

    private boolean cancelled = false;

    public KiloDungeonSpawnEvent(Dungeon dungeon, Location dungeonLocation, Location teleportLocation) {
        this.dungeon = dungeon;
        this.dungeonLocation = dungeonLocation;
        this.teleportLocation = teleportLocation;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Location getDungeonLocation() {
        return dungeonLocation;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    // TODO: What kind of dungeon spawned?

    // Necessary code for native (Bukkit) events to function properly
    private static final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override public boolean isCancelled() {
        return cancelled;
    }
    @Override public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
