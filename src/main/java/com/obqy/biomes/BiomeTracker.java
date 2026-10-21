package com.obqy.biomes;

import com.obqy.biomes.effects.BiomeParticleEffect;
import com.obqy.config.BiomeConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeTracker {
    private static ResourceKey<Biome> lastBiome = null;
    private static ResourceKey<Biome> pendingBiome = null;
    private static long pendingSince = 0L;
    private static long lastNotificationTime = 0L;

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

            long now = System.currentTimeMillis();

            BiomeConfig config = BiomeConfig.INSTANCE;

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

    private static void handleNewBiome(Minecraft client, ResourceKey<Biome> biomeKey, Holder<Biome> biomeHolder) {
        String translationKey = "biome." + biomeKey.identifier().toString().replace(":", ".");
        Biome biome = biomeHolder.value();
        BiomeInfo info = BiomeInfo.get(biomeKey);
        Component size = Component.translatable(info.size().getTranslationKey());
        Component title = Component.translatable(translationKey).withColor(BiomeColourResolver.getColour(biomeKey, biomeHolder));
        Component temperatureComponent = BiomeTemperatureDisplay.format(biome.getBaseTemperature());

        Component rarityComponent;
        if (BiomeConfig.INSTANCE.rarityAsStars) {
            rarityComponent = switch (info.rarity()) {
                case COMMON -> Component.literal("★").withColor(0xf5ecd0);
                case UNCOMMON -> Component.literal("★★").withColor(0xeddfb7);
                case RARE -> Component.literal("★★★").withColor(0xf5dd98);
                case UNKNOWN -> Component.translatable(
                        info.rarity().getTranslationKey()
                );
            };
        } else rarityComponent = Component.translatable(info.rarity().getTranslationKey());

        Component subtitle = Component.empty()
                .append(Component.translatable("travellers_titles.temperature"))
                .append(Component.literal(": "))
                .append(temperatureComponent)
                .append(Component.literal("  |  "))
                .append(Component.translatable("travellers_titles.rarity"))
                .append(Component.literal(": "))
                .append(rarityComponent)
                .append(Component.literal("  |  "))
                .append(Component.translatable("travellers_titles.size"))
                .append(Component.literal(": "))
                .append(size);

        if (BiomeConfig.INSTANCE.enableBiomeParticles) {
            int foliageColour = biome.getFoliageColor();
            BiomeParticleEffect.start(biomeKey.identifier().toString(), foliageColour);
        }

        BiomeNotification.show(title, subtitle);
        BiomeSoundManager.playBiomeSound(client, biomeKey.identifier().toString(), info.rarity());
    }
}