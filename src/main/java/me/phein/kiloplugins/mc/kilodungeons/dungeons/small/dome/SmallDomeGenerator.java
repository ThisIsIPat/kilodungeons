package me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome;

import me.phein.kiloplugins.mc.kilodungeons.event.KiloDungeonSpawnEvent;
import me.phein.kiloplugins.mc.kilodungeons.util.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.util.noise.NoiseGenerator;

/**
 * Represents a single instance of a small dome generation process.
 */
public abstract class SmallDomeGenerator {
    private final SmallDomePalette palette;

    private final World world;
    private final int originX, originY, originZ;

    private final double brokenRate;
    private final double treasureChance;

    private final NoiseGenerator noiseGenerator;

    public SmallDomeGenerator(SmallDomePalette palette, double brokenRate,
                              World world, int originX, int originZ, NoiseGenerator noiseGenerator,
                              double treasureChance) {
        this.palette = palette;
        this.world = world;

        this.originX = originX;
        this.originZ = originZ;
        this.originY = calculateOriginY(); // Do NOT put this before originX or originZ assignment, calculateOriginY() uses them

        this.brokenRate = brokenRate;
        this.treasureChance = treasureChance;

        this.noiseGenerator = noiseGenerator;
    }

    public SmallDomeGenerator(SmallDomePalette palette, double brokenRate,
                              World world, int originX, int originY, int originZ, NoiseGenerator noiseGenerator,
                              double treasureChance) {
        this.palette = palette;
        this.world = world;

        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;

        this.brokenRate = brokenRate;
        this.treasureChance = treasureChance;

        this.noiseGenerator = noiseGenerator;
    }



    public boolean generate() {
        // Event creation
        Location dungeonLocation = new Location(world, getOriginX(), getOriginY(), getOriginZ());
        Location teleportLocation = new Location(world, getOriginX(), getOriginY() + 3, getOriginZ());
        KiloDungeonSpawnEvent spawnEvent = new KiloDungeonSpawnEvent(getSmallDomeCategory(), dungeonLocation, teleportLocation);

        // Optional native cancelling
        if (originY < 0) spawnEvent.setCancelled(true);

        Bukkit.getPluginManager().callEvent(spawnEvent);

        if (spawnEvent.isCancelled()) return false;

        generateFloor();
        generateCeiling();
        generateCorners();

        generateBrokenFloor();

        generateSpawner();
        generateTreasure();

        return true;
    }

    protected void generateFloor() {
        // FLOOR - center
        generateBlock(originX, originY, originZ, palette.getFloorCenter(), 0.75, "floor");

        // FLOOR - x + z axis (Fat plus-shape)
        for (int relHorIt = 5; relHorIt > 0; relHorIt--) {
            generateMirrored(palette.getFloorMaterial(), 0, relHorIt, 0, 0.75, "floor");
            generateMirrored(palette.getFloorMaterial(), 0, relHorIt, 1, 0.75, "floor");
        }

        // FLOOR - round corners
        for (int radiusIt = 2; true; radiusIt++) {
            for (int offset = 2; offset < 5; offset++) {
                if (radiusIt == 4 && offset == 4) return;
                generateMirrored(palette.getFloorMaterial(), 0, radiusIt, offset, 0.75, "floor");
            }
        }
    }

