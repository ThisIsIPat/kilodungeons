package me.phein.kiloplugins.mc.kilodungeons.dungeons;

import me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome.SmallDomeGenerator;
import org.bukkit.Location;

/**
 * Marker / Tag interface for dungeons
 *
 * @author IPat
 */
public interface Dungeon {
    DungeonCategory getCategory();
    SmallDomeGenerator createGenerator(Location location, double treasureChance); // TODO Prepare for more dungeon types by abstracting Generators
}
