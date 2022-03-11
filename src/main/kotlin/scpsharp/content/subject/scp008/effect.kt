/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package scpsharp.content.subject.scp008

import com.mojang.blaze3d.systems.RenderSystem
import scpsharp.util.id
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry


object SCP008StatusEffect : StatusEffect(StatusEffectCategory.HARMFUL, 0xd6426b) {

    val identifier = id("scp008_infected")
    val damageSource: DamageSource = DamageSource("scp008")
        .setBypassesArmor()
        .setUnblockable()
    val bypassTag: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp008_bypass"))

    init {
        Registry.register(Registry.STATUS_EFFECT, identifier, this)
    }

    fun infect(entity: LivingEntity, source: Entity? = null) {
        if (!entity.type.isIn(bypassTag)) {
            SCP008.logger.info("$entity infected SCP-008 because of $source")
            entity.addStatusEffect(
                StatusEffectInstance(SCP008StatusEffect, 20 * (563 + entity.world.random.nextInt(50))),
                source
            )
        }
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = duration == 1

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        if (!entity.world.isClient) {
            SCP008.logger.info("Killing $entity")
            if(entity is PlayerEntity){
                entity.incrementStat(SCP008.dyingStat)
            }
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
        if (MinecraftClient.getInstance().player!!.hasStatusEffect(SCP008StatusEffect)) {
            val width = MinecraftClient.getInstance().window.scaledWidth
            val height = MinecraftClient.getInstance().window.scaledHeight
            RenderSystem.enableBlend()
            RenderSystem.setShaderTexture(0, overlayTexture)
            InGameHud.drawTexture(matrix, 0, 0, 0f, 0f, width, height, width, height)
        }
    }

}
