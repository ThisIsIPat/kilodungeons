package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

/**
 * 11x11 dome-like dungeon structure
 */
public abstract class SmallDomePopulator extends BlockPopulator {

    private final NoiseGenerator noiseGenerator;
    private final SmallDomePalette palette;

    private final double domeProbabilityPerChunk;

    public SmallDomePopulator(long seed, SmallDomePalette palette, double domeProbabilityPerChunk) {
        this.noiseGenerator = new SimplexNoiseGenerator(seed);
        this.palette = palette;

        this.domeProbabilityPerChunk = domeProbabilityPerChunk;
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

        int absX = chunkX + offsetX;
        int absZ = chunkZ + offsetZ;

        SmallDomeGenerator generator = createGenerator(world, absX, absZ);
        if (generator == null) return;

        generator.generate(); // TODO Notify of dungeon via KiloDungeonsNotifier
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
                return new OceanSmallDomeGenerator(world, x, z, noiseGenerator);
        }
    }
}
