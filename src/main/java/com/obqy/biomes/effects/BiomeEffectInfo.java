package com.obqy.biomes.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record BiomeEffectInfo(
        List<ParticleTextureSet> particleTextureSets,
        ParticleTextureSet popTextureSet,
        boolean rises,
        long popFrameDurationMs,
        float densityMultiplier,
        MovementType movementType,
        boolean pulsingGlow
) {
    public enum MovementType {
        VERTICAL,
        WANDERING,
        RISING_WOBBLE
    }

    public BiomeEffectInfo(
            List<ParticleTextureSet> particleTextureSets,
            ParticleTextureSet popTextureSet,
            boolean rises,
            long popFrameDurationMs,
            float densityMultiplier
    ) {
        this(
                particleTextureSets,
                popTextureSet,
                rises,
                popFrameDurationMs,
                densityMultiplier,
                MovementType.VERTICAL,
                false
        );
    }

    private static List<String> indexed(String prefix, int first, int last) {
        List<String> textures = new ArrayList<>();
        for (int i = first; i <= last; i++) textures.add(prefix + i + ".png");
        return List.copyOf(textures);
    }

    private static List<String> combineIndexed(List<String> prefixes, int first, int last) {
        List<String> textures = new ArrayList<>();
        for (String prefix : prefixes) textures.addAll(indexed(prefix, first, last));
        return List.copyOf(textures);
    }

    private static List<String> indexedPadded(String prefix, int first, int last, int digits) {
        List<String> textures = new ArrayList<>();
        for (int i = first; i <= last; i++) textures.add(prefix + String.format("%0" + digits + "d", i) + ".png");
        return List.copyOf(textures);
    }

    private static ParticleTextureSet minecraftParticle(List<String> textures, int weight, boolean foliageTint) {
        return new ParticleTextureSet("minecraft", "textures/particle/", textures, weight, foliageTint);
    }

    // Regular Leaves tinted per-biome
    public static final BiomeEffectInfo LEAVES =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("leaf_", 0, 11), 100, true)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Pale Oak leaves
    public static final BiomeEffectInfo PALE_GARDEN =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("pale_oak_", 0, 11), 100, false)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Cherry Blossom leaves
    public static final BiomeEffectInfo CHERRY_GROVE =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("cherry_", 0, 11), 100, false)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Dappled Forest leaf variants
    public static final BiomeEffectInfo DAPPLED_FOREST =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(
                            combineIndexed(List.of("orange_poplar_", "red_poplar_", "yellow_poplar_"), 1, 4 ),
                            100,
                            false
                    )),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Flowers that generate in the Meadow
    public static final BiomeEffectInfo MEADOW =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/wildflowers/", indexed("wildflowers_", 0, 7), 16, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/dandelion/", indexed("dandelion_", 0, 2), 16, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/cornflower/", indexed("cornflower_", 0, 2), 16, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/poppy/", indexed("poppy_", 0, 2), 16, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/allium/", indexed("allium_", 0, 3), 20, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/oxeye_daisy/", indexed("oxeye_daisy_", 0, 3), 16, false)
                    ),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Flowers that generate in a Flower Forest
    public static final BiomeEffectInfo FLOWER_FOREST =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/dandelion/", indexed("dandelion_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/cornflower/", indexed("cornflower_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/poppy/", indexed("poppy_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/allium/", indexed("allium_", 0, 3), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/oxeye_daisy/", indexed("oxeye_daisy_", 0, 3), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/red_tulip/", indexed("red_tulip_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/orange_tulip/", indexed("orange_tulip_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/pink_tulip/", indexed("pink_tulip_", 0, 2), 3, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/white_tulip/", indexed("white_tulip_", 0, 2), 3, false),
                            minecraftParticle(indexed("leaf_", 0, 11), 73, true)
                    ),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Mainly the "Sunflower" texture plus flowers that generate there
    public static final BiomeEffectInfo SUNFLOWER_PLAINS =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/dandelion/", indexed("dandelion_", 0, 2), 40, false), // Should be sunflower, but no point having 2 nearly identical texture groups, nobody's gonna know yk?
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/cornflower/", indexed("cornflower_", 0, 2), 10, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/oxeye_daisy/", indexed("oxeye_daisy_", 0, 3), 10, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/red_tulip/", indexed("red_tulip_", 0, 2), 10, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/orange_tulip/", indexed("orange_tulip_", 0, 2), 10, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/pink_tulip/", indexed("pink_tulip_", 0, 2), 10, false),
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/white_tulip/", indexed("white_tulip_", 0, 2), 10, false)
                    ),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Flowers + Leaves, for Birch Forests
    public static final BiomeEffectInfo BIRCH_FOREST =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/wildflowers/", indexed("wildflowers_", 0, 7), 25, false),
                            minecraftParticle(indexed("leaf_", 0, 11), 75, true)
                    ),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Snow
    public static final BiomeEffectInfo SNOW =
            new BiomeEffectInfo(
                    List.of(new ParticleTextureSet("travellers-titles", "textures/biome_particles/snow/", indexed("snowflake_", 0, 3), 100, false)),
                    null,
                    false,
                    100L,
                    0.7F
            );

    // Snow + Leaves, for snowy forest biomes
    public static final BiomeEffectInfo SNOW_AND_LEAVES =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/snow/", indexed("snowflake_", 0, 3), 75, false),
                            minecraftParticle(indexed("leaf_", 0, 11), 25, true)
                    ),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Bubbles
    public static final BiomeEffectInfo BUBBLES =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(List.of("bubble.png"), 50, false)),
                    minecraftParticle(indexed("bubble_pop_", 0, 4), 50, false),
                    true,
                    100L,
                    0.75F,
                    MovementType.RISING_WOBBLE,
                    false
            );

    // Sculk souls and popping bubbles
    public static final BiomeEffectInfo SCULK =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("sculk_soul_", 0, 10), 80, false)),
                    minecraftParticle(indexed("sculk_charge_pop_", 0, 3), 20, false),
                    true,
                    200L,
                    0.5F
            );

    // Sulfur gas particles
    public static final BiomeEffectInfo NOXIOUS_GAS =
            new BiomeEffectInfo(
                    List.of(),
                    minecraftParticle(indexedPadded("noxious_gas_", 1, 8, 2), 100, false),
                    true,
                    250L,
                    0.25F
            );

    // Animated souls moving upward
    public static final BiomeEffectInfo SOUL_SAND_VALLEY =
            new BiomeEffectInfo(
                    List.of(),
                    minecraftParticle(indexed("soul_", 0, 10), 100, false),
                    true,
                    250L,
                    0.5F
            );

    // Fireflies moving sporadically
    public static final BiomeEffectInfo SWAMP =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(List.of("firefly.png"), 100, false)),
                    null,
                    false,
                    200L,
                    0.5F,
                    MovementType.WANDERING,
                    true
            );

    // Spore Blossom Textures
    public static final BiomeEffectInfo LUSH_CAVES =
            new BiomeEffectInfo(
                    List.of(new ParticleTextureSet("travellers-titles", "textures/biome_particles/lush/", indexed("spore_blossom_", 0, 7), 100, false)),
                    null,
                    false,
                    150L,
                    0.7F
            );

    private static final Map<String, BiomeEffectInfo> BIOMES =
            Map.ofEntries(
                    // Forest biomes
                    Map.entry("minecraft:forest", LEAVES),
                    Map.entry("minecraft:dark_forest", LEAVES),
                    Map.entry("minecraft:windswept_forest", LEAVES),
                    Map.entry("minecraft:taiga", LEAVES),
                    Map.entry("minecraft:old_growth_pine_taiga", LEAVES),
                    Map.entry("minecraft:old_growth_spruce_taiga", LEAVES),
                    Map.entry("minecraft:jungle", LEAVES),
                    Map.entry("minecraft:sparse_jungle", LEAVES),
                    Map.entry("minecraft:savanna", LEAVES),
                    Map.entry("minecraft:savanna_plateau", LEAVES),
                    Map.entry("minecraft:windswept_savanna", LEAVES),
                    Map.entry("minecraft:wooded_badlands", LEAVES),

                    // Swamps
                    Map.entry("minecraft:swamp", SWAMP),
                    Map.entry("minecraft:mangrove_swamp", SWAMP),

                    // Special forests
                    Map.entry("minecraft:flower_forest", FLOWER_FOREST),
                    Map.entry("minecraft:birch_forest", BIRCH_FOREST),
                    Map.entry("minecraft:old_growth_birch_forest", BIRCH_FOREST),
                    Map.entry("minecraft:dappled_forest", DAPPLED_FOREST),
                    Map.entry("minecraft:cherry_grove", CHERRY_GROVE),
                    Map.entry("minecraft:pale_garden", PALE_GARDEN),

                    // Misc overworld
                    Map.entry("minecraft:meadow", MEADOW),
                    Map.entry("minecraft:sunflower_plains", SUNFLOWER_PLAINS),
                    Map.entry("minecraft:lush_caves", LUSH_CAVES),

                    // Water biomes
                    Map.entry("minecraft:ocean", BUBBLES),
                    Map.entry("minecraft:deep_ocean", BUBBLES),
                    Map.entry("minecraft:cold_ocean", BUBBLES),
                    Map.entry("minecraft:deep_cold_ocean", BUBBLES),
                    Map.entry("minecraft:lukewarm_ocean", BUBBLES),
                    Map.entry("minecraft:deep_lukewarm_ocean", BUBBLES),
                    Map.entry("minecraft:warm_ocean", BUBBLES),

                    // Snowy biomes
                    Map.entry("minecraft:snowy_plains", SNOW),
                    Map.entry("minecraft:ice_spikes", SNOW),
                    Map.entry("minecraft:snowy_taiga", SNOW_AND_LEAVES),
                    Map.entry("minecraft:grove", SNOW_AND_LEAVES),
                    Map.entry("minecraft:snowy_slopes", SNOW),
                    Map.entry("minecraft:frozen_peaks", SNOW),
                    Map.entry("minecraft:jagged_peaks", SNOW),
                    Map.entry("minecraft:frozen_river", SNOW),
                    Map.entry("minecraft:frozen_ocean", SNOW),
                    Map.entry("minecraft:deep_frozen_ocean", SNOW),
                    Map.entry("minecraft:snowy_beach", SNOW),
                    //Map.entry("minecraft:ice_caves", SNOW),

                    // Special caves
                    Map.entry("minecraft:deep_dark", SCULK),
                    Map.entry("minecraft:sulfur_caves", NOXIOUS_GAS),

                    // Nether
                    Map.entry("minecraft:soul_sand_valley", SOUL_SAND_VALLEY)
            );

    public static BiomeEffectInfo get(String biomeId) {
        return BIOMES.get(biomeId);
    }
}