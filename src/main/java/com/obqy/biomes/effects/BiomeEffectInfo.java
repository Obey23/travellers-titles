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
        WANDERING
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

    // Leaves
    public static final BiomeEffectInfo LEAVES =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("leaf_", 0, 11), 100, true)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    public static final BiomeEffectInfo PALE_LEAVES =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("pale_oak_", 0, 11), 100, false)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    public static final BiomeEffectInfo CHERRY_PETALS =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("cherry_", 0, 11), 100, false)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    public static final BiomeEffectInfo AUTUMN_LEAVES =
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

    // Flowers
    public static final BiomeEffectInfo FLOWERS =
            new BiomeEffectInfo(
                    List.of(new ParticleTextureSet("travellers-titles", "textures/biome_particles/flowers/", indexed("flower_", 0, 7), 100, false)),
                    null,
                    false,
                    100L,
                    1.0F
            );

    // Flowers + Leaves, for Birch Forests
    public static final BiomeEffectInfo FLOWERS_AND_LEAVES =
            new BiomeEffectInfo(
                    List.of(
                            new ParticleTextureSet("travellers-titles", "textures/biome_particles/flowers/", indexed("flower_", 0, 7), 25, false),
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
                    0.75F
            );

    // Deep Dark
    public static final BiomeEffectInfo SCULK =
            new BiomeEffectInfo(
                    List.of(minecraftParticle(indexed("sculk_soul_", 0, 10), 80, false)),
                    minecraftParticle(indexed("sculk_charge_pop_", 0, 3), 20, false),
                    true,
                    200L,
                    0.5F
            );

    // Noxious Gas, animated particles only
    public static final BiomeEffectInfo NOXIOUS_GAS =
            new BiomeEffectInfo(
                    List.of(),
                    minecraftParticle(indexedPadded("noxious_gas_", 1, 8, 2), 100, false),
                    true,
                    250L,
                    0.25F
            );

    // Souls, animated particles only
    public static final BiomeEffectInfo SOULS =
            new BiomeEffectInfo(
                    List.of(),
                    minecraftParticle(indexed("soul_", 0, 10), 100, false),
                    true,
                    150L,
                    0.5F
            );

    // Fireflies
    public static final BiomeEffectInfo FIREFLIES =
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
    public static final BiomeEffectInfo LUSH =
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
                    Map.entry("minecraft:flower_forest", LEAVES),
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
                    Map.entry("minecraft:swamp", FIREFLIES),
                    Map.entry("minecraft:mangrove_swamp", FIREFLIES),

                    // Special forests
                    Map.entry("minecraft:birch_forest", FLOWERS_AND_LEAVES),
                    Map.entry("minecraft:old_growth_birch_forest", FLOWERS_AND_LEAVES),
                    Map.entry("minecraft:dappled_forest", AUTUMN_LEAVES),
                    Map.entry("minecraft:cherry_grove", CHERRY_PETALS),
                    Map.entry("minecraft:pale_garden", PALE_LEAVES),

                    // Misc overworld
                    Map.entry("minecraft:meadow", FLOWERS),
                    Map.entry("minecraft:sunflower_plains", FLOWERS),
                    Map.entry("minecraft:lush_caves", LUSH),

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
                    Map.entry("minecraft:soul_sand_valley", SOULS)
            );

    public static BiomeEffectInfo get(String biomeId) {
        return BIOMES.get(biomeId);
    }
}