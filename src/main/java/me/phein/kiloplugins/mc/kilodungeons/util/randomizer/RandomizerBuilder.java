package me.phein.kiloplugins.mc.kilodungeons.util.randomizer;

import java.util.HashSet;
import java.util.Set;

public class RandomizerBuilder<V> {
    private final Set<Randomizer.Pair<V>> options = new HashSet<>();

    public RandomizerBuilder<V> addOption(V option, double chance) {
        options.add(new Randomizer.Pair<>(option, chance));
        return this;
    }

    public Randomizer<V> build() {
        return new Randomizer<>(this.options);
    }
}
