package com.obqy.biomes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public final class BiomeSoundManager {
    private BiomeSoundManager() {}

    private record BiomeSound(SoundEvent sound, float volume, float pitch) {}

    private static final Map<String, BiomeSound> CUSTOM_BIOME_SOUNDS = Map.of(
            "minecraft:cherry_grove", new BiomeSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F, 1.0F),
            "minecraft:deep_dark", new BiomeSound(SoundEvents.AMBIENT_CAVE.value(), 0.8F, 0.5F),
            "minecraft:mushroom_fields", new BiomeSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F, 1.5F),
            "minecraft:pale_garden", new BiomeSound(SoundEvents.CREAKING_DEATH, 0.8F, 0.5F),
            "minecraft:dappled_forest", new BiomeSound(SoundEvents.POPLAR_LEAVES_HIT, 0.8F, 1.0F)
    );

    public static void playBiomeSound(Minecraft client, String biomeId, BiomeInfo.BiomeRarity rarity) {
        BiomeSound customSound = CUSTOM_BIOME_SOUNDS.get(biomeId);

        if (customSound != null) {
            play(client, customSound);
            return;
        }

        BiomeSound raritySound = switch (rarity) {
            case COMMON -> new BiomeSound(SoundEvents.NOTE_BLOCK_HAT.value(), 0.8F, 0.75F);
            case UNCOMMON -> new BiomeSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8F, 1.0F);
            case RARE -> new BiomeSound(SoundEvents.PLAYER_LEVELUP, 0.8F, 1.0F);
            case UNKNOWN -> null;
        };

        if (raritySound != null) {
            play(client, raritySound);
        }
    }

    private static void play(Minecraft client, BiomeSound sound) {
        client.getSoundManager().play(SimpleSoundInstance.forUI(sound.sound(), sound.volume(), sound.pitch()));
    }
}