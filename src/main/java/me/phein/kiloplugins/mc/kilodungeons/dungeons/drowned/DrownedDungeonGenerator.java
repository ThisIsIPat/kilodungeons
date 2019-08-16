package me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned;

import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.Randomizer;
import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.RandomizerBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class DrownedDungeonGenerator {
    private final World world;
    private NoiseGenerator noiseGen;
    private Randomizer<Material> stairPalette = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE_BRICK_STAIRS, 2.5)
            .addOption(Material.PRISMARINE_STAIRS, 2)
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

    public DrownedDungeonGenerator(World world) {
        this.world = world;
        this.noiseGen = new SimplexNoiseGenerator(world);
    }

    public void generate(int originX, int originY, int originZ) {
        generateFloor(originX, originY, originZ);
        generateWalls(originX, originY, originZ);
        generateCeiling(originX, originY, originZ);
        generateCorners(originX, originY, originZ);
    }

    private void generateCorners(int originX, int originY, int originZ) {
        generateMirroredBlocks(Material.GLASS, originX, originY + 1, originZ, 4, 2);
        generateMirroredBlocks(Material.GLASS, originX, originY + 1, originZ, 4, 3);
        generateMirroredBlocks(Material.GLASS, originX, originY + 2, originZ, 4, 2);
        generateMirroredBlocks(Material.GLASS, originX, originY + 2, originZ, 4, 3);
        generateMirroredBlocks(Material.GLASS, originX, originY + 2, originZ, 3, 3);

        generateMirroredBlocks(Material.GLASS, originX, originY + 3, originZ, 4, 2);
        generateMirroredBlocks(Material.GLASS, originX, originY + 3, originZ, 3, 3);
        generateMirroredBlocks(Material.GLASS, originX, originY + 4, originZ, 4, 2);
        generateMirroredBlocks(Material.GLASS, originX, originY + 4, originZ, 3, 3);

        generateMirroredBlocks(Material.GLASS, originX, originY + 5, originZ, 3, 2);

        generateMirroredBlocks(Material.GLASS, originX, originY + 6, originZ, 2, 2);
    }

    private void generateMirroredBlocks(Material material, int originX, int originY, int originZ, int radius, int offset) {
        generateBlockAt(material, originX + radius, originY, originZ + offset);
        generateBlockAt(material, originX - radius, originY, originZ - offset);
        generateBlockAt(material, originX - offset, originY, originZ + radius);
        generateBlockAt(material, originX + offset, originY, originZ - radius);

        if (offset == 0 || radius == offset) return;

        generateBlockAt(material, originX + radius, originY, originZ - offset);
        generateBlockAt(material, originX - radius, originY, originZ + offset);
        generateBlockAt(material, originX + offset, originY, originZ + radius);
        generateBlockAt(material, originX - offset, originY, originZ - radius);
    }

    private void generateCeiling(int originX, int originY, int originZ) {
        int ceilingHeight = originY + 6;

        for (int relX = -1; relX <= 1; relX++) {
            for (int relZ = -1; relZ <= 1; relZ++) {
                if (relX == 0 && relZ == 0) world.getBlockAt(originX, ceilingHeight, originZ).setType(Material.SEA_LANTERN);
                else generateBlockAt(floorPalette, originX + relX, ceilingHeight, originZ + relZ);
            }
        }

        // RADIUS = 2
        // Above
        generateCeilingBase(originX, ceilingHeight, originZ, 2);

        // RADIUS = 4
        // Above
        generateCeilingBase(originX, ceilingHeight - 2, originZ, 4);
        generateBlockAt(stairPalette, originX + 4, ceilingHeight - 1, originZ, BlockFace.WEST, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX - 4, ceilingHeight - 1, originZ, BlockFace.EAST, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX, ceilingHeight - 1, originZ + 4, BlockFace.NORTH, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX, ceilingHeight - 1, originZ - 4, BlockFace.SOUTH, Bisected.Half.BOTTOM);

        // RADIUS = 5
        // Above
        generateCeilingBase(originX, ceilingHeight - 4, originZ, 5);
        generateBlockAt(stairPalette, originX + 5, ceilingHeight - 3, originZ, BlockFace.WEST, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX - 5, ceilingHeight - 3, originZ, BlockFace.EAST, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX, ceilingHeight - 3, originZ + 5, BlockFace.NORTH, Bisected.Half.BOTTOM);
        generateBlockAt(stairPalette, originX, ceilingHeight - 3, originZ - 5, BlockFace.SOUTH, Bisected.Half.BOTTOM);

        for (int sideOffset = -1; sideOffset <= 1; sideOffset++) {
            // RADIUS = 2
            // Below
            generateBlockAt(stairPalette, originX + 2, ceilingHeight - 1, originZ + sideOffset, BlockFace.EAST, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX - 2, ceilingHeight - 1, originZ + sideOffset, BlockFace.WEST, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX + sideOffset, ceilingHeight - 1, originZ + 2, BlockFace.SOUTH, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX + sideOffset, ceilingHeight - 1, originZ - 2, BlockFace.NORTH, Bisected.Half.TOP);

            // RADIUS = 3
            // Above
            generateBlockAt(solidWallPalette, originX + 3, ceilingHeight - 1, originZ + sideOffset);
            generateBlockAt(solidWallPalette, originX - 3, ceilingHeight - 1, originZ + sideOffset);
            generateBlockAt(solidWallPalette, originX + sideOffset, ceilingHeight - 1, originZ + 3);
            generateBlockAt(solidWallPalette, originX + sideOffset, ceilingHeight - 1, originZ - 3);
            // Below
            generateBlockAt(stairPalette, originX + 3, ceilingHeight - 2, originZ + sideOffset, BlockFace.EAST, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX - 3, ceilingHeight - 2, originZ + sideOffset, BlockFace.WEST, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX + sideOffset, ceilingHeight - 2, originZ + 3, BlockFace.SOUTH, Bisected.Half.TOP);
            generateBlockAt(stairPalette, originX + sideOffset, ceilingHeight - 2, originZ - 3, BlockFace.NORTH, Bisected.Half.TOP);
        }
    }

    // Small recurring pattern in the ceiling generation.
    private void generateCeilingBase(int originX, int originY, int originZ, int radius) {
        for (int sideOffset = -1; sideOffset <= 1; sideOffset++) {
            if (sideOffset == 0) {
                generateBlockAt(solidWallPalette, originX + radius, originY, originZ);
                generateBlockAt(solidWallPalette, originX - radius, originY, originZ);
                generateBlockAt(solidWallPalette, originX, originY, originZ + radius);
                generateBlockAt(solidWallPalette, originX, originY, originZ - radius);
            } else {
                generateBlockAt(stairPalette, originX + radius, originY, originZ + sideOffset, BlockFace.WEST, Bisected.Half.BOTTOM);
                generateBlockAt(stairPalette, originX - radius, originY, originZ + sideOffset, BlockFace.EAST, Bisected.Half.BOTTOM);
                generateBlockAt(stairPalette, originX + sideOffset, originY, originZ + radius, BlockFace.NORTH, Bisected.Half.BOTTOM);
                generateBlockAt(stairPalette, originX + sideOffset, originY, originZ - radius, BlockFace.SOUTH, Bisected.Half.BOTTOM);
            }
        }
    }

    private void generateWalls(int originX, int originY, int originZ) {
        for (int radiusIt = 0; radiusIt <= 1; radiusIt++) {
            int radius = 4 + radiusIt;
            int wallHeight = 3 - 2 * radiusIt;

            for (int sideOffset = 1; sideOffset >= -1; sideOffset--) {
                for (int relY = 1; relY <= wallHeight; relY++) {
                    generateBlockAt(solidWallPalette, originX + radius, originY + relY, originZ + sideOffset);
                    generateBlockAt(solidWallPalette, originX - radius, originY + relY, originZ + sideOffset);
                    generateBlockAt(solidWallPalette, originX + sideOffset, originY + relY, originZ + radius);
                    generateBlockAt(solidWallPalette, originX + sideOffset, originY + relY, originZ - radius);
                }
            }
        }
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

    private static final double SELECTOR_SIZE_FACTOR = 0.7; // Defines how rapid the palette changes.

    private void generateBlockAt(Randomizer<Material> palette, int x, int y, int z) {
        generateBlockAt(palette, x, y, z, null, null);
    }

    private void generateBlockAt(Material material, int x, int y, int z) {
        generateBlockAt(material, x, y, z, null, null);
    }

    private void generateBlockAt(Randomizer<Material> palette, int x, int y, int z, BlockFace face, Bisected.Half half) {
        double selector = (noiseGen.noise(SELECTOR_SIZE_FACTOR * x, SELECTOR_SIZE_FACTOR * y, SELECTOR_SIZE_FACTOR * z) + 1.0) / 2;
        Material randomBlock = palette.getRandomValue(selector);

        generateBlockAt(randomBlock, x, y, z, face, half);
    }

    private void generateBlockAt(Material material, int x, int y, int z, BlockFace face, Bisected.Half half) {
        System.out.println("Generating @x" + x + " y" + y + " z" + z);
        Block block = world.getBlockAt(x, y, z);

        BlockData data = material.createBlockData();
        if (face != null) {
            if (data instanceof Directional) {
                ((Directional) data).setFacing(face);
            }
        }
        if (half != null) {
            if (data instanceof Bisected) {
                ((Bisected) data).setHalf(half);
            }
            if (data instanceof Slab) {
                switch (half) {
                    case TOP:
                        ((Slab) data).setType(Slab.Type.TOP);
                        break;
                    case BOTTOM:
                        ((Slab) data).setType(Slab.Type.BOTTOM);
                        break;
                }
            }
        }
        if (data instanceof Waterlogged) {
            ((Waterlogged) data).setWaterlogged(true);
        }
        block.setBlockData(data);
    }
}
