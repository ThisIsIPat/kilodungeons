package me.phein.kiloplugins.mc.kilodungeons.util;

import me.phein.kiloplugins.mc.kilodungeons.util.randomizer.Randomizer;

public class RandomizerPalette<V> implements Palette<V> {
    private final Randomizer<V> randomizer;

    public RandomizerPalette(Randomizer<V> randomizer) {
        this.randomizer = randomizer;
    }

    @Override
    public V withSeed(double seed) {
        return randomizer.getRandomValue(seed);
    }
}
