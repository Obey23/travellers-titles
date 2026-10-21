package com.obqy.biomes;

import java.util.Map;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record BiomeInfo(BiomeRarity rarity, BiomeSize size, Item icon) {
    public enum BiomeRarity {
        COMMON("travellers_titles.rarity.common"),
        UNCOMMON("travellers_titles.rarity.uncommon"),
        RARE("travellers_titles.rarity.rare"),
        UNKNOWN("travellers_titles.rarity.unknown");

        private final String translationKey;

        BiomeRarity(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

    public enum BiomeSize {
        SMALL("travellers_titles.size.small"),
        NORMAL("travellers_titles.size.normal"),
        LARGE("travellers_titles.size.large"),
        UNKNOWN("travellers_titles.size.unknown");

        private final String translationKey;

        BiomeSize(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

    private static final BiomeInfo UNKNOWN =
            new BiomeInfo(BiomeRarity.UNKNOWN, BiomeSize.UNKNOWN, Items.GRASS_BLOCK);

    private static final Map<String, BiomeInfo> BIOMES = Map.ofEntries(
            // Common Overworld biomes
            entry("plains", BiomeRarity.COMMON, BiomeSize.LARGE, Items.GRASS_BLOCK),
            entry("forest", BiomeRarity.COMMON, BiomeSize.LARGE, Items.OAK_SAPLING),
            entry("birch_forest", BiomeRarity.COMMON, BiomeSize.NORMAL, Items.BIRCH_SAPLING),
            entry("taiga", BiomeRarity.COMMON, BiomeSize.LARGE, Items.SPRUCE_SAPLING),
            entry("snowy_taiga", BiomeRarity.COMMON, BiomeSize.LARGE, Items.SNOW_BLOCK),
            entry("savanna", BiomeRarity.COMMON, BiomeSize.LARGE, Items.ACACIA_SAPLING),
            entry("desert", BiomeRarity.COMMON, BiomeSize.LARGE, Items.DEAD_BUSH),
            entry("snowy_plains", BiomeRarity.COMMON, BiomeSize.LARGE, Items.SNOW_BLOCK),

            // Oceans
            entry("ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.COD_BUCKET),
            entry("deep_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.COD_BUCKET),
            entry("cold_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.SALMON_BUCKET),
            entry("deep_cold_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.SALMON_BUCKET),
            entry("lukewarm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.PUFFERFISH_BUCKET),
            entry("deep_lukewarm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.PUFFERFISH_BUCKET),
            entry("warm_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.TROPICAL_FISH_BUCKET),
            entry("frozen_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.ICE),
            entry("deep_frozen_ocean", BiomeRarity.COMMON, BiomeSize.LARGE, Items.PACKED_ICE),

            // Small transitional biomes
            entry("river", BiomeRarity.COMMON, BiomeSize.SMALL, Items.COD_BUCKET),
            entry("frozen_river", BiomeRarity.COMMON, BiomeSize.SMALL, Items.ICE),
            entry("beach", BiomeRarity.COMMON, BiomeSize.SMALL, Items.SAND),
            entry("snowy_beach", BiomeRarity.COMMON, BiomeSize.SMALL, Items.SNOW_BLOCK),
            entry("stony_shore", BiomeRarity.COMMON, BiomeSize.SMALL, Items.STONE),

            // Uncommon Overworld biomes
            entry("meadow", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.LILY_OF_THE_VALLEY),
            entry("grove", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SNOW_BLOCK),
            entry("windswept_hills", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.GRASS_BLOCK),
            entry("windswept_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SPRUCE_SAPLING),
            entry("windswept_gravelly_hills", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.GRAVEL),
            entry("windswept_savanna", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.ACACIA_SAPLING),
            entry("savanna_plateau", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.ACACIA_SAPLING),
            entry("cherry_grove", BiomeRarity.UNCOMMON, BiomeSize.SMALL, Items.CHERRY_SAPLING),
            entry("bamboo_jungle", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.BAMBOO),
            entry("sparse_jungle", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.COCOA_BEANS),
            entry("jungle", BiomeRarity.UNCOMMON, BiomeSize.LARGE, Items.JUNGLE_SAPLING),
            entry("swamp", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SLIME_BLOCK),
            entry("mangrove_swamp", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.MUD),
            entry("dark_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.DARK_OAK_SAPLING),
            entry("badlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.RED_SAND),
            entry("wooded_badlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.DEAD_BUSH),
            entry("eroded_badlands", BiomeRarity.RARE, BiomeSize.NORMAL, Items.DYED_TERRACOTTA.red()),
            entry("snowy_slopes", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SNOW_BLOCK),
            entry("frozen_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.BLUE_ICE),
            entry("jagged_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.GOAT_HORN),
            entry("stony_peaks", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.STONE),
            entry("old_growth_pine_taiga", BiomeRarity.UNCOMMON, BiomeSize.LARGE, Items.PODZOL),
            entry("old_growth_spruce_taiga", BiomeRarity.UNCOMMON, BiomeSize.LARGE, Items.MOSSY_COBBLESTONE),
            entry("old_growth_birch_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.BIRCH_SAPLING),
            entry("flower_forest", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.POPPY),
            entry("sunflower_plains", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SUNFLOWER),

            // Rare Overworld biomes
            entry("mushroom_fields", BiomeRarity.RARE, BiomeSize.SMALL, Items.RED_MUSHROOM_BLOCK),
            entry("ice_spikes", BiomeRarity.RARE, BiomeSize.NORMAL, Items.PACKED_ICE),
            entry("pale_garden", BiomeRarity.RARE, BiomeSize.NORMAL, Items.OPEN_EYEBLOSSOM),
            entry("dappled_forest", BiomeRarity.RARE, BiomeSize.NORMAL, Items.RED_SHRUB),

            // Underground
            entry("dripstone_caves", BiomeRarity.COMMON, BiomeSize.NORMAL, Items.POINTED_DRIPSTONE),
            entry("lush_caves", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SPORE_BLOSSOM),
            entry("deep_dark", BiomeRarity.RARE, BiomeSize.NORMAL, Items.SCULK_SHRIEKER),
            entry("sulfur_caves", BiomeRarity.RARE, BiomeSize.NORMAL, Items.POTENT_SULFUR),
            //entry("ice_caves", BiomeRarity.RARE, BiomeSize.NORMAL),

            // Nether
            entry("nether_wastes", BiomeRarity.COMMON, BiomeSize.LARGE, Items.NETHERRACK),
            entry("crimson_forest", BiomeRarity.UNCOMMON, BiomeSize.LARGE, Items.CRIMSON_FUNGUS),
            entry("warped_forest", BiomeRarity.UNCOMMON, BiomeSize.LARGE, Items.WARPED_FUNGUS),
            entry("soul_sand_valley", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.SOUL_SAND),
            entry("basalt_deltas", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.BASALT),

            // The End
            entry("the_end", BiomeRarity.COMMON, BiomeSize.LARGE, Items.DRAGON_EGG),
            entry("end_highlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.ELYTRA),
            entry("end_midlands", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.PURPUR_BLOCK),
            entry("end_barrens", BiomeRarity.UNCOMMON, BiomeSize.NORMAL, Items.END_STONE),
            entry("small_end_islands", BiomeRarity.UNCOMMON, BiomeSize.SMALL, Items.END_STONE)
    );

    private static Map.Entry<String, BiomeInfo> entry(
            String id,
            BiomeRarity rarity,
            BiomeSize size
    ) {
        return entry(id, rarity, size, Items.GRASS_BLOCK);
    }

    private static Map.Entry<String, BiomeInfo> entry(
            String id,
            BiomeRarity rarity,
            BiomeSize size,
            Item icon
    ) {
        return Map.entry(
                "minecraft:" + id,
                new BiomeInfo(rarity, size, icon)
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