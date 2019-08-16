package me.phein.kiloplugins.mc.kilodungeons.util.randomizer;

import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class Randomizer<V> {
    private final NavigableMap<Double, V> optionMap = new TreeMap<>();

    public Randomizer(Set<Pair<V>> options) {
        double optionChanceSum = 0.0;
        for (Pair<V> option : options) {
            optionChanceSum += option.getChance();
        }
        double normalizedChanceIt = 0.0;
        for (Pair<V> option : options) {
            normalizedChanceIt += option.getChance() / optionChanceSum;
            optionMap.put(normalizedChanceIt, option.getValue());
        }
    }

    public V getRandomValue(double seed) {
        return optionMap.ceilingEntry(seed).getValue();
    }

    public static class Pair<T> {
        private final T value;
        private final double chance;

        public Pair(T value, double chance) {
            this.value = value;
            this.chance = chance;
        }

        public T getValue() {
            return value;
        }

        public double getChance() {
            return chance;
        }
    }
}
