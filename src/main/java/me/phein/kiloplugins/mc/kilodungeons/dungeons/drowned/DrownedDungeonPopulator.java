package me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned;

import me.phein.kiloplugins.mc.kilodungeons.config.v0_1a.Config;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.logging.Logger;

public class DrownedDungeonPopulator extends BlockPopulator {

    private DrownedDungeonGenerator dungeonGenerator;
    private Logger logger;
    private Config config;

    public DrownedDungeonPopulator(World world, Logger logger, Config config) {
        this.dungeonGenerator = new DrownedDungeonGenerator(world);
        this.logger = logger;
        this.config = config;
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        if (random.nextDouble() >= config.getDrownedChance()) {
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

        int absY = world.getHighestBlockYAt(absX, absZ) - 7; // 7 = minimal depth to spawn

        while (world.getBlockAt(absX, absY, absZ).getBlockData() instanceof Waterlogged || world.getBlockAt(absX, absY, absZ).getType() == Material.WATER) {
            absY--;
            if (absY <= 4) return;
        }
        absY -= 2; // A bit inside the ground

        logger.info("Drowned Dungeon spawning at: (" + absX + ", " + absY + ", " + absZ + ")...");
        dungeonGenerator.generate(absX, absY, absZ);
        logger.info("Drowned Dungeon spawned at: (" + absX + ", " + absY + ", " + absZ + ")");

        /*
        boolean cont = true;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = 0; z < 10; z++) {
                    if (world.isChunkGenerated((absX + x) / 16, (absZ + z) / 16)) {
                        Block block = world.getBlockAt(absX + x, absY + y, absZ + z);

                        // TODO: Use Noise to make holes in the walls.

                        if (x == 0 || x == 9 || y == 0 || y == 5 || z == 0 || z == 9)
                            block.setType(random.nextBoolean() ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);
                        else block.setType(Material.WATER);
                    } else {
                        cont = false;
                    }
                }
            }
        }

        for (int y = absY - 1; y > 0; y--) {
            int test = 0;
            if (world.getBlockAt(absX, y, absZ).getBlockData().getMaterial() != Material.WATER) {
                test++;
            } else {
                world.getBlockAt(absX, y, absZ).setType(random.nextBoolean() ? Material.MOSSY_COBBLESTONE_WALL : Material.COBBLESTONE_WALL);
            }
            if (world.getBlockAt(absX + 9, y, absZ).getBlockData().getMaterial() != Material.WATER) {
                test++;
            } else
                world.getBlockAt(absX + 9, y, absZ).setType(random.nextBoolean() ? Material.MOSSY_COBBLESTONE_WALL : Material.COBBLESTONE_WALL);
            if (world.getBlockAt(absX, y, absZ + 9).getBlockData().getMaterial() != Material.WATER) {
                test++;
            } else
                world.getBlockAt(absX, y, absZ + 9).setType(random.nextBoolean() ? Material.MOSSY_COBBLESTONE_WALL : Material.COBBLESTONE_WALL);
            if (world.getBlockAt(absX + 9, y, absZ + 9).getBlockData().getMaterial() != Material.WATER) {
                test++;
            } else
                world.getBlockAt(absX + 9, y, absZ + 9).setType(random.nextBoolean() ? Material.MOSSY_COBBLESTONE_WALL : Material.COBBLESTONE_WALL);
            world.getBlockAt(absX + 4, y, absZ + 4).setType(random.nextBoolean() ? Material.MOSSY_COBBLESTONE : Material.COBBLESTONE);
            if (test >= 4) break;
        }

        if (cont) {
            Block chest1 = world.getBlockAt(absX + 1, absY + 1, absZ + 2);
            Block chest2 = world.getBlockAt(absX + 1, absY + 1, absZ + 7);

            chest1.setType(Material.CHEST);
            chest2.setType(Material.CHEST);

            Chest chest1Data = (Chest) chest1.getState();
            Chest chest2Data = (Chest) chest2.getState();

            Directional chest1Rot = (Directional) chest1Data.getBlockData();
            Directional chest2Rot = (Directional) chest2Data.getBlockData();

            chest1Rot.setFacing(BlockFace.EAST);
            chest2Rot.setFacing(BlockFace.EAST);

            chest1Data.setBlockData(chest1Rot);
            chest2Data.setBlockData(chest2Rot);

            chest1Data.setLootTable(Bukkit.getLootTable((random.nextDouble() < config.getDrownedTreasureChance()) ? LootTables.SHIPWRECK_TREASURE.getKey() : LootTables.UNDERWATER_RUIN_SMALL.getKey()));
            chest2Data.setLootTable(Bukkit.getLootTable((random.nextDouble() < config.getDrownedTreasureChance()) ? LootTables.BURIED_TREASURE.getKey() : LootTables.UNDERWATER_RUIN_SMALL.getKey()));

            chest1Data.update();
            chest2Data.update();

            Block spawner;
            CreatureSpawner spawnerData;

            int type = random.nextInt(3);

            switch (type) {
                default:
                case 0:
                    spawner = world.getBlockAt(absX + 4, absY + 1, absZ + 4);
                    spawner.setType(Material.SPAWNER);
                    spawnerData = (CreatureSpawner) spawner.getState();
                    spawnerData.setSpawnedType(EntityType.DROWNED);
                    spawnerData.update();
                    break;
                case 1:
                    Block ironBars = world.getBlockAt(absX + 4, absY + 4, absZ + 4);
                    ironBars.setType(Material.IRON_BARS);
                    Waterlogged ironBarsWater = (Waterlogged) ironBars.getBlockData();
                    ironBarsWater.setWaterlogged(true);
                    BlockState ironBarsState = ironBars.getState();
                    ironBarsState.setBlockData(ironBarsWater);
                    ironBarsState.update();
                    spawner = world.getBlockAt(absX + 4, absY + 3, absZ + 4);
                    spawner.setType(Material.SPAWNER);
                    spawnerData = (CreatureSpawner) spawner.getState();
                    spawnerData.setSpawnedType(EntityType.DROWNED);
                    spawnerData.update();
                    break;
            }
        }*/
    }
}
