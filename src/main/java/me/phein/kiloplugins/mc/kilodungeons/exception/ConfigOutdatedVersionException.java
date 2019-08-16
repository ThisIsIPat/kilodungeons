package me.phein.kiloplugins.mc.kilodungeons.exception;

import java.io.File;

public class ConfigOutdatedVersionException extends ConfigException {
    public ConfigOutdatedVersionException(File configFile) {
        super("Outdated config file \"" + configFile.getAbsolutePath() + "\"!");
    }
}
