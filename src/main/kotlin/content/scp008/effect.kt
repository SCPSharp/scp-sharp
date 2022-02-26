package com.xtex.scpsharp.content.scp008

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.inventory.Inventory
import net.minecraft.util.registry.Registry


object SCP008StatusEffect : StatusEffect(StatusEffectCategory.HARMFUL, 0xd6426b) {

    val identifier = id("scp008_infected")
    val damageSource: DamageSource = DamageSource("scp008")
        .setBypassesArmor()
        .setUnblockable()

    init {
        Registry.register(Registry.STATUS_EFFECT, identifier, this)
    }

    fun infect(entity: LivingEntity, source: Entity? = null) {
        SCP008.logger.info("$entity infected SCP-008 because of $source")
        entity.addStatusEffect(
            StatusEffectInstance(SCP008StatusEffect, 20 * (563 + entity.world.random.nextInt(50))),
            source
        )
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = duration == 1

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        if (!entity.world.isClient) {
            SCP008.logger.info("Killing $entity")
            entity.damage(damageSource, Float.MAX_VALUE)
        }
    }

}

object SCP008OverlayRenderer : HudRenderCallback {

    val identifier = id("scp008_overlay")
    val overlayTexture = id("textures/gui/scp008_overlay.png")

    init {
        HudRenderCallback.EVENT.register(identifier, this)
    }

    override fun onHudRender(matrix: MatrixStack, tickDelta: Float) {
        if(MinecraftClient.getInstance().player!!.hasStatusEffect(SCP008StatusEffect)) {
            val width = MinecraftClient.getInstance().window.scaledWidth
            val height = MinecraftClient.getInstance().window.scaledHeight
            RenderSystem.enableBlend()
            RenderSystem.setShaderTexture(0, overlayTexture)
            InGameHud.drawTexture(matrix, 0, 0, 0f, 0f, width, height, width, height)
        }
    }

}
