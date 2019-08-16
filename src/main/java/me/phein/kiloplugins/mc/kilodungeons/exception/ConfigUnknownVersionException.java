package me.phein.kiloplugins.mc.kilodungeons.exception;

import java.io.File;

public class ConfigUnknownVersionException extends ConfigVersionException {
    public ConfigUnknownVersionException(File configFile, String version) {
        super("Config file \"" + configFile.getAbsolutePath() + "\" specifies to be in an unknown version: " + version + "\n" +
                "Is the plugin up-to-date?");
    }
}
