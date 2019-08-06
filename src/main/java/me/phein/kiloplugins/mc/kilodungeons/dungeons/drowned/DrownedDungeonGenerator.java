package me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned;

import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.Randomizer;
import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.RandomizerBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class DrownedDungeonGenerator {
    private final World world;
    private NoiseGenerator noiseGen;
    private Randomizer<Material> brokenWallPalette = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE_BRICKS, 0.5)
            .addOption(Material.PRISMARINE_BRICK_STAIRS, 2)
            .addOption(Material.PRISMARINE, 0.5)
            .addOption(Material.PRISMARINE_STAIRS, 2.5)
            .build();
    private Randomizer<Material> solidWallPalette = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE_BRICKS, 2)
            .addOption(Material.PRISMARINE, 1.5)
            .build();
    private Randomizer<Material> floorPalette = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE_BRICKS, 1.7)
            .addOption(Material.PRISMARINE, 1.5)
            .addOption(Material.DARK_PRISMARINE_SLAB, 0.3)
            .build();
    private Randomizer<Material> blocksAndStairs = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE, 3)
            .addOption(Material.PRISMARINE_SLAB, 1)
            .addOption(Material.PRISMARINE_BRICKS, 2)
            .addOption(Material.PRISMARINE_BRICK_SLAB, 1)
            .build();

    public DrownedDungeonGenerator(World world) {
        this.world = world;
        this.noiseGen = new SimplexNoiseGenerator(world);
    }

    public void generate(int originX, int originY, int originZ) {
        originY += 5; // TODO TEMPORARY

        generateFloor(originX, originY, originZ);
        for (int radiusIt = 0; radiusIt <= 1; radiusIt++) {
            int radius = 4 + radiusIt;
            int wallHeight = 5 - radiusIt;

            for (int sideOffset = 1; sideOffset >= -1; sideOffset--) {
                generateWall(BlockFace.WEST, originX + radius, originY, originZ + sideOffset, wallHeight);
                generateWall(BlockFace.EAST, originX - radius, originY, originZ + sideOffset, wallHeight);
                generateWall(BlockFace.NORTH, originX + sideOffset, originY, originZ + radius, wallHeight);
                generateWall(BlockFace.SOUTH, originX + sideOffset, originY, originZ - radius, wallHeight);
            }
        }
    }

    private void generateWall(BlockFace brokenFacing, int originX, int originY, int originZ, int maxHeight) {
        int height = noiseGen.noise(originX, originZ) < 0.3 ? maxHeight : maxHeight - 1;    // Warning: Noise = {-1 ; 1}

        for (int relY = 1; relY <= height; relY++) {
            generateBlockAt(solidWallPalette, originX, originY + relY, originZ);
        }
        generateBlockAt(brokenWallPalette, originX, originY + height, originZ, brokenFacing);
    }

    private void generateFloor(int originX, int originY, int originZ) {
        // FLOOR - center
        world.getBlockAt(originX, originY, originZ).setType(Material.SEA_LANTERN);

        // FLOOR - x + z axis (Fat plus-shape)
        for (int relHorIt = 5; relHorIt > 0; relHorIt--) {
            for (int sideOffset = 1; sideOffset >= -1; sideOffset--) {
                generateBlockAt(floorPalette, originX + relHorIt, originY, originZ + sideOffset);
                generateBlockAt(floorPalette, originX - relHorIt, originY, originZ + sideOffset);
                generateBlockAt(floorPalette, originX + sideOffset, originY, originZ + relHorIt);
                generateBlockAt(floorPalette, originX + sideOffset, originY, originZ - relHorIt);
            }
        }

        // FLOOR - round corners
        for (int relXIt = 2; true; relXIt++) {
            for (int relZIt = 2; relZIt < 5; relZIt++) {
                if (relXIt == 4 && relZIt == 4) return;
                generateBlockAt(floorPalette, originX + relXIt, originY, originZ + relZIt);
                generateBlockAt(floorPalette, originX - relXIt, originY, originZ + relZIt);
                generateBlockAt(floorPalette, originX + relXIt, originY, originZ - relZIt);
                generateBlockAt(floorPalette, originX - relXIt, originY, originZ - relZIt);
            }
        }
    }

    private static final double SELECTOR_SIZE_FACTOR = 0.7;

    private void generateBlockAt(Randomizer<Material> palette, int x, int y, int z) {
        generateBlockAt(palette, x, y, z, null);
    }

    private void generateBlockAt(Randomizer<Material> palette, int x, int y, int z, BlockFace face) {
        double selector = (noiseGen.noise(SELECTOR_SIZE_FACTOR * x, SELECTOR_SIZE_FACTOR * y, SELECTOR_SIZE_FACTOR * z) + 1.0) / 2;
        Material randomBlock = palette.getRandomValue(selector);

        Block block = world.getBlockAt(x, y, z);

        BlockData data = randomBlock.createBlockData();
        if (data instanceof Directional) {
            ((Directional) data).setFacing(face);
        }
        if (data instanceof Waterlogged) {
            ((Waterlogged) data).setWaterlogged(true);
        }
        block.setBlockData(data);
    }
}
