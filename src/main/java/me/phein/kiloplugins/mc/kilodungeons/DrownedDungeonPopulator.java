package me.phein.kiloplugins.mc.kilodungeons;

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

public class DrownedDungeonPopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        if(random.nextInt(Math.max(KiloDungeonsPlugin.DrownedRarity, KiloDungeonsPlugin.RarityMinimum)) == 0) {
            int offsetX, offsetZ, offsetY, type;

            offsetX = random.nextInt(16);
            offsetZ = random.nextInt(16);

            boolean generate = false;

            switch(world.getBiome(source.getX() * 16 + offsetX, source.getZ() * 16 + offsetZ)) {
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
                    generate = true;
                    break;
                default:
                    break;
            }

            if(generate) {
                type = random.nextInt(3);

                offsetY = random.nextInt(world.getHighestBlockYAt(source.getX() * 16 + offsetX, source.getZ() * 16 + offsetZ) - 7);

                int ox = source.getX() * 16 + offsetX, oz = source.getZ() * 16 + offsetZ;

                KiloDungeonsPlugin.instance.getLogger().info("Drowned Dungeon Spawned At: (" + ox +", " + offsetY + ", " + oz + ")");

                boolean cont = true;

                for(int x = 0; x < 10; x++) {
                    for(int y = 0; y < 6; y++) {
                        for(int z = 0; z < 10; z++) {
                            if(world.isChunkGenerated((ox + x) / 16, (oz + z) / 16)) {
                                Block block = world.getBlockAt(ox + x, offsetY + y, oz + z);

                                if(x == 0 || x == 9 || y == 0 || y == 5 || z == 0 || z == 9) block.setType(random.nextBoolean() ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);
                                else block.setType(Material.WATER);
                            }
                            else {
                                cont = false;
                            }
                        }
                    }
                }
                if(cont) {
                    Block chest1 = world.getBlockAt(ox + 1, offsetY + 1, oz + 2);
                    Block chest2 = world.getBlockAt(ox + 1, offsetY + 1, oz + 7);

                    chest1.setType(Material.CHEST);
                    chest2.setType(Material.CHEST);

                    Chest chest1Data = (Chest)chest1.getState();
                    Chest chest2Data = (Chest)chest2.getState();

                    Directional chest1Rot = (Directional)chest1Data.getBlockData();
                    Directional chest2Rot = (Directional)chest2Data.getBlockData();

                    chest1Rot.setFacing(BlockFace.EAST);
                    chest2Rot.setFacing(BlockFace.EAST);

                    chest1Data.setBlockData(chest1Rot);
                    chest2Data.setBlockData(chest2Rot);

                    chest1Data.setLootTable(Bukkit.getLootTable((random.nextInt(KiloDungeonsPlugin.DrownedTreasureChance) == 0) ? LootTables.SHIPWRECK_TREASURE.getKey() : LootTables.UNDERWATER_RUIN_SMALL.getKey()));
                    chest2Data.setLootTable(Bukkit.getLootTable((random.nextInt(KiloDungeonsPlugin.DrownedTreasureChance) == 0) ? LootTables.BURIED_TREASURE.getKey() : LootTables.UNDERWATER_RUIN_SMALL.getKey()));

                    chest1Data.update();
                    chest2Data.update();

                    Block spawner;
                    CreatureSpawner spawnerData;

                    switch (type) {
                        default:
                        case 0:
                            spawner = world.getBlockAt(ox + 4, offsetY + 3, oz + 4);
                            spawner.setType(Material.SPAWNER);
                            spawnerData = (CreatureSpawner)spawner.getState();
                            spawnerData.setSpawnedType(EntityType.DROWNED);
                            spawnerData.update();
                            break;
                        case 1:
                            Block ironBars = world.getBlockAt(ox + 4, offsetY + 4, oz + 4);
                            Waterlogged ironBarsWater = (Waterlogged)ironBars.getBlockData();
                            ironBarsWater.setWaterlogged(true);
                            BlockState ironBarsState = ironBars.getState();
                            ironBarsState.setBlockData(ironBarsWater);
                            ironBarsState.update();
                            spawner = world.getBlockAt(ox + 4, offsetY + 3, oz + 4);
                            spawner.setType(Material.SPAWNER);
                            spawnerData = (CreatureSpawner)spawner.getState();
                            spawnerData.setSpawnedType(EntityType.DROWNED);
                            spawnerData.update();
                            break;
                    }
                }
            }
        }
    }
}
