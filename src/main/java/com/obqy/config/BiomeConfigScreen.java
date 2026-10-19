
package com.obqy.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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

        return builder.build();
    }
}
