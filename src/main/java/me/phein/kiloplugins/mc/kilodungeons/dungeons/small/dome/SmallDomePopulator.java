package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.event.KiloDungeonSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

/**
 * 11x11 dome-like dungeon structure
 */
public class SmallDomePopulator extends BlockPopulator {

    private final NoiseGenerator noiseGenerator;

    private final double domeProbabilityPerChunk;
    private final double treasureChance;

    public SmallDomePopulator(long seed, double domeProbabilityPerChunk, double treasureChance) {
        this.noiseGenerator = new SimplexNoiseGenerator(seed);

        this.domeProbabilityPerChunk = domeProbabilityPerChunk;
        this.treasureChance = treasureChance;
    }

    @Override
    public final void populate(World world, Random random, Chunk chunk) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        boolean domeInChunk = noiseGenerator.noise(chunkX, chunkZ) < domeProbabilityPerChunk;

        if (!domeInChunk) {
            return;
        }

        if (chunkX % 2 == 0 || chunkZ % 2 == 0) {
            boolean domeInAdjacentChunkA = noiseGenerator.noise(chunkX + 1, chunkZ) < domeProbabilityPerChunk;
            boolean domeInAdjacentChunkB = noiseGenerator.noise(chunkX - 1, chunkZ) < domeProbabilityPerChunk;
            boolean domeInAdjacentChunkC = noiseGenerator.noise(chunkX, chunkZ + 1) < domeProbabilityPerChunk;
            boolean domeInAdjacentChunkD = noiseGenerator.noise(chunkX, chunkZ - 1) < domeProbabilityPerChunk;

            if (domeInAdjacentChunkA || domeInAdjacentChunkB || domeInAdjacentChunkC || domeInAdjacentChunkD) {
                return;
            }
        }

        int offsetX = (int) (16 * noiseGenerator.noise(chunkX * 16));
        int offsetZ = (int) (16 * noiseGenerator.noise(chunkZ * 16));

        int absX = chunkX * 16 + offsetX;
        int absZ = chunkZ * 16 + offsetZ;

        SmallDomeGenerator generator = createGenerator(world, absX, absZ);
        if (generator == null || !generator.generate()) return;

        Location dungeonLocation = new Location(world, absX, generator.getOriginY(), absZ);
        Location teleportLocation = new Location(world, absX, generator.getOriginY() + 3, absZ);

        Bukkit.getPluginManager().callEvent(new KiloDungeonSpawnEvent(dungeonLocation, teleportLocation));
    }

    private SmallDomeGenerator createGenerator(World world, int x, int z) {
        switch (world.getBiome(x, z)) {
            default:
                return null;
            case OCEAN:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_OCEAN:
            case DEEP_FROZEN_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
            case DEEP_WARM_OCEAN:
            case FROZEN_OCEAN:
            case LUKEWARM_OCEAN:
            case WARM_OCEAN:
                return new OceanSmallDomeGenerator(world, x, z, noiseGenerator, treasureChance);
            case DESERT:
            case DESERT_HILLS:
            case DESERT_LAKES:/*
            case BADLANDS:
            case BADLANDS_PLATEAU:
            case ERODED_BADLANDS:
            case WOODED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:*/
                return new DesertSmallDomeGenerator(world, x, z, noiseGenerator, treasureChance);
        }
    }
}
