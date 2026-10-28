package com.obqy.biomes;

import com.obqy.biomes.effects.BiomeParticleEffect;
import com.obqy.biomes.sounds.BiomeSoundManager;
import com.obqy.config.BiomeConfig;
import com.obqy.config.enums.UndergroundDetectionMode;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomeTracker {
    private static ResourceKey<Biome> lastBiome = null;
    private static ResourceKey<Biome> pendingBiome = null;
    private static long pendingSince = 0L;
    private static long lastNotificationTime = 0L;
    private static final BiomeConfig config = BiomeConfig.INSTANCE;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) {
                lastBiome = null;
                pendingBiome = null;
                pendingSince = 0L;
                return;
            }

            var level = client.player.level();
            var pos = client.player.blockPosition();

            var biome = level.getBiome(pos);
            var biomeKey = biome.unwrapKey().orElse(null);
            if (biomeKey == null) return;

            if (!shouldTrackBiome(client, biome)) {
                lastBiome = null;
                pendingBiome = null;
                pendingSince = 0L;
                return;
            }

            long now = System.currentTimeMillis();

            long confirmationDelay = Math.max(0, config.biomeConfirmationDelayMs);
            long cooldown = Math.max(0, config.biomeNotificationCooldownMs);

            // Biome hasn't changed
            if (biomeKey.equals(lastBiome)) {
                pendingBiome = null;
                pendingSince = 0L;
                return;
            }

            // Entered a new biome, start timer
            if (!biomeKey.equals(pendingBiome)) {
                pendingBiome = biomeKey;
                pendingSince = now;
                return;
            }

            // Wait out the timer
            if (now - pendingSince < confirmationDelay) {
                return;
            }

            // Check cooldown
            if (now - lastNotificationTime < cooldown) {
                return;
            }

            // Announce after all checks
            lastBiome = biomeKey;
            lastNotificationTime = now;
            pendingBiome = null;
            pendingSince = 0L;

            handleNewBiome(client, biomeKey, biome);
        });
    }

    private static boolean shouldTrackBiome(Minecraft client, Holder<Biome> biome) {
        if (config.undergroundDetectionMode == UndergroundDetectionMode.NONE) return true;
        // Underground biomes are exempt
        if (isUndergroundBiome(biome)) return true;
        // If not in overworld, always track
        if (client.level.dimension() != Level.OVERWORLD) return true;

        var level = client.player.level();
        var pos = client.player.blockPosition();

        return switch (config.undergroundDetectionMode) {
            case NONE -> true;
            case CAN_SEE_SKY -> level.canSeeSky(pos);
            case SKY_LIGHT -> level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) >= config.undergroundSkyLightThreshold;
            case Y_LEVEL -> pos.getY() >= config.undergroundMinYLevel;
        };
    }

    private static boolean isUndergroundBiome(Holder<Biome> biome) {
        return biome.unwrapKey().map(key ->
            switch (key.identifier().toString()) {
                case "minecraft:deep_dark",
                     "minecraft:dripstone_caves",
                     "minecraft:lush_caves",
                     "minecraft:sulfur_caves" -> true;
                    default -> false;
                })
                .orElse(false);
    }

    private static void handleNewBiome(Minecraft client, ResourceKey<Biome> biomeKey, Holder<Biome> biomeHolder) {
        String translationKey = "biome." + biomeKey.identifier().toString().replace(":", ".");
        Biome biome = biomeHolder.value();
        BiomeInfo info = BiomeInfo.get(biomeKey);
        Component size = Component.translatable(info.size().getTranslationKey());
        Component title = Component.translatable(translationKey).withColor(BiomeColourResolver.getColour(biomeKey, biomeHolder));
        Component temperatureComponent = BiomeTemperatureDisplay.format(biome.getBaseTemperature());

        Component rarityComponent;
        if (config.rarityAsStars) {
            rarityComponent = switch (info.rarity()) {
                case COMMON -> Component.literal("★").withColor(0xf5ecd0);
                case UNCOMMON -> Component.literal("★★").withColor(0xeddfb7);
                case RARE -> Component.literal("★★★").withColor(0xf5dd98);
                case UNKNOWN -> Component.translatable(
                        info.rarity().getTranslationKey()
                );
            };
        } else rarityComponent = Component.translatable(info.rarity().getTranslationKey());

        MutableComponent subtitle = Component.empty();
        if (config.showTemperature) {
            subtitle.append(Component.translatable("travellers_titles.temperature"))
                    .append(Component.literal(": "))
                    .append(temperatureComponent);
        }

        if (config.showRarity) {
            if (!subtitle.getString().isEmpty()) subtitle.append(Component.literal("  |  "));
            subtitle.append(Component.translatable("travellers_titles.rarity"))
                    .append(Component.literal(": "))
                    .append(rarityComponent);
        }

        if (config.showSize) {
            if (!subtitle.getString().isEmpty()) subtitle.append(Component.literal("  |  "));
            subtitle.append(Component.translatable("travellers_titles.size"))
                    .append(Component.literal(": "))
                    .append(size);
        }

        if (config.enableBiomeParticles) {
            int foliageColour = biome.getFoliageColor();
            BiomeParticleEffect.start(biomeKey.identifier().toString(), foliageColour);
        }

        BiomeNotification.show(title, subtitle, info, biomeKey.identifier());
        if (config.enableBiomeSounds) BiomeSoundManager.playBiomeSound(client, biomeHolder, info.rarity());
    }
}