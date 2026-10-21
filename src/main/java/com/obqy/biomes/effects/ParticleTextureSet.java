package com.obqy.biomes.effects;

import java.util.List;

public record ParticleTextureSet(
        String namespace,
        String directory,
        List<String> textures,
        int weight,
        boolean useFoliageTint
) {}