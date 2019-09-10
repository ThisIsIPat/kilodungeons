package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.util.Palette;
import me.phein.kiloplugins.mc.kilodungeons.util.RandomizerPalette;
import me.phein.kiloplugins.mc.kilodungeons.util.SingletonPalette;
import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.RandomizerBuilder;
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
        private static final Palette<Material> FLOOR_CENTER = new SingletonPalette<>(Material.POLISHED_GRANITE);
        private static final Palette<Material> FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.PRISMARINE_BRICKS, 1.7)
                .addOption(Material.PRISMARINE, 1.5)
                .addOption(Material.DARK_PRISMARINE_SLAB, 0.3)
                .build());
        private static final Palette<Material> INNER_BROKEN_FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.DARK_PRISMARINE_SLAB, 0.7)
                .addOption(Material.PRISMARINE_SLAB, 0.7)
                .build());
        private static final Palette<Material> OUTER_BROKEN_FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.DARK_PRISMARINE, 0.7)
                .addOption(Material.PRISMARINE, 0.7)
                .addOption(Material.DARK_PRISMARINE_SLAB, 0.4)
                .addOption(Material.PRISMARINE_SLAB, 0.4)
                .build());
        private static final Palette<Material> SPAWNER_POST = new SingletonPalette<>(Material.MOSSY_COBBLESTONE_WALL);
        private static final Palette<EntityType> SPAWNER_TYPE = new SingletonPalette<>(EntityType.DROWNED);
        private static final Palette<Material> PART_PILLAR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.PRISMARINE_BRICK_STAIRS, 2.5)
                .addOption(Material.PRISMARINE_STAIRS, 2)
                .build());
        private static final Palette<Material> SOLID_PILLAR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.PRISMARINE_BRICKS, 2)
                .addOption(Material.PRISMARINE, 1.5)
                .build());
        private static final Palette<Material> CORNER_MATERIAL = new SingletonPalette<>(Material.GLASS);
        private static final Palette<Material> CEILING_CENTER = new SingletonPalette<>(Material.MAGMA_BLOCK);
        @Override public Palette<Material> getFloorCenter() {
            return FLOOR_CENTER;
        }
        @Override public Palette<Material> getFloorMaterial() {
            return FLOOR_MATERIAL;
        }
        @Override public Palette<Material> getInnerBrokenFloorMaterial() {
            return INNER_BROKEN_FLOOR_MATERIAL;
        }
        @Override public Palette<Material> getOuterBrokenFloorMaterial() {
            return OUTER_BROKEN_FLOOR_MATERIAL;
        }
        @Override public Palette<Material> getSpawnerPost() {
            return SPAWNER_POST;
        }
        @Override public Palette<EntityType> getSpawnerType() {
            return SPAWNER_TYPE;
        }
        @Override public Palette<Material> getPartPillarMaterial() {
            return PART_PILLAR_MATERIAL;
        }
        @Override public Palette<Material> getSolidPillarMaterial() {
            return SOLID_PILLAR_MATERIAL;
        }
        @Override public Palette<Material> getCornerMaterial() {
            return CORNER_MATERIAL;
        }
        @Override public Palette<Material> getCeilingCenter() {
            return CEILING_CENTER;
        }
    }

    public OceanSmallDomeGenerator(World world, int x, int z, NoiseGenerator noiseGenerator,
                                   double treasureChance) {
        super(new OceanSmallDomePalette(), 0.4,
                world, x, z, noiseGenerator,
                treasureChance);
    }

    @Override
    protected int calculateOriginY(World world) {
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
