package com.obqy.biomes.effects;

import com.obqy.TravellersTitles;
import com.mojang.blaze3d.platform.NativeImage;
import com.obqy.config.BiomeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class BiomeParticleEffect {
    static BiomeConfig config = BiomeConfig.INSTANCE;

    private static final Random RANDOM = new Random();
    private static final List<Particle> PARTICLES = new ArrayList<>();
    private static final Map<Identifier, ParticleTexture> TEXTURE_CACHE = new HashMap<>();

    private static long startTime;
    private static boolean active;
    private static BiomeEffectInfo currentEffect;
    private static int currentFoliageColour = 0xFFFFFF;

    private record Particle(
            List<ParticleTexture> textures,
            float x,
            float y,
            int size,
            float opacity,
            int tint,
            float fallSpeed,
            float swayAmount,
            float swaySpeed,
            float rotationSpeed,
            float rotationOffset,
            long animationStartTime,
            long movementSeed,
            float glowPhase,
            float glowSpeed,
            float movementSpeed
    ) {}

    private record ParticleTexture(
            Identifier id,
            int width,
            int height
    ) {}

    private BiomeParticleEffect() {}

    public static void register() {
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("travellers-titles", "biome_particle_effect"),
                (graphics, _) -> render(graphics)
        );
    }

    public static void start(String biomeId, int foliageColour) {
        BiomeEffectInfo effect = BiomeEffectInfo.get(biomeId);

        if (effect == null) {
            stop();
            return;
        }

        currentEffect = effect;
        currentFoliageColour = foliageColour;
        startTime = System.currentTimeMillis();
        active = true;

        generateParticles();
    }

    public static void stop() {
        active = false;
        currentEffect = null;
        PARTICLES.clear();
    }

    private static ParticleTexture loadTexture(Identifier id) {
        ParticleTexture cached = TEXTURE_CACHE.get(id);
        if (cached != null) return cached;

        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(id);
            if (resource.isEmpty()) {
                TravellersTitles.LOGGER.error("Missing particle texture: {}", id);
                return null;
            }

            try (InputStream stream = resource.get().open(); NativeImage image = NativeImage.read(stream)) {
                ParticleTexture texture = new ParticleTexture(id, image.getWidth(), image.getHeight());
                TEXTURE_CACHE.put(id, texture);
                return texture;
            }
        } catch (IOException e) {
            TravellersTitles.LOGGER.error("Failed to load particle texture {}: {}", id, e.getMessage());
            return null;
        }
    }

    private static void generateParticles() {
        PARTICLES.clear();
        if (currentEffect == null) return;
        List<ParticleTextureSet> textureSets = new ArrayList<>();

        for (ParticleTextureSet set : currentEffect.particleTextureSets()) {
            if (set != null && set.textures() != null && !set.textures().isEmpty() && set.weight() > 0) {
                textureSets.add(set);
            }
        }
        ParticleTextureSet popSet = currentEffect.popTextureSet();

        if (popSet != null
                && popSet.textures() != null
                && !popSet.textures().isEmpty()
                && popSet.weight() > 0) {
            textureSets.add(popSet);
        }

        if (textureSets.isEmpty()) return;

        int minSize = Math.max(1, Math.min(config.biomeParticleMinSize, config.biomeParticleMaxSize));
        int maxSize = Math.max(minSize, Math.max(config.biomeParticleMinSize, config.biomeParticleMaxSize));
        int particleCount = Math.max(0, Math.round(config.biomeParticleDensity * currentEffect.densityMultiplier()));

        for (int i = 0; i < particleCount; i++) {
            int totalWeight = textureSets.stream().mapToInt(ParticleTextureSet::weight).sum();
            if (totalWeight <= 0) break;
            int choice = RANDOM.nextInt(totalWeight);
            ParticleTextureSet selectedSet = null;

            for (ParticleTextureSet set : textureSets) {
                choice -= set.weight();
                if (choice < 0) {
                    selectedSet = set;
                    break;
                }
            }
            if (selectedSet == null) continue;

            boolean popping = selectedSet == popSet;
            List<ParticleTexture> textures = new ArrayList<>();

            if (popping) {
                assert selectedSet.textures() != null;
                for (String name : selectedSet.textures()) {
                    Identifier id = Identifier.fromNamespaceAndPath(selectedSet.namespace(), selectedSet.directory() + name);
                    ParticleTexture texture = loadTexture(id);
                    if (texture != null) textures.add(texture);
                }
            } else {
                List<String> names = selectedSet.textures();
                String name = names.get(RANDOM.nextInt(names.size()));
                Identifier id = Identifier.fromNamespaceAndPath(selectedSet.namespace(), selectedSet.directory() + name);
                ParticleTexture texture = loadTexture(id);
                if (texture != null) textures.add(texture);
            }

            if (textures.isEmpty()) continue;
            int size = (minSize + RANDOM.nextInt(maxSize - minSize + 1)) * 3;
            int tint = selectedSet.useFoliageTint() ? varyColour(currentFoliageColour) : 0xFFFFFF;

            PARTICLES.add(new Particle(
                    List.copyOf(textures),
                    RANDOM.nextFloat(),
                    RANDOM.nextFloat(),
                    size,
                    0.55F + RANDOM.nextFloat() * 0.45F,
                    tint,
                    randomBetween(15.0F, 50.0F),
                    randomBetween(10.0F, 40.0F),
                    randomBetween(0.5F, 2.0F),
                    randomBetween(-90.0F, 90.0F),
                    RANDOM.nextFloat() * 360.0F,
                    popping ? System.currentTimeMillis() : 0L,
                    RANDOM.nextLong(),
                    RANDOM.nextFloat() * (float) (Math.PI * 2.0),
                    randomBetween(0.4F, 0.9F),
                    randomBetween(0.8F, 1.2F)
            ));
        }
    }

    private static float randomBetween(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }

    private static int varyColour(int colour) {
        float brightness = 0.85F + RANDOM.nextFloat() * 0.30F;

        int red = Math.min(255, Math.round(((colour >> 16) & 0xFF) * brightness));
        int green = Math.min(255, Math.round(((colour >> 8) & 0xFF) * brightness));
        int blue = Math.min(255, Math.round((colour & 0xFF) * brightness));

        return (red << 16) | (green << 8) | blue;
    }

    private static float wanderTarget(Particle particle, int segment, boolean vertical) {
        if (segment == 0) return vertical ? particle.y() : particle.x();
        Random random = new Random(particle.movementSeed() + segment * 0x9E3779B97F4A7C15L);
        float x = random.nextFloat();
        float y = random.nextFloat();
        return vertical ? y : x;
    }

    private static float wanderPosition(Particle particle, float elapsedSeconds, boolean vertical) {
        float speed = (float) Math.clamp(config.wanderingParticlesSpeed, 1, 200) / 100;

        // Slightly random speeds
        float segmentDuration = 4.0F / (speed * particle.movementSpeed());

        int segment = (int) (elapsedSeconds / segmentDuration);
        float progress = (elapsedSeconds % segmentDuration) / segmentDuration;

        progress = progress * progress * (3.0F - 2.0F * progress);

        float start = wanderTarget(particle, segment, vertical);
        float end = wanderTarget(particle, segment + 1, vertical);

        return start + (end - start) * progress;
    }

    private static void render(GuiGraphicsExtractor graphics) {
        if (!active || currentEffect == null) return;

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed >= config.biomeParticleDurationMS) {
            stop();
            return;
        }

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        float elapsedSeconds = elapsed / 1000.0F * config.biomeParticleAnimationSpeed;

        float fadeIn = Math.min(1.0F, elapsed / 300.0F);
        float fadeOut = Math.min(1.0F, (config.biomeParticleDurationMS - elapsed) / 500.0F);

        float alpha = Math.clamp(config.biomeParticleOpacity * fadeIn * fadeOut, 0.0F, 1.0F);

        for (Particle particle : PARTICLES) {
            float xPosition;
            float yPosition;

            if (currentEffect.movementType() == BiomeEffectInfo.MovementType.WANDERING) {
                xPosition = wanderPosition(particle, elapsedSeconds, false) * screenWidth;
                yPosition = wanderPosition(particle, elapsedSeconds, true) * screenHeight;
            } else {
                float distance = particle.fallSpeed() * elapsedSeconds;
                if (currentEffect.rises()) yPosition = Math.floorMod(Math.round(particle.y() * screenHeight - distance), screenHeight + particle.size());
                else yPosition = (particle.y() * screenHeight + distance) % (screenHeight + particle.size());
                float sway = (float) Math.sin(elapsedSeconds * particle.swaySpeed() + particle.rotationOffset()) * particle.swayAmount();
                xPosition = particle.x() * screenWidth + sway;
            }

            float particleAlpha = alpha * particle.opacity();
            if (currentEffect.pulsingGlow()) {
                float pulse = 0.35F + 0.65F * (0.5F + 0.5F * (float) Math.sin(elapsedSeconds * particle.glowSpeed() + particle.glowPhase()));
                particleAlpha *= pulse;
            }

            int alphaByte = Math.round(particleAlpha * 255.0F);
            int tint = (alphaByte << 24) | particle.tint();

            float rotation = particle.rotationOffset() + particle.rotationSpeed() * elapsedSeconds;

            ParticleTexture texture;
            if (particle.textures().size() > 1) {
                long animationElapsed = System.currentTimeMillis() - particle.animationStartTime();
                int frame = (int) (animationElapsed / Math.max(1L, currentEffect.popFrameDurationMs()));
                if (frame < 0 || frame >= particle.textures().size()) continue;
                texture = particle.textures().get(frame);
            } else texture = particle.textures().getFirst();

            int maxDimension = Math.max(texture.width(), texture.height());
            float scale = particle.size() / (float) maxDimension;
            int displayWidth = Math.max(1, Math.round(texture.width() * scale));
            int displayHeight = Math.max(1, Math.round(texture.height() * scale));

            graphics.pose().pushMatrix();
            graphics.pose().translate(xPosition, yPosition);
            graphics.pose().rotate((float) Math.toRadians(rotation));
            graphics.pose().translate(-displayWidth / 2.0F,-displayHeight / 2.0F);

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    texture.id(),
                    0,
                    0,
                    0.0F,
                    0.0F,
                    displayWidth,
                    displayHeight,
                    texture.width(),
                    texture.height(),
                    texture.width(),
                    texture.height(),
                    tint
            );

            graphics.pose().popMatrix();
        }
    }
}