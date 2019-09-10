package me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned;

import me.phein.kiloplugins.mc.kilodungeons.command.KiloDungeonsNotifierCommandExecutor;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.Config;
import me.phein.kiloplugins.mc.kilodungeons.event.KiloDungeonSpawnEvent;
import org.bukkit.*;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;
import java.util.logging.Logger;

public class DrownedDungeonPopulator extends BlockPopulator {

    private final DrownedDungeonGenerator dungeonGenerator;
    private final Logger logger;
    private final Config config;
    private final KiloDungeonsNotifierCommandExecutor notifier;

    public DrownedDungeonPopulator(World world, Logger logger, Config config, KiloDungeonsNotifierCommandExecutor notifier) {
        this.dungeonGenerator = new DrownedDungeonGenerator(world, config.getDrownedDungeonTreasureChance());
        this.logger = logger;
        this.config = config;
        this.notifier = notifier;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        if (random.nextDouble() >= config.getDrownedDungeonSpawnChance()) {
            return;
        }

        int relX = random.nextInt(16);
        int relZ = random.nextInt(16);

        int absX = source.getX() * 16 + relX;
        int absZ = source.getZ() * 16 + relZ;

        switch (world.getBiome(absX, absZ)) {
            default:
                return;
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
        }

        int absY = world.getHighestBlockYAt(absX, absZ) - 10; // minimal depth to spawn

        while (world.getBlockAt(absX, absY, absZ).getType() == Material.WATER ||
                (world.getBlockAt(absX, absY, absZ).getBlockData() instanceof Waterlogged
                        && ((Waterlogged) world.getBlockAt(absX, absY, absZ).getBlockData()).isWaterlogged()) ||
                world.getBlockAt(absX, absY, absZ).getType() == Material.KELP ||
                world.getBlockAt(absX, absY, absZ).getType() == Material.KELP_PLANT) {
            absY--;
            if (absY <= 4) return;
        }

        dungeonGenerator.generate(absX, absY, absZ);

        Location dungeonLocation = new Location(world, absX, absY, absZ);
        Location teleportLocation = new Location(world, absX, absY + 3, absZ);

        Bukkit.getPluginManager().callEvent(new KiloDungeonSpawnEvent(dungeonLocation, teleportLocation));
        // notifier.notifyDungeonGeneration("drowned", absX, absY + 3, absZ);
    }
}
