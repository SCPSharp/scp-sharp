/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp427

import scpsharp.content.subject.SCPSubjects
import scpsharp.util.id
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class SCP427Item private constructor(val open: Boolean) : Item(
    FabricItemSettings()
        .group(SCPSubjects.ITEM_GROUP)
        .fireproof()
        .maxCount(1)
) {

    companion object {

        val CLOSED_IDENTIFIER = id("scp427_closed")
        val CLOSED_ITEM = SCP427Item(open = false)
        val OPEN_IDENTIFIER = id("scp427_open")
        val OPEN_ITEM = SCP427Item(open = true)

        init {
            Registry.register(Registry.ITEM, CLOSED_IDENTIFIER, CLOSED_ITEM)
            Registry.register(Registry.ITEM, OPEN_IDENTIFIER, OPEN_ITEM)
        }

    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (open) {
            user.incrementStat(SCP427.CLOSING_STAT)
            return TypedActionResult.success(ItemStack(CLOSED_ITEM))
        } else {
            user.incrementStat(SCP427.OPENING_STAT)
            return TypedActionResult.success(ItemStack(OPEN_ITEM))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        if (open) {
            world.getEntitiesByClass(
                LivingEntity::class.java,
                Box.from(entity.pos).expand(3.0),
            ) {
                !it.type.isIn(SCP427.BYPASS_TAG)
            }
                .forEach {
                    if (it is PlayerEntity) {
                        it.incrementStat(SCP427.APPLYING_STAT)
                    }
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.RESISTANCE, 0, 1, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.SATURATION, 1, 0, true, false), entity)
                }
        }
    }

}