    protected void generateCeiling() {
        int ceilingHeight = 6;

        // RADIUS = 1
        generateBlock(originX, originY + ceilingHeight, originZ, palette.getCeilingCenter(), 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), ceilingHeight, 1, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), ceilingHeight, 1, 1, 0.75, "ceiling");

        // RADIUS = 2
        // Above
        generateCeilingBase(ceilingHeight, 2);
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 1, 2, 0, 0.75, Bisected.Half.TOP, "ceiling");
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 1, 2, 1, 0.75, Bisected.Half.TOP, "ceiling");

        // RADIUS = 3
        // Above
        generateMirrored(palette.getSolidPillarMaterial(), ceilingHeight - 1, 3, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), ceilingHeight - 1, 3, 1, 0.75, "ceiling");
        // Below
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 2, 3, 0, 0.75, Bisected.Half.TOP, "ceiling");
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 2, 3, 1, 0.75, Bisected.Half.TOP, "ceiling");

        // RADIUS = 4
        // Above
        generateCeilingBase(ceilingHeight - 2, 4);
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 1, 4, 0, 0.75, Bisected.Half.BOTTOM, "ceiling");

        // RADIUS = 5
        // Above
        generateCeilingBase(ceilingHeight - 4, 5);
        generateMirrored(palette.getPartPillarMaterial(), ceilingHeight - 3, 5, 0, 0.75, Bisected.Half.BOTTOM, "ceiling");

        // r = 4, h = 3
        generateMirrored(palette.getSolidPillarMaterial(), 1, 4, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 1, 4, 1, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 2, 4, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 2, 4, 1, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 3, 4, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 3, 4, 1, 0.75, "ceiling");
        // r = 5, h = 1
        generateMirrored(palette.getSolidPillarMaterial(), 1, 5, 0, 0.75, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), 1, 5, 1, 0.75, "ceiling");
    }
    // Small recurring pattern in the ceiling generation.
    protected void generateCeilingBase(int yOffset, int radius) {
        generateMirrored(palette.getPartPillarMaterial(), yOffset, radius, 0, 0.75, Bisected.Half.BOTTOM, "ceiling");
        generateMirrored(palette.getSolidPillarMaterial(), yOffset, radius, 1, 0.75, Bisected.Half.BOTTOM, "ceiling");
    }

    protected void generateCorners() {
        generateMirrored(palette.getCornerMaterial(), 1, 4, 2, "corner");
        generateMirrored(palette.getCornerMaterial(), 1, 4, 3, "corner");
        generateMirrored(palette.getCornerMaterial(), 2, 4, 2, "corner");
        generateMirrored(palette.getCornerMaterial(), 2, 4, 3, "corner");
        generateMirrored(palette.getCornerMaterial(), 2, 3, 3, "corner");

        generateMirrored(palette.getCornerMaterial(), 3, 4, 2, "corner");
        generateMirrored(palette.getCornerMaterial(), 3, 3, 3, "corner");
        generateMirrored(palette.getCornerMaterial(), 4, 4, 2, "corner");
        generateMirrored(palette.getCornerMaterial(), 4, 3, 3, "corner");

        generateMirrored(palette.getCornerMaterial(), 5, 3, 2, "corner");
        generateMirrored(palette.getCornerMaterial(), 6, 2, 2, "corner");
    }

    protected void generateBrokenFloor() {
        for (int offset = 2; offset >= 0; offset--) {
            generateMirrored(palette.getInnerBrokenFloorMaterial(), 1, 2, offset, "brokenfloor");
        }
        for (int offset = 3; offset >= 0; offset--) {
            generateMirrored(palette.getOuterBrokenFloorMaterial(), 1, 3, offset, "brokenfloor");
        }
    }

    protected void generateSpawner() {
        generateBlock(originX, originY + 1, originZ, palette.getSpawnerPost(), "spawnerpost");

        double paletteSeedSpawnerType = normalizedNoise(originX, originZ, "spawner");
        world.getBlockAt(originX, originY + 2, originZ).setType(Material.SPAWNER);
        CreatureSpawner spawner = ((CreatureSpawner) world.getBlockAt(originX, originY + 2, originZ).getState());
        spawner.setSpawnedType(palette.getSpawnerType().withSeed(paletteSeedSpawnerType));
        spawner.update();
    }

    protected void generateTreasure() {
        double chestSelector = normalizedNoise(originX, originZ, "treasure");

        if (chestSelector < 0.5) {
            boolean treasure1 = (noiseGenerator.noise(originX + 3, originZ - 1) + 1.0) / 2 < treasureChance;
            generateLoot(world, originX + 3, originY + 1, originZ - 1, BlockFace.WEST, treasure1);

            boolean treasure2 = (noiseGenerator.noise(originX - 3, originZ + 1) + 1.0) / 2 < treasureChance;
            generateLoot(world, originX - 3, originY + 1, originZ + 1, BlockFace.EAST, treasure2);
        } else {
            boolean treasure3 = (noiseGenerator.noise(originX - 1, originZ + 3) + 1.0) / 2 < treasureChance;
            generateLoot(world, originX - 1, originY + 1, originZ + 3, BlockFace.SOUTH, treasure3);

            boolean treasure4 = (noiseGenerator.noise(originX + 1, originZ - 3) + 1.0) / 2 < treasureChance;
            generateLoot(world, originX + 1, originY + 1, originZ - 3, BlockFace.NORTH, treasure4);
        }
    }
    protected abstract void generateLoot(World world, int x, int y, int z, BlockFace facing, boolean treasure);

    protected void generateMirrored(Palette<Material> materialPalette, int yOffset, int radius, int offset, String salt) {
        generateMirrored(materialPalette, yOffset, radius, offset, 1.0, salt);
    }
    protected void generateMirrored(Palette<Material> materialPalette, int yOffset, int radius, int offset, double selectionPace, String salt) {
        generateMirrored(materialPalette, yOffset, radius, offset, selectionPace, null, salt);
    }
    protected void generateMirrored(Palette<Material> materialPalette, int yOffset, int radius, int offset, double selectionPace, Bisected.Half half, String salt) {
        if (half == Bisected.Half.BOTTOM) {
            generateBlock(originX + radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, BlockFace.WEST, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, BlockFace.EAST, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, BlockFace.NORTH, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, BlockFace.SOUTH, half, salt);

            if (offset == 0 || radius == offset) return;

            generateBlock(originX + radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, BlockFace.WEST, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, BlockFace.EAST, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, BlockFace.NORTH, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, BlockFace.SOUTH, half, salt);
        } else if (half == Bisected.Half.TOP) {
            generateBlock(originX + radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, BlockFace.EAST, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, BlockFace.WEST, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, BlockFace.SOUTH, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, BlockFace.NORTH, half, salt);

            if (offset == 0 || radius == offset) return;

            generateBlock(originX + radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, BlockFace.EAST, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, BlockFace.WEST, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, BlockFace.SOUTH, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, BlockFace.NORTH, half, salt);
        } else {
            // Possible BUG: blocks that aren't Bisected could still be facing a direction.
            generateBlock(originX + radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, null, half, salt);

            if (offset == 0 || radius == offset) return;

            generateBlock(originX + radius, originY + yOffset, originZ - offset, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX - radius, originY + yOffset, originZ + offset, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX + offset, originY + yOffset, originZ + radius, materialPalette, selectionPace, null, half, salt);
            generateBlock(originX - offset, originY + yOffset, originZ - radius, materialPalette, selectionPace, null, half, salt);
        }
    }

    protected void generateBlock(int x, int y, int z, Palette<Material> materialPalette, String salt) {
        generateBlock(x, y, z, materialPalette, 1.0, salt);
    }
    protected void generateBlock(int x, int y, int z, Palette<Material> materialPalette, double selectionPace, String salt) {
        generateBlock(x, y, z, materialPalette, selectionPace, null, null, salt);
    }
    protected void generateBlock(int x, int y, int z, Palette<Material> materialPalette, double selectionPace, BlockFace facing, Bisected.Half half, String salt) {
        double skipSeed = normalizedNoise(x, y, z, "skip");
        if (skipSeed < brokenRate) return;

        double blockSeed = normalizedNoise(x, y, z, selectionPace, salt);

        BlockData data = materialPalette.withSeed(blockSeed).createBlockData();

        if (facing != null) {
            if (data instanceof Directional) {
                ((Directional) data).setFacing(facing);
            }
        }
        if (half != null) {
            if (data instanceof Bisected) {
                ((Bisected) data).setHalf(half);
            } else if (data instanceof Slab) {
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

        world.getBlockAt(x, y, z).setBlockData(data);
    }

    protected final double normalizedNoise(int x, int y, String salt) {
        return (noiseGenerator.noise(x + salt.hashCode(), y + salt.hashCode()) + 1.0) / 2;
    }
    protected final double normalizedNoise(int x, int y, int z, String salt) {
        return (noiseGenerator.noise(x + salt.hashCode(), y + salt.hashCode(), z + salt.hashCode()) + 1.0) / 2;
    }
    protected final double normalizedNoise(int x, int y, int z, double selectionPace, String salt) {
        return (noiseGenerator.noise(
                (x + salt.hashCode() % 64) * selectionPace,
                (y + salt.hashCode() % 64) * selectionPace,
                (z + salt.hashCode() % 64) * selectionPace
        ) + 1.0) / 2;
    }

    public World getWorld() {
        return world;
    }
    public int getOriginX() {
        return originX;
    }
    public int getOriginY() {
        return originY;
    }
    public int getOriginZ() {
        return originZ;
    }

    /**
     * Is used in constructor to get the y value of the dungeon.
     *
     * @return y value the dungeon bases on
     */
    protected abstract int calculateOriginY();
    public abstract SmallDomeDungeon getSmallDomeCategory();
}
