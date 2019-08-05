package me.phein.kiloplugins.mc.kilodungeons.config.v0_1a;

import de.leonhard.storage.Yaml;

import java.io.File;
import java.io.IOException;

public class YmlConfig implements Config {

    protected final Yaml yaml;

    public YmlConfig(File configFile) throws IOException {
        this("0.1a", configFile);
    }

    protected YmlConfig(String version, File configFile) throws IOException {
        if (!configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdir()) {
                throw new IOException("Couldn't create config file \"" + configFile + "\". Did you allow the plugin to do this?");
            }
        }
        this.yaml = new Yaml(configFile);

        if (!yaml.getOrSetDefault("version", version).equals(version)) {
            throw new IOException("Outdated config file \"" + configFile + "\"!"); // TODO Throw custom exception
        }
    }

    @Override
    public double getDrownedChance() {
        return yaml.getOrSetDefault("drowned-chance", 0.01);
    }

    @Override
    public double getDrownedTreasureChance() {
        return yaml.getOrSetDefault("drowned-treasure-chance", 0.1);
    }
}
