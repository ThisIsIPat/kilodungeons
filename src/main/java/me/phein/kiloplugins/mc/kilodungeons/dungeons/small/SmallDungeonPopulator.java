package me.phein.kiloplugins.mc.kilodungeons.dungeons.small;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 11x11 dome-like dungeon structure
 */
public abstract class SmallDungeonPopulator extends BlockPopulator {

    private SmallDungeonPalette palette;

    public SmallDungeonPopulator(SmallDungeonPalette palette) {
        this.palette = palette;
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        // TODO
    }
}
