package me.phein.kiloplugins.mc.kilodungeons.util;

import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.Randomizer;

public class Palette<V> {
    private Randomizer<V> randomizer;
    private V value;

    public Palette(Randomizer<V> randomizer) {
        this.randomizer = randomizer;
        this.value = null;
    }

    public Palette(V value) {
        this.randomizer = null;
        this.value = value;
    }

    public V withSeed(double seed) {
        if (randomizer == null) {
            return this.value;
        }
        return this.randomizer.getRandomValue(seed);
    }
}
