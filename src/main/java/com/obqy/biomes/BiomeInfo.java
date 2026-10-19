
package com.obqy.biomes;

import java.util.Map;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public record BiomeInfo(BiomeRarity rarity, BiomeSize size) {
    public enum BiomeRarity {
        COMMON("biome_tracker.rarity.common"),
        UNCOMMON("biome_tracker.rarity.uncommon"),
        RARE("biome_tracker.rarity.rare"),
        UNKNOWN("biome_tracker.rarity.unknown");

        private final String translationKey;

        BiomeRarity(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

    public enum BiomeSize {
        SMALL("biome_tracker.size.small"),
        NORMAL("biome_tracker.size.normal"),
        LARGE("biome_tracker.size.large"),
        UNKNOWN("biome_tracker.size.unknown");

        private final String translationKey;

        BiomeSize(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

    private static final BiomeInfo UNKNOWN =
            new BiomeInfo(BiomeRarity.UNKNOWN, BiomeSize.UNKNOWN);

    private static final Map<String, BiomeInfo> BIOMES = Map.ofEntries(
            // Common Overworld biomes
            entry("plains", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("forest", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("birch_forest", BiomeRarity.COMMON, BiomeSize.NORMAL),
            entry("taiga", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("snowy_taiga", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("savanna", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("desert", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("snowy_plains", BiomeRarity.COMMON, BiomeSize.LARGE),

            // Oceans
            entry("ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("deep_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("cold_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("deep_cold_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("lukewarm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("deep_lukewarm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("warm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("frozen_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("deep_frozen_ocean", BiomeRarity.COMMON, BiomeSize.LARGE),

            // Small transitional biomes
            entry("river", BiomeRarity.COMMON, BiomeSize.SMALL),
            entry("frozen_river", BiomeRarity.COMMON, BiomeSize.SMALL),
            entry("beach", BiomeRarity.COMMON, BiomeSize.SMALL),
            entry("snowy_beach", BiomeRarity.COMMON, BiomeSize.SMALL),
            entry("stony_shore", BiomeRarity.COMMON, BiomeSize.SMALL),

            // Uncommon Overworld biomes
            entry("meadow", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("grove", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("windswept_hills", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("windswept_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("windswept_gravelly_hills", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("windswept_savanna", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("cherry_grove", BiomeRarity.UNCOMMON, BiomeSize.SMALL),
            entry("bamboo_jungle", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("sparse_jungle", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("jungle", BiomeRarity.UNCOMMON, BiomeSize.LARGE),
            entry("swamp", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("mangrove_swamp", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("dark_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("badlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("wooded_badlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("eroded_badlands", BiomeRarity.RARE, BiomeSize.NORMAL),
            entry("snowy_slopes", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("frozen_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("jagged_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("stony_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("old_growth_pine_taiga", BiomeRarity.UNCOMMON, BiomeSize.LARGE),
            entry("old_growth_spruce_taiga", BiomeRarity.UNCOMMON, BiomeSize.LARGE),
            entry("old_growth_birch_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("flower_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("sunflower_plains", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),

            // Rare Overworld biomes
            entry("mushroom_fields", BiomeRarity.RARE, BiomeSize.SMALL),
            entry("ice_spikes", BiomeRarity.RARE, BiomeSize.NORMAL),
            entry("pale_garden", BiomeRarity.RARE, BiomeSize.NORMAL),
            entry("dappled_forest", BiomeRarity.RARE, BiomeSize.NORMAL),

            // Underground
            entry("dripstone_caves", BiomeRarity.COMMON, BiomeSize.NORMAL),
            entry("lush_caves", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("deep_dark", BiomeRarity.RARE, BiomeSize.NORMAL),

            // Nether
            entry("nether_wastes", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("crimson_forest", BiomeRarity.UNCOMMON, BiomeSize.LARGE),
            entry("warped_forest", BiomeRarity.UNCOMMON, BiomeSize.LARGE),
            entry("soul_sand_valley", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("basalt_deltas", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),

            // The End
            entry("the_end", BiomeRarity.COMMON, BiomeSize.LARGE),
            entry("end_highlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("end_midlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("end_barrens", BiomeRarity.UNCOMMON, BiomeSize.NORMAL),
            entry("small_end_islands", BiomeRarity.UNCOMMON, BiomeSize.SMALL)
    );

    private static Map.Entry<String, BiomeInfo> entry(
            String id,
            BiomeRarity rarity,
            BiomeSize size
    ) {
        return Map.entry(
                "minecraft:" + id,
                new BiomeInfo(rarity, size)
        );
    }

    public static BiomeInfo get(ResourceKey<Biome> biomeKey) {
        if (biomeKey == null) {
            return UNKNOWN;
        }

        String id = biomeKey.identifier().toString();
        return BIOMES.getOrDefault(id, UNKNOWN);
    }
}