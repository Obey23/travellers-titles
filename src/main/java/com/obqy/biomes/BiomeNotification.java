
package com.obqy.biomes;

import com.obqy.config.BiomeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.TextAlignment;

public final class BiomeNotification {
    private static Component title = Component.empty();
    private static Component subtitle = Component.empty();

    private static long expiresAt = 0L;

    private static final long DISPLAY_TIME_MS = 5000L;
    private static final long FADE_IN_TIME_MS = 300L;
    private static final long FADE_TIME_MS = 1000L;

    private BiomeNotification() {}

    public static void register() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(
                        "travellers-titles",
                        "biome_notification"
                ),
                BiomeNotification::render
        );
    }

    public static void show(Component newTitle, Component newSubtitle) {
        title = newTitle;
        subtitle = newSubtitle;
        expiresAt = System.currentTimeMillis() + DISPLAY_TIME_MS;
    }

    public static void clear() {
        title = Component.empty();
        subtitle = Component.empty();
        expiresAt = 0L;
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null || System.currentTimeMillis() >= expiresAt || title.getString().isEmpty()) return;

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        double guiScale = client.getWindow().getGuiScale();
        float guiScaleFactor = (float) (guiScale / 3.0);

        BiomeConfig config = BiomeConfig.INSTANCE;
        float titleScale = config.titleDefaultSize
                * (config.scaleWithGuiScale
                ? guiScaleFactor
                : config.titleScale);
        float subtitleScale = config.subtitleDefaultSize
                * (config.scaleWithGuiScale
                ? guiScaleFactor
                : config.subtitleScale);

        int subtitleGap = 4;

        int titleHeight = Math.round(9 * titleScale);
        int subtitleHeight = !subtitle.getString().isEmpty()
                ? Math.round(9 * subtitleScale)
                : 0;
        int totalHeight = titleHeight + (!subtitle.getString().isEmpty() ? subtitleGap + subtitleHeight : 0);

        int titleY;
        if (BiomeConfig.INSTANCE.notificationAtTop) titleY = 10;
        else titleY = Math.round((screenHeight - totalHeight) / 2.0f);
        int subtitleY = titleY + titleHeight + subtitleGap;

        long remaining = expiresAt - System.currentTimeMillis();
        long elapsed = DISPLAY_TIME_MS - remaining;

        if (remaining < 0) {
            clear();
            return;
        }

        float fadeInAlpha = Math.min(1.0f, (float) elapsed / FADE_IN_TIME_MS);
        float fadeOutAlpha = remaining < FADE_TIME_MS ? (float) remaining / FADE_TIME_MS : 1.0f;
        float alpha = Math.clamp(Math.min(fadeInAlpha, fadeOutAlpha), 0.0f, 1.0f );

        renderSection(graphics, screenWidth, titleScale, titleY, alpha, title);

        if (!subtitle.getString().isEmpty()) {
            renderSection(graphics, screenWidth, subtitleScale, subtitleY, alpha, subtitle);
        }
    }

    private static void renderSection(GuiGraphicsExtractor graphics, int screenWidth, float scale, int thisY, float alpha, Component component) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(screenWidth / 2.0f, thisY);
        graphics.pose().scale(scale, scale);

        ActiveTextCollector renderer = graphics.textRenderer();
        ActiveTextCollector.Parameters parameters = renderer.defaultParameters().withOpacity(alpha);

        renderer.accept(TextAlignment.CENTER, 0, 0, parameters, component);

        graphics.pose().popMatrix();
    }
}