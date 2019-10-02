package me.phein.kiloplugins.mc.kilodungeons.dungeons;

import me.phein.kiloplugins.mc.kilodungeons.dungeons.small.dome.SmallDomeDungeon;

public enum DungeonCategory {
    SMALL_DOME(
            SmallDomeDungeon.DESERT,
            SmallDomeDungeon.OCEAN);

    private final Dungeon[] dungeons;

    DungeonCategory(Dungeon... dungeons) {
        this.dungeons = dungeons;
    }

    public Dungeon[] getDungeons() {
        return dungeons;
    }
}
