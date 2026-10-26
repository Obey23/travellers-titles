package com.obqy.biomes;

import com.obqy.config.BiomeConfig;
import com.obqy.config.enums.NotificationPosition;
import com.obqy.biomes.structures.StructureInfo;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;

public final class BiomeNotification {
    private static boolean renderingNotificationIcon;
    private static Component title = Component.empty();
    private static Component subtitle = Component.empty();
    private static Item icon = Items.GRASS_BLOCK;
    private static List<StructureInfo> structureIcons = List.of();
    private static final int POPUP_STRUCTURE_ICON_SIZE = 16;
    private static final int TOAST_STRUCTURE_ICON_SIZE = 8;
    private static final int STRUCTURE_ICON_GAP = 2;
    private static final int TOAST_STRUCTURE_LABEL_GAP = 3;
    private static long expiresAt = 0L;

    private static final long DISPLAY_TIME_MS = 5000L;
    private static final long FADE_IN_TIME_MS = 300L;
    private static final long FADE_TIME_MS = 1000L;

    private static final int MARGIN = 10;
    private static final int TOAST_WIDTH = 160;
    private static final int TOAST_ICON_SIZE = 20;
    private static final int TOAST_TEXT_GAP = 6;
    private static final int SUBTITLE_GAP = 4;
    private static final Identifier TOAST_SPRITE = Identifier.withDefaultNamespace("toast/advancement");

