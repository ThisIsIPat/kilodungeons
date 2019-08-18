package me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release;

import de.leonhard.storage.ConfigSettings;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigException;

import java.io.File;

public class YmlConfig extends me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig {

    public static YmlConfig fromAlpha(File configFile, me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig alphaConfig) throws ConfigException {
        YmlConfig config = new YmlConfig(configFile);

        double drownedSpawnChance = alphaConfig.getDrownedDungeonSpawnChance();
        double drownedTreasureChance = alphaConfig.getDrownedDungeonTreasureChance();

        config.yaml.set(DROWNED_DUNGEON_SPAWN_CHANCE_KEY, drownedSpawnChance, ConfigSettings.preserveComments);
        config.yaml.set(DROWNED_DUNGEON_TREASURE_CHANCE_KEY, drownedTreasureChance, ConfigSettings.preserveComments);

        return config;
    }

    public YmlConfig(File configFile) throws ConfigException {
        this("0.1", configFile);
    }

    protected YmlConfig(String version, File configFile) throws ConfigException {
        super(version, configFile);
    }

    private static final String DROWNED_DUNGEON_SPAWN_CHANCE_KEY = "dungeons.small.drowned.spawn-chance";
    @Override public double getDrownedDungeonSpawnChance() {
        return yaml.getOrSetDefault(DROWNED_DUNGEON_SPAWN_CHANCE_KEY, 0.01);
    }

    private static final String DROWNED_DUNGEON_TREASURE_CHANCE_KEY = "dungeons.small.drowned.treasure-chance";
    @Override public double getDrownedDungeonTreasureChance() {
        return yaml.getOrSetDefault(DROWNED_DUNGEON_TREASURE_CHANCE_KEY, 0.25);
    }
}
