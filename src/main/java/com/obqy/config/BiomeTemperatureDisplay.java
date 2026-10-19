package com.obqy.config;

import net.minecraft.network.chat.Component;

import java.util.Locale;

public final class BiomeTemperatureDisplay {

    private BiomeTemperatureDisplay() {}

    public static Component format(float temperature) {
        BiomeConfig config = BiomeConfig.INSTANCE;

        return switch (config.temperatureDisplayMode) {
            case MINECRAFT -> Component.literal(String.format(Locale.ROOT, "%.1f", temperature));
            case REAL_WORLD -> formatRealWorld(temperature, config.temperatureUnit);
            case FIRES -> formatFires(temperature);
        };
    }

    private static Component formatRealWorld(float temperature, TemperatureUnit unit) {
        double celsius = (temperature - 0.8) * 25.0 + 20.0;

        if (unit == TemperatureUnit.FAHRENHEIT) {
            double fahrenheit = celsius * 9.0 / 5.0 + 32.0;
            return Component.literal(String.format(Locale.ROOT, "%.1f°F", fahrenheit));
        }

        return Component.literal(String.format(Locale.ROOT, "%.1f°C", celsius));
    }

    private static Component formatFires(float temperature) {
        if (temperature < 0.0f) return Component.literal("❄");

        int fireCount;

        if (temperature < 0.5f) fireCount = 1;
        else if (temperature < 0.9f) fireCount = 2;
        else if (temperature < 1.2f) fireCount = 3;
        else if (temperature < 1.5f) fireCount = 4;
        else fireCount = 5;

        int color = switch (fireCount) {
            case 1 -> 0x6EC8FF;
            case 2 -> 0x52A7E8;
            case 3 -> 0xF5D65E;
            case 4 -> 0xFF9B45;
            default -> 0xF15B35;
        };

        return Component.literal("🔥".repeat(fireCount))
                .withColor(color);
    }
}