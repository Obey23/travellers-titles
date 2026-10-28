package com.obqy.biomes.sounds;

import com.obqy.biomes.BiomeInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import com.obqy.config.BiomeConfig;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class BiomeSoundManager {
    private BiomeSoundManager() {}

    private record BiomeSound(SoundEvent sound, float volume, float pitch) {}

    private static BiomeSoundEntry findCustomSound(Holder<Biome> biome) {
        String biomeId = biome.unwrapKey()
                .map(key -> key.identifier().toString())
                .orElse("");
        BiomeSoundEntry wildcard = null;

        for (BiomeSoundEntry entry : BiomeConfig.INSTANCE.customBiomeSounds) {
            if (entry == null || entry.biomeId == null) continue;

            // Exact biome matches first
            if (biomeId.equals(entry.biomeId)) return entry;

            // If wildcard and wildcard entry isn't set, use it
            if ("*".equals(entry.biomeId)) {
                if (wildcard == null) wildcard = entry;
                continue;
            }

            // Biome tags, like #minecraft:is_overworld
            if (entry.biomeId.startsWith("#")) {
                try {
                    var tagId = Identifier.parse(entry.biomeId.substring(1));
                    var tag = TagKey.create(Registries.BIOME, tagId);
                    if (biome.is(tag)) return entry;
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return wildcard;
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

    public static void playBiomeSound(Minecraft client, Holder<Biome> biome, BiomeInfo.BiomeRarity rarity) {
        BiomeSoundEntry customSound = findCustomSound(biome);

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