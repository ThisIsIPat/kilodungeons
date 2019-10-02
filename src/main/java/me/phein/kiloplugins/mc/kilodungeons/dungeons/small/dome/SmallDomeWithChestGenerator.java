package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Chest;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.util.noise.NoiseGenerator;

/**
 * Generic class to generate chests for loot in small domes
 *
 * @author IPat
 */
public abstract class SmallDomeWithChestGenerator extends SmallDomeGenerator {
    public SmallDomeWithChestGenerator(SmallDomePalette palette, double brokenRate, World world, int originX, int originZ, NoiseGenerator noiseGenerator, double treasureChance) {
        super(palette, brokenRate, world, originX, originZ, noiseGenerator, treasureChance);
    }

    public SmallDomeWithChestGenerator(SmallDomePalette palette, double brokenRate, World world, int originX, int originY, int originZ, NoiseGenerator noiseGenerator, double treasureChance) {
        super(palette, brokenRate, world, originX, originY, originZ, noiseGenerator, treasureChance);
    }

    @Override
    protected void generateLoot(World world, int x, int y, int z, BlockFace facing, boolean treasure) {
        Block block = world.getBlockAt(x, y, z);

        Chest chestBlockData = (Chest) Material.CHEST.createBlockData();
        chestBlockData.setFacing(facing);
        chestBlockData.setWaterlogged(isWaterlogged());
        block.setBlockData(chestBlockData);

        Lootable chest = (Lootable) block.getState();

        chest.setLootTable(treasure ? getGoodLootTable() : getBadLootTable());

        ((BlockState) chest).update();
    }

    protected abstract boolean isWaterlogged();
    protected abstract LootTable getBadLootTable();
    protected abstract LootTable getGoodLootTable();
}
