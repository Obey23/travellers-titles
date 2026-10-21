package com.obqy.biomes;

public class BiomeSoundEntry {
    public String biomeId = "minecraft:cherry_grove";
    public String soundId = "minecraft:ui.toast.challenge_complete";
    public float volume = 0.8F;
    public float pitch = 1.0F;

    public BiomeSoundEntry() {}

    public BiomeSoundEntry(String biomeId, String soundId, float volume, float pitch) {
        this.biomeId = biomeId;
        this.soundId = soundId;
        this.volume = volume;
        this.pitch = pitch;
    }
}