    private BiomeNotification() {}

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath( "travellers-titles","biome_notification"), BiomeNotification::render);
    }

    public static boolean isRenderingNotificationIcon() {
        return renderingNotificationIcon;
    }

    public static void show(Component newTitle, Component newSubtitle, BiomeInfo info, Identifier biomeId) {
        title = newTitle;
        subtitle = newSubtitle;
        icon = info.icon();
        structureIcons = StructureInfo.getForBiome(biomeId);
        expiresAt = System.currentTimeMillis() + DISPLAY_TIME_MS;
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        BiomeConfig config = BiomeConfig.INSTANCE;

        if (client.player == null || client.level == null) return;
        long now = System.currentTimeMillis();
        if (now >= expiresAt || title.getString().isEmpty()) return;

        float alpha = calculateAlpha(now);
        if (alpha <= 0.0F) return;

        float sizeMultiplier = (config.showAsToast ? config.toastSize : config.popupSize) / 100.0F;
        if (sizeMultiplier <= 0.0F) return;

        float titleScale = (config.showAsToast ? 1.0F : 3.5F) * sizeMultiplier;
        float subtitleScale = (config.showAsToast ? 0.5F : 1.25F) * sizeMultiplier;

        int titleHeight = Math.round(9 * titleScale);
        int subtitleHeight = Math.round(9 * subtitleScale);
        int contentHeight = titleHeight;

        if (config.showAsToast) {
            if (!subtitle.getString().isEmpty()) contentHeight = Math.round(11 * sizeMultiplier) + subtitleHeight;
            if (!structureIcons.isEmpty() && config.showStructures) {
                if (!subtitle.getString().isEmpty()) contentHeight += Math.round(SUBTITLE_GAP * sizeMultiplier);
                contentHeight += Math.round(TOAST_STRUCTURE_ICON_SIZE * sizeMultiplier);
            }
        } else {
            if (!subtitle.getString().isEmpty()) contentHeight += subtitleHeight + SUBTITLE_GAP;
            if (!structureIcons.isEmpty() && config.showStructures) {
                contentHeight += SUBTITLE_GAP + POPUP_STRUCTURE_ICON_SIZE;
                if (!subtitle.getString().isEmpty()) contentHeight += 4;
            }
        }

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();
        NotificationPosition position = config.showAsToast ? config.toastPosition : NotificationPosition.TOP_MIDDLE;
        TextAlignment alignment = getAlignment(position);

        int x;
        int titleY;

        if (!config.showAsToast) {
            x = screenWidth / 2;
            titleY = (screenHeight - contentHeight) / 2;
        } else {
            int panelWidth = Math.round(TOAST_WIDTH * sizeMultiplier);
            int panelHeight = Math.round(12 * sizeMultiplier) + contentHeight;

            int panelX = switch (position) {
                case TOP_LEFT, LEFT, BOTTOM_LEFT -> MARGIN;
                case TOP_RIGHT, RIGHT, BOTTOM_RIGHT ->
                        screenWidth - panelWidth - MARGIN;
                default -> (screenWidth - panelWidth) / 2;
            };

            int panelY = switch (position) {
                case TOP_LEFT, TOP_MIDDLE, TOP_RIGHT -> MARGIN;
                case BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT ->
                        screenHeight - panelHeight - MARGIN;
                default -> (screenHeight - panelHeight) / 2;
            };

            panelX = Math.clamp(panelX, 0, Math.max(0, screenWidth - panelWidth));
            panelY = Math.clamp(panelY, 0, Math.max(0, screenHeight - panelHeight));

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TOAST_SPRITE, panelX, panelY, panelWidth, panelHeight, alpha);
            graphics.pose().pushMatrix();
            graphics.pose().translate(panelX, panelY);
            graphics.pose().scale(sizeMultiplier, sizeMultiplier);
            try {
                renderingNotificationIcon = true;
                graphics.fakeItem(new ItemStack(icon), 6, Math.round((panelHeight / sizeMultiplier - 16) / 2.0F));
            } finally {
                renderingNotificationIcon = false;
            }
            graphics.pose().popMatrix();

            x = panelX + Math.round((TOAST_ICON_SIZE + TOAST_TEXT_GAP) * sizeMultiplier);
            titleY = panelY + (panelHeight - contentHeight) / 2;
            alignment = TextAlignment.LEFT;
        }
        renderSection(graphics, x, alignment, titleScale, titleY, alpha, title);
        if (!subtitle.getString().isEmpty()) {
            int subtitleY = titleY + (config.showAsToast ? Math.round(11 * sizeMultiplier) : titleHeight + SUBTITLE_GAP);
            renderSection(graphics, x, alignment, subtitleScale, subtitleY, alpha, subtitle);
        }

        if (!structureIcons.isEmpty()) {
            int iconSize = config.showAsToast ? TOAST_STRUCTURE_ICON_SIZE : POPUP_STRUCTURE_ICON_SIZE;
            int iconsWidth = structureIcons.size() * (iconSize + STRUCTURE_ICON_GAP) - STRUCTURE_ICON_GAP;
            int iconsY;

            if (config.showAsToast) {
                iconsY = titleY + Math.round(11 * sizeMultiplier);
                if (!subtitle.getString().isEmpty()) iconsY += subtitleHeight + Math.round(SUBTITLE_GAP * sizeMultiplier);

                graphics.pose().pushMatrix();
                graphics.pose().translate(x, iconsY);
                graphics.pose().scale(subtitleScale, subtitleScale);

                ActiveTextCollector renderer = graphics.textRenderer();
                ActiveTextCollector.Parameters parameters = renderer.defaultParameters().withOpacity(alpha);
                renderer.accept(TextAlignment.LEFT, 0, 0, parameters, Component.literal("Generates:"));

                graphics.pose().popMatrix();
            } else {
                iconsY = titleY + titleHeight;
                if (!subtitle.getString().isEmpty()) iconsY += subtitleHeight;
                iconsY += SUBTITLE_GAP + (!subtitle.getString().isEmpty() ? 4 : 0);
            }

            graphics.pose().pushMatrix();
            if (config.showAsToast) {
                int labelWidth = Math.round(Minecraft.getInstance().font.width(Component.literal("Generates:")) * subtitleScale);
                int labelHeight = Math.round(9 * subtitleScale);
                int iconY = iconsY + (labelHeight - Math.round(iconSize * sizeMultiplier)) / 2 - 1;
                graphics.pose().translate(x + labelWidth + Math.round(TOAST_STRUCTURE_LABEL_GAP * sizeMultiplier), iconY);
            } else graphics.pose().translate((screenWidth - iconsWidth * sizeMultiplier) / 2.0F, iconsY);
            graphics.pose().scale(sizeMultiplier, sizeMultiplier);
            for (int i = 0; i < structureIcons.size(); i++) {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        structureIcons.get(i).icon(),
                        i * (iconSize + STRUCTURE_ICON_GAP),
                        0,
                        0,
                        0,
                        iconSize,
                        iconSize,
                        iconSize,
                        iconSize,
                        withAlpha(alpha)
                );
            }
            graphics.pose().popMatrix();
        }
    }

    private static TextAlignment getAlignment(NotificationPosition position) {
        return switch (position) {
            case TOP_LEFT, LEFT, BOTTOM_LEFT -> TextAlignment.LEFT;
            case TOP_RIGHT, RIGHT, BOTTOM_RIGHT -> TextAlignment.RIGHT;
            default -> TextAlignment.CENTER;
        };
    }

    private static float calculateAlpha(long now) {
        long remaining = expiresAt - now;
        long elapsed = DISPLAY_TIME_MS - remaining;

        if (remaining <= 0L) return 0.0F;
        if (remaining < FADE_TIME_MS) return Math.clamp(remaining / (float) FADE_TIME_MS, 0.0F, 1.0F);
        if (elapsed < FADE_IN_TIME_MS) return Math.clamp(elapsed / (float) FADE_IN_TIME_MS, 0.0F, 1.0F);
        return 1.0F;
    }

    public static float getAlpha() {
        return expiresAt == 0L ? 0.0F : calculateAlpha(System.currentTimeMillis());
    }

    private static void renderSection(GuiGraphicsExtractor graphics, float x, TextAlignment alignment, float scale, int y, float alpha, Component component) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        ActiveTextCollector renderer = graphics.textRenderer();
        ActiveTextCollector.Parameters parameters = renderer.defaultParameters().withOpacity(alpha);
        renderer.accept(alignment, 0, 0, parameters, component);
        graphics.pose().popMatrix();
    }

    private static int withAlpha(float opacity) {
        int originalAlpha = (-1 >>> 24) & 0xFF;
        int alpha = Math.round(originalAlpha * opacity);

        return (0x00FFFFFF) | (alpha << 24);
    }
}