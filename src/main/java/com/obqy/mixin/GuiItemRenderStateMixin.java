package com.obqy.mixin;

import com.obqy.access.AlphaAccess;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiItemRenderState.class)
public abstract class GuiItemRenderStateMixin implements AlphaAccess {
    @Unique
    private float travellersTitles$alpha = 1.0F;

    @Override
    @Unique
    public float travellersTitles$getAlpha() {
        return travellersTitles$alpha;
    }

    @Override
    @Unique
    public void travellersTitles$setAlpha(float alpha) {
        travellersTitles$alpha = Math.clamp(alpha, 0.0F, 1.0F);
    }
}