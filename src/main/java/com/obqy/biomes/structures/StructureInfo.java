package com.obqy.biomes.structures;

import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.List;

public record StructureInfo(Identifier id, Identifier icon, List<Identifier> biomes) {
    // Underground
    public static final StructureInfo TRAIL_RUINS = create(
            "underground",
            "trail_ruins",
            "jungle", "old_growth_birch_forest", "old_growth_pine_taiga",
            "old_growth_spruce_taiga", "snowy_taiga", "taiga"
    );
    // Above Ground
    public static final StructureInfo ABANDONED_CAMP = create(
            "above_ground",
            "abandoned_camp",
            "meadow", "swamp", "savanna",
            "cherry_grove", "forest", "birch_forest",
            "old_growth_birch_forest", "flower_forest", "windswept_forest",
            "dappled_forest", "taiga", "snowy_taiga",
            "old_growth_spruce_taiga", "old_growth_pine_taiga", "sparse_jungle",
            "bamboo_jungle", "wooded_badlands", "pale_garden"
    );
    public static final StructureInfo IGLOO = create(
            "above_ground",
            "igloo",
            "snowy_plains", "snowy_taiga", "snowy_slopes"
    );
    public static final StructureInfo JUNGLE_PYRAMID = create(
            "above_ground",
            "jungle_pyramid",
            "jungle", "bamboo_jungle"
    );
    public static final StructureInfo PILLAGER_OUTPOST = create(
            "above_ground",
            "pillager_outpost",
            "plains", "desert", "savanna",
            "taiga", "snowy_plains", "grove",
            "meadow", "cherry_grove", "jagged_peaks",
            "frozen_peaks", "stony_peaks", "snowy_slopes"
    );
    public static final StructureInfo SWAMP_HUT = create(
            "above_ground",
            "swamp_hut",
            "swamp"
    );
    public static final StructureInfo WOODLAND_MANSION = create(
            "above_ground",
            "woodland_mansion",
            "dark_forest",
            "pale_garden"
    );
    // Villages
    public static final StructureInfo VILLAGE_DESERT = create(
            "above_ground",
            "village_desert",
            "desert"
    );
    public static final StructureInfo VILLAGE_PLAINS = create(
            "above_ground",
            "village_plains",
            "plains", "meadow"
    );
    public static final StructureInfo VILLAGE_SAVANNA = create(
            "above_ground",
            "village_savanna",
            "savanna"
    );
    public static final StructureInfo VILLAGE_SNOWY_PLAINS = create(
            "above_ground",
            "village_snowy_plains",
            "snowy_plains"
    );
    public static final StructureInfo VILLAGE_TAIGA = create(
            "above_ground",
            "village_taiga",
            "taiga"
    );
    // Underwater
    public static final StructureInfo COLD_OCEAN_RUINS = create(
            "underwater",
            "cold_ocean_ruins",
            "deep_ocean", "deep_frozen_ocean", "deep_cold_ocean",
            "ocean", "frozen_ocean", "cold_ocean"
    );
    public static final StructureInfo WARM_OCEAN_RUINS = create(
            "underwater",
            "warm_ocean_ruins",
            "deep_lukewarm_ocean", "warm_ocean", "lukewarm_ocean"
    );
    public static final StructureInfo OCEAN_MONUMENT = create(
            "underwater",
            "ocean_monument",
            "deep_ocean", "deep_frozen_ocean", "deep_cold_ocean",
            "deep_lukewarm_ocean"
    );
    public static final StructureInfo SHIPWRECK = create(
            "underwater",
            "shipwreck",
            "deep_ocean", "deep_frozen_ocean", "deep_cold_ocean",
            "deep_lukewarm_ocean", "ocean", "frozen_ocean",
            "cold_ocean", "warm_ocean", "lukewarm_ocean",
            "beach", "snowy_beach"
    );
    // The Nether
    public static final StructureInfo BASTION_REMNANT = create(
            "the_nether",
            "bastion_remnant",
            "nether_wastes", "crimson_forest", "warped_forest",
            "soul_sand_valley"
    );
    public static final StructureInfo NETHER_FORTRESS = create(
            "the_nether",
            "nether_fortress",
            "nether_wastes", "soul_sand_valley", "crimson_forest",
            "warped_forest", "basalt_deltas"
    );
    public static final StructureInfo NETHER_FOSSIL = create(
            "the_nether",
            "nether_fossil",
            "soul_sand_valley"
    );
    // The End
    public static final StructureInfo END_CITY = create(
            "the_end",
            "end_city",
            "end_midlands", "end_highlands"
    );
    // Features
    public static final StructureInfo BEE_NEST = create(
            "features",
            "bee_nest",
            "meadow", "plains", "sunflower_plains",
            "cherry_grove", "mangrove_swamp", "flower_forest",
            "forest", "birch_forest", "old_growth_birch_forest"
    );
    public static final StructureInfo DESERT_WELL = create(
            "features",
            "desert_well",
            "desert"
    );
    public static final StructureInfo FOREST_ROCK = create(
            "features",
            "forest_rock",
            "old_growth_pine_taiga", "old_growth_spruce_taiga"
    );
    public static final StructureInfo FOSSIL = create(
            "features",
            "fossil",
            "desert", "swamp", "mangrove_swamp"
    );

    private static StructureInfo create(String category, String name, String... biomes) {
        return new StructureInfo(
                Identifier.withDefaultNamespace(name),
                Identifier.fromNamespaceAndPath(
                        "travellers-titles",
                        "textures/gui/structures/" + category + "/" + name + ".png"
                ),
                Arrays.stream(biomes)
                        .map(Identifier::withDefaultNamespace)
                        .toList()
        );
    }

    public static final List<StructureInfo> STRUCTURES = List.of(
            TRAIL_RUINS,
            ABANDONED_CAMP,
            IGLOO,
            JUNGLE_PYRAMID,
            PILLAGER_OUTPOST,
            SWAMP_HUT,
            WOODLAND_MANSION,
            VILLAGE_DESERT,
            VILLAGE_PLAINS,
            VILLAGE_SAVANNA,
            VILLAGE_SNOWY_PLAINS,
            VILLAGE_TAIGA,
            COLD_OCEAN_RUINS,
            WARM_OCEAN_RUINS,
            OCEAN_MONUMENT,
            SHIPWRECK,
            BASTION_REMNANT,
            NETHER_FORTRESS,
            NETHER_FOSSIL,
            END_CITY,
            BEE_NEST,
            DESERT_WELL,
            FOREST_ROCK,
            FOSSIL
    );

    public static List<StructureInfo> getForBiome(Identifier biomeId) {
        return STRUCTURES.stream()
                .filter(structure -> structure.biomes().contains(biomeId))
                .toList();
    }
}