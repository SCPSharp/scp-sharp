/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp008

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id

object SCP008StatusEffect : StatusEffect(StatusEffectCategory.HARMFUL, 0xd6426b) {

    val IDENTIFIER = id("scp008_infected")
    val DAMAGE_SOURCE: DamageSource = DamageSource("scp008")
        .setBypassesArmor()
        .setUnblockable()
    val BYPASS_TAG: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id("scp008_bypass"))

    init {
        Registry.register(Registries.STATUS_EFFECT, IDENTIFIER, this)
    }

    fun infect(entity: LivingEntity, source: Entity? = null) {
        if (!entity.type.isIn(BYPASS_TAG) && !AntiSCP008Suit.isWoreFully(entity)) {
            SCP008.LOGGER.info("$entity infected SCP-008 because of $source")
            entity.addStatusEffect(
                StatusEffectInstance(SCP008StatusEffect, 20 * (563 + entity.world.random.nextInt(50))),
                source
            )
        }
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = duration == 1

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        if (!entity.world.isClient) {
            SCP008.LOGGER.info("Killing $entity")
            if (entity is PlayerEntity) {
                entity.incrementStat(SCP008.DYING_STAT)
            }
            entity.damage(DAMAGE_SOURCE, Float.MAX_VALUE)
        }
    }

}
