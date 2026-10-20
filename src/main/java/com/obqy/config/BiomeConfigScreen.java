
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
                .setTitle(Component.translatable("biome_tracker.config.title"))
                .setSavingRunnable(BiomeConfig::save);

        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory generalCategory = builder.getOrCreateCategory(Component.translatable("biome_tracker.config.category.general"));

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("biome_tracker.config.gui_scale"), config.scaleWithGuiScale)
                        .setDefaultValue(true)
                        .setTooltip(Component.translatable("biome_tracker.config.gui_scale.tooltip"))
                        .setSaveConsumer(value -> config.scaleWithGuiScale = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("biome_tracker.config.notification_at_top"), BiomeConfig.INSTANCE.notificationAtTop)
                        .setDefaultValue(false)
                        .setTooltip(Component.translatable("biome_tracker.config.notification_at_top.tooltip"))
                        .setSaveConsumer(value -> BiomeConfig.INSTANCE.notificationAtTop = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startBooleanToggle(Component.translatable("biome_tracker.config.rarity_as_stars"), BiomeConfig.INSTANCE.rarityAsStars)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> BiomeConfig.INSTANCE.rarityAsStars = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startIntField(Component.translatable("biome_tracker.config.confirmation_delay"), BiomeConfig.INSTANCE.biomeConfirmationDelayMs)
                        .setDefaultValue(1500)
                        .setMin(0)
                        .setMax(10000)
                        .setSaveConsumer(value -> BiomeConfig.INSTANCE.biomeConfirmationDelayMs = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startIntField(Component.translatable("biome_tracker.config.notification_cooldown"), BiomeConfig.INSTANCE.biomeNotificationCooldownMs)
                        .setDefaultValue(2000)
                        .setMin(0)
                        .setMax(30000)
                        .setSaveConsumer(value -> BiomeConfig.INSTANCE.biomeNotificationCooldownMs = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startEnumSelector(Component.translatable("biome_tracker.config.temperature_display_mode"), TemperatureDisplayMode.class, config.temperatureDisplayMode)
                        .setDefaultValue(TemperatureDisplayMode.MINECRAFT)
                        .setEnumNameProvider(value -> switch (value) {
                            case TemperatureDisplayMode.MINECRAFT -> Component.translatable("biome_tracker.config.temperature_display_mode.minecraft");
                            case TemperatureDisplayMode.REAL_WORLD -> Component.translatable("biome_tracker.config.temperature_display_mode.real_world");
                            case TemperatureDisplayMode.FIRES -> Component.translatable("biome_tracker.config.temperature_display_mode.fires");
                            default -> throw new IllegalStateException("Unexpected value: " + value);
                        })
                        .setSaveConsumer(value -> config.temperatureDisplayMode = value)
                        .build()
        );

        generalCategory.addEntry(
                entries.startEnumSelector(Component.translatable("biome_tracker.config.temperature_unit"), TemperatureUnit.class, config.temperatureUnit)
                        .setDefaultValue(TemperatureUnit.CELSIUS)
                        .setEnumNameProvider(value -> switch (value) {
                            case TemperatureUnit.CELSIUS -> Component.translatable("biome_tracker.config.temperature_unit.celsius");
                            case TemperatureUnit.FAHRENHEIT -> Component.translatable("biome_tracker.config.temperature_unit.fahrenheit");
                            default -> throw new IllegalStateException("Unexpected value: " + value);
                        })
                        .setSaveConsumer(value -> config.temperatureUnit = value)
                        .build()
        );

        ConfigCategory titleCategory = builder.getOrCreateCategory(Component.translatable("biome_tracker.config.category.title"));

        titleCategory.addEntry(
                entries.startFloatField(Component.translatable("biome_tracker.config.title.default_size"), config.titleDefaultSize)
                        .setDefaultValue(3.5F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.titleDefaultSize = value)
                        .build()
        );

        titleCategory.addEntry(
                entries.startFloatField(Component.translatable("biome_tracker.config.title.scale"), config.titleScale)
                        .setDefaultValue(1.0F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.titleScale = value)
                        .build()
        );

        ConfigCategory subtitleCategory = builder.getOrCreateCategory(Component.translatable("biome_tracker.config.category.subtitle"));

        subtitleCategory.addEntry(
                entries.startFloatField(Component.translatable("biome_tracker.config.subtitle.default_size"), config.subtitleDefaultSize)
                        .setDefaultValue(1.25F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.subtitleDefaultSize = value)
                        .build()
        );

        subtitleCategory.addEntry(
                entries.startFloatField(Component.translatable("biome_tracker.config.subtitle.scale"), config.subtitleScale)
                        .setDefaultValue(1.0F)
                        .setMin(0.25F)
                        .setMax(5.0F)
                        .setSaveConsumer(value -> config.subtitleScale = value)
                        .build()
        );

        ConfigCategory soundCategory = builder.getOrCreateCategory(Component.translatable("biome_tracker.config.category.sounds"));

        soundCategory.addEntry(
                new NestedListListEntry<BiomeSoundEntry, MultiElementListEntry<BiomeSoundEntry>>(
                        Component.translatable("biome_tracker.config.sounds.entries"),
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
                                                                    "biome_tracker.config.sounds.biome_id"
                                                            ), value.biomeId)
                                                    .setSaveConsumer(v -> value.biomeId = v.trim())
                                                    .build(),

                                            entries.startStrField(Component.translatable(
                                                                    "biome_tracker.config.sounds.sound_id"
                                                            ), value.soundId)
                                                    .setSaveConsumer(v -> value.soundId = v.trim())
                                                    .build(),

                                            entries.startFloatField(Component.translatable(
                                                                    "biome_tracker.config.sounds.volume"
                                                            ), value.volume)
                                                    .setMin(0.0F)
                                                    .setMax(2.0F)
                                                    .setSaveConsumer(v -> value.volume = v)
                                                    .build(),

                                            entries.startFloatField(Component.translatable(
                                                                    "biome_tracker.config.sounds.pitch"
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
