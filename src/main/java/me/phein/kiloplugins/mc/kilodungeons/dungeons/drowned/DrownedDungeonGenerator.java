package me.phein.kiloplugins.mc.kilodungeons.dungeons.drowned;

import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.Randomizer;
import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.RandomizerBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class DrownedDungeonGenerator {
    private final World world;
    private NoiseGenerator noiseGen;
    private Randomizer<Material> floorPalette = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE, 2)
            .addOption(Material.PRISMARINE_BRICKS, 1)
            .addOption(Material.DARK_PRISMARINE_SLAB, 0.5)
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
    }

    private void generateFloor(int originX, int originY, int originZ) {
        // FLOOR - center
        world.getBlockAt(originX, originY, originZ).setType(Material.SEA_LANTERN);

        // FLOOR - x + z axis (Fat plus-shape)
        for (int relHorIt = 5; true; relHorIt--) {
            // Middle plus
            generateBlockAt(floorPalette, originX + relHorIt, originY, originZ);
            generateBlockAt(floorPalette, originX - relHorIt, originY, originZ);
            generateBlockAt(floorPalette, originX, originY, originZ + relHorIt);
            generateBlockAt(floorPalette, originX, originY, originZ - relHorIt);

            // Side (Clock-wise)
            generateBlockAt(floorPalette, originX + relHorIt, originY, originZ + 1);
            generateBlockAt(floorPalette, originX - relHorIt, originY, originZ - 1);
            generateBlockAt(floorPalette, originX - 1, originY, originZ + relHorIt);
            generateBlockAt(floorPalette, originX + 1, originY, originZ - relHorIt);

            // Side (Counter-clock-wise)
            // This is not needed in the most center iteration, as it was already generated clock-wise.
            if (relHorIt <= 1) break;
            generateBlockAt(floorPalette, originX + relHorIt, originY, originZ - 1);
            generateBlockAt(floorPalette, originX - relHorIt, originY, originZ + 1);
            generateBlockAt(floorPalette, originX + 1, originY, originZ + relHorIt);
            generateBlockAt(floorPalette, originX - 1, originY, originZ - relHorIt);
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
        double selector = (noiseGen.noise(SELECTOR_SIZE_FACTOR * x, SELECTOR_SIZE_FACTOR * y, SELECTOR_SIZE_FACTOR * z) + 1.0) / 2;
        Material randomBlock = palette.getRandomValue(selector);
        world.getBlockAt(x, y, z).setType(randomBlock);
    }
}
