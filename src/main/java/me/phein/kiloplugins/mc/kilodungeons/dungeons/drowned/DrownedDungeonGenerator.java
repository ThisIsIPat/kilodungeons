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
    private Randomizer<Material> blocks = new RandomizerBuilder<Material>()
            .addOption(Material.PRISMARINE, 2)
            .addOption(Material.PRISMARINE_BRICKS, 1)
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

        int radius = 5;

        // FLOOR - center
        world.getBlockAt(originX, originY, originZ).setType(Material.SEA_LANTERN);

        // FLOOR - x + z axis (Fat plus-shape)
        for (int relHorIt = radius; true; relHorIt--) {
            // Middle plus
            generateFloor(originX + relHorIt, originY, originZ);
            generateFloor(originX - relHorIt, originY, originZ);
            generateFloor(originX, originY, originZ + relHorIt);
            generateFloor(originX, originY, originZ - relHorIt);

            // Side (Clock-wise)
            generateFloor(originX + relHorIt, originY, originZ + 1);
            generateFloor(originX - relHorIt, originY, originZ - 1);
            generateFloor(originX - 1, originY, originZ + relHorIt);
            generateFloor(originX + 1, originY, originZ - relHorIt);

            // Side (Counter-clock-wise)
            // This is not needed in the most center iteration, as it was already generated clock-wise.
            if (relHorIt <= 1) break;
            generateFloor(originX + relHorIt, originY, originZ - 1);
            generateFloor(originX - relHorIt, originY, originZ + 1);
            generateFloor(originX + 1, originY, originZ + relHorIt);
            generateFloor(originX - 1, originY, originZ - relHorIt);
        }

        // FLOOR - round corners
        for (int relXIt = 2; relXIt < radius; relXIt++) {
            for (int relZIt = 2; relZIt < radius; relZIt++) {
                if (relXIt == radius - 1 && relZIt == radius - 1) break;
                generateFloor(originX + relXIt, originY, originZ + relZIt);
                generateFloor(originX - relXIt, originY, originZ + relZIt);
                generateFloor(originX + relXIt, originY, originZ - relZIt);
                generateFloor(originX - relXIt, originY, originZ - relZIt);
            }
        }
    }

    private void generateFloor(int x, int y, int z) {
        double selector = (noiseGen.noise(0.1 * x, 0.1 * z) + 1.0) / 2;
        Material randomBlock = blocks.getRandomValue(selector);
        world.getBlockAt(x, y, z).setType(randomBlock);
    }
}
