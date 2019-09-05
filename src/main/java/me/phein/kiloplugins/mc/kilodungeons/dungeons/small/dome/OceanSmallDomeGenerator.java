package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.util.Palette;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;
import org.bukkit.util.noise.NoiseGenerator;

public class OceanSmallDomeGenerator extends SmallDomeGenerator {

    private static final class OceanSmallDomePalette implements SmallDomePalette {
        @Override
        public Palette<Material> getFloorCenter() {
            return null;
        }
        @Override
        public Palette<Material> getFloorMaterial() {
            return null;
        }
        @Override
        public Palette<Material> getBrokenFloorMaterial() {
            return null;
        }
        @Override
        public Palette<Material> getSpawnerPost() {
            return null;
        }
        @Override
        public Palette<EntityType> getSpawnerType() {
            return null;
        }
        @Override
        public Palette<Material> getPartPillarMaterial() {
            return null;
        }
        @Override
        public Palette<Material> getSolidPillarMaterial() {
            return null;
        }
        @Override
        public Palette<Material> getCornerMaterial() {
            return null;
        }
        @Override
        public Palette<Material> getCeilingCenter() {
            return null;
        }
    }

    public OceanSmallDomeGenerator(World world, int x, int z, NoiseGenerator noiseGenerator, double treasureChance) {
        super(new OceanSmallDomePalette(), world, x, z, noiseGenerator, treasureChance);


    }

    @Override
    public int getOriginY(World world) {
        int x = getOriginX();
        int z = getOriginZ();

        int y = world.getHighestBlockYAt(x, z);
        while (y > 0) {
            switch (world.getBlockAt(x, y, z).getType()) {
                default:
                    y--;
                    continue;
                case SAND:
                case GRAVEL:
                case DIRT:
                case STONE:
                    return y;
            }
        }

        return -1;
    }

    @Override
    protected void generateLoot(World world, int x, int y, int z, BlockFace facing, boolean treasure) {
        Block block = world.getBlockAt(x, y, z);

        Chest chestBlockData = (Chest) Material.CHEST.createBlockData();
        chestBlockData.setFacing(facing);
        chestBlockData.setWaterlogged(true);
        block.setBlockData(chestBlockData);

        Lootable chest = (Lootable) block.getState();

        chest.setLootTable(treasure ? LootTables.SHIPWRECK_TREASURE.getLootTable() : LootTables.UNDERWATER_RUIN_SMALL.getLootTable());

        ((BlockState) chest).update();
    }
}
