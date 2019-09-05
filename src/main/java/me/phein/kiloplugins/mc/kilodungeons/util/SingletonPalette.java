package me.phein.kiloplugins.mc.kilodungeons.util;

public class SingletonPalette<V> implements Palette<V> {
    private final V value;

    public SingletonPalette(V value) {
        this.value = value;
    }

    @Override
    public V withSeed(double seed) {
        return value;
    }
}
