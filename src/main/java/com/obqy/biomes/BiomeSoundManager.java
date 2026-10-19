package com.obqy.biomes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import com.obqy.config.BiomeConfig;

public final class BiomeSoundManager {
    private BiomeSoundManager() {}

    private record BiomeSound(SoundEvent sound, float volume, float pitch) {}

    private static BiomeSoundEntry findCustomSound(String biomeId) {
        return BiomeConfig.INSTANCE.customBiomeSounds.stream()
                .filter(entry -> entry != null)
                .filter(entry -> biomeId.equals(entry.biomeId))
                .findFirst()
                .orElse(null);
    }


    private static SoundEvent resolveSound(String soundId) {
        if (soundId == null || soundId.isBlank()) return null;

        try {
            return net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT
                    .getOptional(net.minecraft.resources.Identifier.parse(soundId))
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void playBiomeSound(Minecraft client, String biomeId, BiomeInfo.BiomeRarity rarity) {
        BiomeSoundEntry customSound = findCustomSound(biomeId);

        if (customSound != null) {
            SoundEvent sound = resolveSound(customSound.soundId);
            if (sound != null) {
                play(client, new BiomeSound(sound, customSound.volume, customSound.pitch));
                return;
            }
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