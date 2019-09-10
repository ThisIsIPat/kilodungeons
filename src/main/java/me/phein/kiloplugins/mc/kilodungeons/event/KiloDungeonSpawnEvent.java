package me.phein.kiloplugins.mc.kilodungeons.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KiloDungeonSpawnEvent extends Event {

    private final Location dungeonLocation;
    private final Location teleportLocation;

    public KiloDungeonSpawnEvent(Location dungeonLocation, Location teleportLocation) {
        this.dungeonLocation = dungeonLocation;
        this.teleportLocation = teleportLocation;
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
}
