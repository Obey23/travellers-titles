
package com.obqy.mixin;

import com.obqy.access.AlphaAccess;
import com.obqy.biomes.BiomeNotification;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderState.class)
public abstract class GuiRenderStateMixin {
    @Inject(method = "addItem", at = @At("HEAD"))
    private void travellersTitles$setNotificationAlpha(
            GuiItemRenderState itemState,
            CallbackInfo ci
    ) {
        if (BiomeNotification.isRenderingNotificationIcon()) {
            ((AlphaAccess) (Object) itemState)
                    .travellersTitles$setAlpha(BiomeNotification.getAlpha());
        }
    }
}
