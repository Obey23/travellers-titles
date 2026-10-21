package com.obqy.mixin;

import com.obqy.access.AlphaAccess;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
    @Unique
    private static float travellersTitles$currentAlpha = 1.0F;

    @Inject(
            method = "submitBlitFromItemAtlas",
            at = @At("HEAD")
    )
    private void travellersTitles$captureAlpha(
            GuiItemRenderState itemState,
            net.minecraft.client.gui.render.GuiItemAtlas.SlotView slotView,
            CallbackInfo ci
    ) {
        travellersTitles$currentAlpha =
                ((AlphaAccess) (Object) itemState).travellersTitles$getAlpha();
    }

    @Redirect(
            method = "submitBlitFromItemAtlas",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addBlitToCurrentLayer(Lnet/minecraft/client/renderer/state/gui/BlitRenderState;)V"
            )
    )
    private void travellersTitles$fadeNotificationIcon(
            GuiRenderState renderState,
            BlitRenderState blit
    ) {
        int colour = blit.color();
        float opacity = travellersTitles$currentAlpha;

        int red = Math.round(((colour >> 16) & 0xFF) * opacity);
        int green = Math.round(((colour >> 8) & 0xFF) * opacity);
        int blue = Math.round((colour & 0xFF) * opacity);
        int alpha = Math.round(((colour >>> 24) & 0xFF) * opacity);

        colour = (alpha << 24) | (red << 16) | (green << 8) | blue;

        BlitRenderState faded = new BlitRenderState(
                blit.pipeline(),
                blit.textureSetup(),
                blit.pose(),
                blit.x0(),
                blit.y0(),
                blit.x1(),
                blit.y1(),
                blit.u0(),
                blit.u1(),
                blit.v0(),
                blit.v1(),
                (colour & 0x00FFFFFF) | (alpha << 24),
                blit.scissorArea(),
                blit.bounds()
        );

        renderState.addBlitToCurrentLayer(faded);
    }
}