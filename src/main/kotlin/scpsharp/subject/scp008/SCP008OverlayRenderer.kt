/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp008

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import scpsharp.util.id

object SCP008OverlayRenderer : HudRenderCallback {

    val IDENTIFIER = id("scp008_overlay")
    val OVERLAY_TEXTURE = id("textures/gui/scp008_overlay.png")

    init {
        HudRenderCallback.EVENT.register(IDENTIFIER, this)
    }

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        if (MinecraftClient.getInstance().player!!.hasStatusEffect(SCP008StatusEffect)) {
            val width = MinecraftClient.getInstance().window.scaledWidth
            val height = MinecraftClient.getInstance().window.scaledHeight
            RenderSystem.enableBlend()
            drawContext.drawTexture(OVERLAY_TEXTURE, 0, 0, 0f, 0f, width, height, width, height)
        }
    }

}
