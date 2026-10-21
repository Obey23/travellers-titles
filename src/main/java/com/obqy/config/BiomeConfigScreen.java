package com.obqy.config;

import com.obqy.biomes.BiomeSoundEntry;
import com.obqy.biomes.TemperatureDisplayMode;
import com.obqy.biomes.TemperatureUnit;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BiomeConfigScreen {
    private BiomeConfigScreen() {}

    public static Screen build(Screen parent) {
        BiomeConfig config = BiomeConfig.INSTANCE;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("travellers_titles.config.title"))
                .setSavingRunnable(BiomeConfig::save);

        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory generalCategory = builder.getOrCreateCategory(Component.translatable("travellers_titles.config.category.general"));

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("travellers_titles.config.gui_scale"), config.scaleWithGuiScale)
                        .setDefaultValue(true)
                        .setTooltip(Component.translatable("travellers_titles.config.gui_scale.tooltip"))
                        .setSaveConsumer(value -> config.scaleWithGuiScale = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("travellers_titles.config.notification_at_top"), BiomeConfig.INSTANCE.notificationAtTop)
                        .setDefaultValue(false)
                        .setTooltip(Component.translatable("travellers_titles.config.notification_at_top.tooltip"))
                        .setSaveConsumer(value -> BiomeConfig.INSTANCE.notificationAtTop = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("travellers_titles.config.rarity_as_stars"), config.rarityAsStars)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.rarityAsStars = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.confirmation_delay"), config.biomeConfirmationDelayMs, 0, 10000)
                        .setDefaultValue(1500)
                        .setSaveConsumer(value -> config.biomeConfirmationDelayMs = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.notification_cooldown"), config.biomeNotificationCooldownMs, 0, 30000)
                        .setDefaultValue(2000)
                        .setSaveConsumer(value -> config.biomeNotificationCooldownMs = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startEnumSelector(Component.translatable("travellers_titles.config.temperature_display_mode"), TemperatureDisplayMode.class, config.temperatureDisplayMode)
                        .setDefaultValue(TemperatureDisplayMode.MINECRAFT)
                        .setEnumNameProvider(value -> Component.translatable("travellers_titles.config.temperature_display_mode." + value.name().toLowerCase(java.util.Locale.ROOT)))
                        .setSaveConsumer(value -> config.temperatureDisplayMode = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startEnumSelector(Component.translatable("travellers_titles.config.temperature_unit"), TemperatureUnit.class, config.temperatureUnit)
                        .setDefaultValue(TemperatureUnit.CELSIUS)
                        .setEnumNameProvider(value -> Component.translatable("travellers_titles.config.temperature_unit." + value.name().toLowerCase(java.util.Locale.ROOT)))
                        .setSaveConsumer(value -> config.temperatureUnit = value)
                        .build()
        );

        ConfigCategory titleCategory = builder.getOrCreateCategory(Component.translatable("travellers_titles.config.category.title"));

        titleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.title.default_size"), config.titleDefaultSize)
                        .setDefaultValue(3.5F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.titleDefaultSize = value)
                        .build()
        );

        titleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.title.scale"), config.titleScale)
                        .setDefaultValue(1.0F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.titleScale = value)
                        .build()
        );

        ConfigCategory subtitleCategory = builder.getOrCreateCategory(Component.translatable("travellers_titles.config.category.subtitle"));

        subtitleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.subtitle.default_size"), config.subtitleDefaultSize)
                        .setDefaultValue(1.25F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.subtitleDefaultSize = value)
                        .build()
        );

        subtitleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.subtitle.scale"), config.subtitleScale)
                        .setDefaultValue(1.0F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.subtitleScale = value)
                        .build()
        );

        ConfigCategory particleCategory = builder.getOrCreateCategory(Component.translatable("travellers_titles.config.category.particles"));

        particleCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("travellers_titles.config.particles.enable"), config.enableBiomeParticles)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableBiomeParticles = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.particles.density"), config.biomeParticleDensity, 0, 1000)
                        .setDefaultValue(100)
                        .setSaveConsumer(value -> config.biomeParticleDensity = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.particles.opacity"), config.biomeParticleOpacity)
                        .setDefaultValue(0.75F)
                        .setMin(0.0F)
                        .setMax(1.0F)
                        .setSaveConsumer(value -> config.biomeParticleOpacity = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.particles.min_size"), config.biomeParticleMinSize, 1, 10)
                        .setDefaultValue(1)
                        .setSaveConsumer(value -> config.biomeParticleMinSize = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.particles.max_size"), config.biomeParticleMaxSize,1, 10)
                        .setDefaultValue(3)
                        .setSaveConsumer(value -> config.biomeParticleMaxSize = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.particles.duration_ms"), config.biomeParticleDurationMS, 500, 15000)
                        .setDefaultValue(4000)
                        .setSaveConsumer(value -> config.biomeParticleDurationMS = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startFloatField(Component.translatable("travellers_titles.config.particles.animation_speed"), config.biomeParticleAnimationSpeed)
                        .setDefaultValue(1.0F)
                        .setMin(0.1F)
                        .setMax(3.0F)
                        .setSaveConsumer(value -> config.biomeParticleAnimationSpeed = value)
                        .build()
        );

        particleCategory.addEntry(
                entries.startIntSlider(Component.translatable("travellers_titles.config.particles.firefly_speed"), config.wanderingParticlesSpeed, 1, 200)
                        .setDefaultValue(100)
                        .setSaveConsumer(value -> config.wanderingParticlesSpeed = value)
                        .build()
        );

        ConfigCategory soundCategory = builder.getOrCreateCategory(Component.translatable("travellers_titles.config.category.sounds"));

        soundCategory.addEntry(
                new NestedListListEntry<BiomeSoundEntry, MultiElementListEntry<BiomeSoundEntry>>(
                        Component.translatable("travellers_titles.config.sounds.entries"),
                        new ArrayList<>(config.customBiomeSounds),
                        true,
                        Optional::empty,
                        list -> config.customBiomeSounds = new ArrayList<>(list),
                        BiomeConfig::createDefaultBiomeSounds,
                        entries.getResetButtonKey(),
                        true,
                        true,
                        (sound, _) -> {
                            BiomeSoundEntry value = sound != null ? sound : new BiomeSoundEntry(
                                    "minecraft:a_biome",
                                    "minecraft:some.minecraft.sound",
                                    0.8F,
                                    1.0F
                            );

                            return new MultiElementListEntry<>(
                                    Component.literal(value.biomeId == null ? "Biome sound" : value.biomeId),
                                    value,
                                    List.of(
                                            entries.startStrField(Component.translatable(
                                                                    "travellers_titles.config.sounds.biome_id"
                                                            ), value.biomeId)
                                                    .setSaveConsumer(v -> value.biomeId = v.trim())
                                                    .build(),

                                            entries.startStrField(Component.translatable(
                                                                    "travellers_titles.config.sounds.sound_id"
                                                            ), value.soundId)
                                                    .setSaveConsumer(v -> value.soundId = v.trim())
                                                    .build(),

                                            entries.startFloatField(Component.translatable(
                                                                    "travellers_titles.config.sounds.volume"
                                                            ), value.volume)
                                                    .setMin(0.0F)
                                                    .setMax(2.0F)
                                                    .setSaveConsumer(v -> value.volume = v)
                                                    .build(),

                                            entries.startFloatField(Component.translatable(
                                                                    "travellers_titles.config.sounds.pitch"
                                                            ), value.pitch)
                                                    .setMin(0.5F)
                                                    .setMax(2.0F)
                                                    .setSaveConsumer(v -> value.pitch = v)
                                                    .build()
                                    ),
                                    true
                            );
                        }
                )
        );

        return builder.build();
    }
}
