package com.xtex.scpsharp.content.scp427

import com.google.common.base.Predicates
import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.util.id
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
        .group(scpSubjectItemGroup)
        .fireproof()
        .maxCount(1)
) {

    companion object {

        val closedIdentifier = id("scp427_closed")
        val closedItem = SCP427Item(open = false)
        val openIdentifier = id("scp427_open")
        val openItem = SCP427Item(open = true)

        init {
            Registry.register(Registry.ITEM, closedIdentifier, closedItem)
            Registry.register(Registry.ITEM, openIdentifier, openItem)
        }

    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
        TypedActionResult.success(ItemStack(if (open) closedItem else openItem))

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        if (open) {
            world.getEntitiesByClass(
                LivingEntity::class.java,
                Box.from(entity.pos).expand(3.0),
                Predicates.alwaysTrue()
            )
                .forEach {
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.RESISTANCE, 0, 1, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.SATURATION, 1, 0, true, false), entity)
                }
        }
    }

}
