package me.phein.kiloplugins.mc.kilodungeons.dungeons.small;

import me.phein.kiloplugins.mc.kilodungeons.util.Palette;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public interface SmallDungeonPalette {
    Palette<Material> getFloorCenter();
    Palette<Material> getFloorMaterial();
    Palette<Material> getBrokenFloorMaterial();

    Palette<Material> getSpawnerPost();
    Palette<EntityType> getSpawnerType();

    Palette<Material> getPartPillarMaterial();
    Palette<Material> getSolidPillarMaterial();
    Palette<Material> getCornerMaterial();

    Palette<Material> getCeilingCenter();
}
