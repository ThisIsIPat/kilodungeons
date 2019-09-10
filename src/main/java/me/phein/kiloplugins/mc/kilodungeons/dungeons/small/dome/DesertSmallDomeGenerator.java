package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.util.Palette;
import me.phein.kiloplugins.mc.kilodungeons.util.RandomizerPalette;
import me.phein.kiloplugins.mc.kilodungeons.util.SingletonPalette;
import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.RandomizerBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.util.noise.NoiseGenerator;

public class DesertSmallDomeGenerator extends SmallDomeWithChestGenerator {

    private static final class DesertSmallDomePalette implements SmallDomePalette {
        private static final Palette<Material> AIR_BUBBLE = new SingletonPalette<>(Material.AIR);

        private static final Palette<Material> FLOOR_CENTER = new SingletonPalette<>(Material.RED_SANDSTONE);
        private static final Palette<Material> FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.CUT_RED_SANDSTONE, 1.5)
                .addOption(Material.RED_SANDSTONE, 1.7)
                .addOption(Material.RED_SAND, 0.6)
                .addOption(Material.SAND, 0.6)
                .addOption(Material.SANDSTONE, 1.7)
                .addOption(Material.CUT_SANDSTONE, 1.5)
                .build());
        private static final Palette<Material> INNER_BROKEN_FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.RED_SANDSTONE_SLAB, 0.3)
                .addOption(Material.CUT_RED_SANDSTONE_SLAB, 0.1)
                .build());
        private static final Palette<Material> OUTER_BROKEN_FLOOR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.RED_SANDSTONE, 0.7)
                .addOption(Material.CUT_RED_SANDSTONE, 0.7)
                .addOption(Material.RED_SANDSTONE_SLAB, 0.4)
                .addOption(Material.CUT_RED_SANDSTONE_SLAB, 0.4)
                .build());
        private static final Palette<Material> SPAWNER_POST = new SingletonPalette<>(Material.SANDSTONE_WALL);
        private static final Palette<EntityType> SPAWNER_TYPE = new SingletonPalette<>(EntityType.STRAY);
        private static final Palette<Material> PART_PILLAR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.SMOOTH_SANDSTONE_STAIRS, 2)
                .addOption(Material.SANDSTONE_STAIRS, 2.5)
                .addOption(Material.RED_SANDSTONE_STAIRS, 2.5)
                .build());
        private static final Palette<Material> SOLID_PILLAR_MATERIAL = new RandomizerPalette<>(new RandomizerBuilder<Material>()
                .addOption(Material.SANDSTONE, 2)
                .addOption(Material.RED_SANDSTONE, 1.5)
                .addOption(Material.CUT_SANDSTONE, 1.5)
                .build());
        private static final Palette<Material> CORNER_MATERIAL = new SingletonPalette<>(Material.AIR);
        private static final Palette<Material> CEILING_CENTER = new SingletonPalette<>(Material.BONE_BLOCK);
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

    public DesertSmallDomeGenerator(World world, int x, int z, NoiseGenerator noiseGenerator,
                                    double treasureChance) {
        super(new DesertSmallDomePalette(), 0.0,
                world, x, z, noiseGenerator,
                treasureChance);
    }

    @Override
    public boolean generate() {
        if (!super.generate()) return false;

        for (int offset = 1; offset >= 0; offset--) {
            generateMirrored(DesertSmallDomePalette.AIR_BUBBLE, 1, 1, offset, "bubble");
        }
        for (int y = 2; y <= 5; y++) {
            if (y != 2) {
                generateMirrored(DesertSmallDomePalette.AIR_BUBBLE, y, 0, 0, "bubble");
            }

            for (int offset = 1; offset >= 0; offset--) {
                generateMirrored(DesertSmallDomePalette.AIR_BUBBLE, y, 1, offset, "bubble");
            }

            if (y == 5) continue;

            for (int offset = 2; offset >= 0; offset--) {
                generateMirrored(DesertSmallDomePalette.AIR_BUBBLE, y, 2, offset, "bubble");
            }
        }

        generateMirrored(DesertSmallDomePalette.SOLID_PILLAR_MATERIAL, 5, 2, 2, "ceiling");

        return true;
    }

    @Override protected void generateCorners() {}   // Corners not needed

    @Override
    protected int calculateOriginY(World world) {
        int x = getOriginX();
        int z = getOriginZ();

        int y = world.getHighestBlockYAt(x, z);
        while (y > 5) {
            switch (world.getBlockAt(x, y, z).getType()) {
                default:
                    y--;
                    continue;
                case RED_SAND:
                case SAND:
                case RED_SANDSTONE:
                case SANDSTONE:
                case GRAVEL:
                case DIRT:
                case STONE:
                case ANDESITE:
                case DIORITE:
                case GRANITE:
                    return y - 5;
            }
        }

        return -1;
    }

    @Override protected boolean isWaterlogged() {
        return false;
    }
    @Override protected LootTable getBadLootTable() {
        return LootTables.VILLAGE_DESERT_HOUSE.getLootTable();
    }
    @Override protected LootTable getGoodLootTable() {
        return LootTables.DESERT_PYRAMID.getLootTable();
    }
}
