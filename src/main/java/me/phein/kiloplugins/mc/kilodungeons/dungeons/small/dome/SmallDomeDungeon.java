package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.dungeons.Dungeon;
import me.phein.kiloplugins.mc.kilodungeons.dungeons.DungeonCategory;
import org.bukkit.Location;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public enum SmallDomeDungeon implements Dungeon {
    OCEAN("Small Ocean Dome") {
        @Override
        public SmallDomeGenerator createGenerator(Location location, double treasureChance) {
            return new OceanSmallDomeGenerator(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                    new SimplexNoiseGenerator(location.getWorld().getSeed()), treasureChance);
        }
    },
    DESERT("Small Desert Dome") {
        @Override
        public SmallDomeGenerator createGenerator(Location location, double treasureChance) {
            return new DesertSmallDomeGenerator(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                    new SimplexNoiseGenerator(location.getWorld().getSeed()), treasureChance);
        }
    };

    private final String name;

    SmallDomeDungeon(String name) {
        this.name = name;
    }

    public DungeonCategory getCategory() {
        return DungeonCategory.SMALL_DOME;
    }

    @Override
    public String toString() {
        return name;
    }
}
