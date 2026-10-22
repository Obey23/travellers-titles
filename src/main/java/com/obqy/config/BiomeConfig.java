package com.obqy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obqy.TravellersTitles;
import com.obqy.biomes.BiomeSoundEntry;
import com.obqy.config.enums.NotificationPosition;
import com.obqy.config.enums.TemperatureDisplayMode;
import com.obqy.config.enums.TemperatureUnit;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BiomeConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("travellers-titles.json");
    public static BiomeConfig INSTANCE = new BiomeConfig();

    // Notification Settings
    public boolean showAsToast = false;
    public NotificationPosition toastPosition = NotificationPosition.TOP_MIDDLE;
    public int popupSize = 100;
    public int toastSize = 100;

    public boolean rarityAsStars = false;

    // Confirmation (before showing popup)
    public int biomeConfirmationDelayMs = 1500;
    public int biomeNotificationCooldownMs = 2000;

    // Temperature Display
    public TemperatureDisplayMode temperatureDisplayMode = TemperatureDisplayMode.MINECRAFT;
    public TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;

    // Biome Sounds
    public static List<BiomeSoundEntry> createDefaultBiomeSounds() {
        return new ArrayList<>(List.of(
                new BiomeSoundEntry("minecraft:cherry_grove", "minecraft:block.amethyst_block.chime", 2.0F, 1.0F),
                new BiomeSoundEntry("minecraft:deep_dark", "minecraft:ambient.cave", 0.8F, 0.5F),
                new BiomeSoundEntry("minecraft:pale_garden", "minecraft:entity.creaking.death", 0.8F, 0.5F),
                new BiomeSoundEntry("minecraft:dappled_forest", "minecraft:block.poplar_leaves.hit", 0.8F, 1.0F),
                new BiomeSoundEntry("minecraft:sulfur_caves", "minecraft:block.potent_sulfur.geyser_eruption_active", 0.8F, 1.0F)
        ));
    }

    public List<BiomeSoundEntry> customBiomeSounds = createDefaultBiomeSounds();

    // Biome Particle Settings
    public boolean enableBiomeParticles = true;
    public int biomeParticleDensity = 100;
    public float biomeParticleOpacity = 0.75F;
    public int biomeParticleMinSize = 1;
    public int biomeParticleMaxSize = 3;
    public int biomeParticleDurationMS = 4000;
    public float biomeParticleAnimationSpeed = 1.0F;
    public int wanderingParticlesSpeed = 100;
    public boolean enableBiomeParticleTransitions = true;
    public int biomeParticleTransitionDurationMS = 500;

    private BiomeConfig() {}

    public static void load() {
        if (Files.exists(FILE)) {
            try {
                BiomeConfig loaded = GSON.fromJson(Files.readString(FILE), BiomeConfig.class);
                if (loaded != null) INSTANCE = loaded;
                if (INSTANCE.customBiomeSounds == null) INSTANCE.customBiomeSounds = new ArrayList<>();
            } catch (Exception e) {
                TravellersTitles.LOGGER.error("Could not load config: {}", e.getMessage());
            }
        }

        save();
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            TravellersTitles.LOGGER.error("Could not save config: {}", e.getMessage());
        }
    }
}