
package com.obqy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obqy.TravellersTitles;
import com.obqy.biomes.BiomeSoundEntry;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BiomeConfig {
    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("biome-tracker.json");

    public static BiomeConfig INSTANCE = new BiomeConfig();

    public float titleDefaultSize = 3.5F;
    public float titleScale = 1.0F;

    public float subtitleDefaultSize = 1.25F;
    public float subtitleScale = 1.0F;

    public boolean scaleWithGuiScale = true;
    public boolean notificationAtTop = false;
    public boolean rarityAsStars = false;

    public TemperatureDisplayMode temperatureDisplayMode =
            TemperatureDisplayMode.MINECRAFT;

    public TemperatureUnit temperatureUnit =
            TemperatureUnit.CELSIUS;

    public int biomeConfirmationDelayMs = 1500;
    public int biomeNotificationCooldownMs = 2000;

    public static List<BiomeSoundEntry> createDefaultBiomeSounds() {
        return new ArrayList<>(List.of(
                new BiomeSoundEntry("minecraft:cherry_grove", "minecraft:block.amethyst_block.chime", 1.5F, 1.0F),
                new BiomeSoundEntry("minecraft:deep_dark", "minecraft:ambient.cave", 0.8F, 0.5F),
                new BiomeSoundEntry("minecraft:pale_garden", "minecraft:entity.creaking.death", 0.8F, 0.5F),
                new BiomeSoundEntry("minecraft:dappled_forest", "minecraft:block.poplar_leaves.hit", 0.8F, 1.0F)
        ));
    }

    public List<BiomeSoundEntry> customBiomeSounds = createDefaultBiomeSounds();

    private BiomeConfig() {}

    public static void load() {
        if (Files.exists(FILE)) {
            try {
                BiomeConfig loaded = GSON.fromJson(Files.readString(FILE), BiomeConfig.class);
                if (loaded != null) INSTANCE = loaded;
                if (INSTANCE.customBiomeSounds == null) {
                    INSTANCE.customBiomeSounds = new ArrayList<>();
                }
            } catch (Exception e) {
                TravellersTitles.LOGGER.error("Could not load biome-tracker.json: {}", e.getMessage());
            }
        }

        save();
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            TravellersTitles.LOGGER.error("Could not save biome-tracker.json: {}", e.getMessage());
        }
    }
}
