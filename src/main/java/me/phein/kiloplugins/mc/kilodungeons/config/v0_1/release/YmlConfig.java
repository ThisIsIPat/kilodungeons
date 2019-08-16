package me.phein.kiloplugins.mc.kilodungeons.config.v0_1.release;

import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigException;

import java.io.File;

public class YmlConfig extends me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha.YmlConfig {

    public YmlConfig(File configFile) throws ConfigException {
        this("0.1", configFile);
    }

    protected YmlConfig(String version, File configFile) throws ConfigException {
        super(version, configFile);
    }

    @Override
    public double getDrownedDungeonChance() {
        return yaml.getOrSetDefault("dungeons.small.drowned.spawn-chance", 0.01);
    }

    @Override
    public double getDrownedDungeonTreasureChance() {
        return yaml.getOrSetDefault("dungeons.small.drowned.treasure-chance", 0.25);
    }
}
