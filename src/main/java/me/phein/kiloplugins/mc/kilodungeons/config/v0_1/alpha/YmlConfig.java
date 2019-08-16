package me.phein.kiloplugins.mc.kilodungeons.config.v0_1.alpha;

import de.leonhard.storage.Yaml;
import me.phein.kiloplugins.mc.kilodungeons.config.v0_1.Config;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigException;
import me.phein.kiloplugins.mc.kilodungeons.exception.ConfigOutdatedVersionException;

import java.io.File;

public class YmlConfig implements Config {

    protected final Yaml yaml;

    public YmlConfig(File configFile) throws ConfigException {
        this("0.1a", configFile);
    }

    protected YmlConfig(String version, File configFile) throws ConfigException {
        if (!configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdir()) {
                throw new ConfigException("Couldn't create config file \"" + configFile + "\". Did you allow the plugin to do this?");
            }
        }
        this.yaml = new Yaml(configFile);

        if (!yaml.getOrSetDefault("version", version).equals(version)) {
            throw new ConfigOutdatedVersionException(configFile);
        }
    }

    @Override
    public double getDrownedDungeonChance() {
        return yaml.getOrSetDefault("drowned-chance", 0.01);
    }

    @Override
    public double getDrownedDungeonTreasureChance() {
        return yaml.getOrSetDefault("drowned-treasure-chance", 0.25);
    }
}
