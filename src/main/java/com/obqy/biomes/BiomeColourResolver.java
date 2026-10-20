
package com.obqy.biomes;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class BiomeColourResolver {

    private BiomeColourResolver() {}

    public static int getColour(ResourceKey<Biome> biomeKey, Holder<Biome> biome) {
        String id = biomeKey.identifier().toString();
        String path = id.substring(id.indexOf(':') + 1);
        float temperature = biome.value().getBaseTemperature();

        // Oceans: cold navy -> warm turquoise.
        if (path.contains("ocean") || path.contains("river")) {
            return temperatureColour(
                    temperature, -0.5f, 1.0f,
                    0x163A70, 0x50E0CF
            );
        }

        // Swamps: muted teal -> vivid marsh green.
        if (path.contains("swamp")
                || path.contains("mangrove")) {
            return temperatureColour(
                    temperature, 0.0f, 1.0f,
                    0x386B67, 0x77C95B
            );
        }

        // Dappled Forests: burnt orange -> golden amber
        if (path.contains("dappled_forest")) {
            return temperatureColour(
                    temperature, 0.0f, 1.2f,
                    0xC65D25, 0xFFCB63
            );
        }

        // Forests, taigas, jungles and bamboo jungles.
        if (path.contains("forest")
                || path.contains("taiga")
                || path.contains("jungle")) {
            return temperatureColour(
                    temperature, -0.5f, 1.2f,
                    0x24543B, 0x72D85A
            );
        }

        // Deserts and badlands.
        if (path.contains("desert")
                || path.contains("badlands")) {
            return temperatureColour(
                    temperature, 1.0f, 2.0f,
                    0xA77936, 0xFFE36B
            );
        }

        // Snowy and icy biomes.
        if (path.contains("snow")
                || path.contains("frozen")
                || path.contains("ice_spikes")
                || path.contains("jagged_peaks")
                || path.contains("stony_peaks")
                || path.contains("grove")) {
            return temperatureColour(
                    temperature, -0.7f, 0.5f,
                    0xBDEBFF, 0x2866A5
            );
        }

        // Mushroom fields.
        if (path.contains("mushroom")) {
            return temperatureColour(
                    temperature, 0.0f, 1.0f,
                    0x8156A8, 0xD58BE8
            );
        }

        // Nether biomes.
        if (path.startsWith("nether_wastes")
                || path.contains("crimson_forest")) {
            return 0xE65B63;
        }

        if (path.contains("warped_forest")) {
            return 0x40D6C2;
        }

        if (path.contains("soul_sand_valley")) {
            return 0xA6A3C9;
        }

        if (path.contains("basalt_deltas")) {
            return 0xB7A0B9;
        }

        // End biomes.
        if (path.startsWith("the_end")
                || path.contains("end_highlands")
                || path.contains("end_midlands")
                || path.contains("small_end_islands")
                || path.contains("end_barrens")) {
            return 0xC18BDB;
        }

        // Savannas: earthy yellow -> warm gold.
        if (path.contains("savanna")) {
            return temperatureColour(
                    temperature, 0.8f, 2.0f,
                    0xB7A34B, 0xFFE36B
            );
        }

        // Beaches: sand -> sunny gold.
        if (path.contains("beach")
                || path.contains("shore")) {
            return temperatureColour(
                    temperature, 0.0f, 1.5f,
                    0xC7B77B, 0xFFE58A
            );
        }

        // Default for plains and other unclassified biomes.
        return temperatureColour(
                temperature, -0.5f, 2.0f,
                0x77B8A0, 0xF4D66D
        );
    }

    private static int temperatureColour(
            float temperature,
            float minTemperature,
            float maxTemperature,
            int coldColour,
            int warmColour
    ) {
        float t = (temperature - minTemperature)
                / (maxTemperature - minTemperature);

        t = Math.clamp(t, 0.0f, 1.0f);

        int red = interpolate(
                (coldColour >> 16) & 0xFF,
                (warmColour >> 16) & 0xFF,
                t
        );

        int green = interpolate(
                (coldColour >> 8) & 0xFF,
                (warmColour >> 8) & 0xFF,
                t
        );

        int blue = interpolate(
                coldColour & 0xFF,
                warmColour & 0xFF,
                t
        );

        return (red << 16) | (green << 8) | blue;
    }

    private static int interpolate(int start, int end, float t) {
        return Math.round(start + (end - start) * t);
    }
}
