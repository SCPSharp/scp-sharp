/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp714

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
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object SCP714Item : Item(
    FabricItemSettings()
        .group(SCPSubjects.ITEM_GROUP)
        .fireproof()
        .maxCount(1)
) {

    val IDENTIFIER = id("scp714")

    init {
        Registry.register(Registry.ITEM, IDENTIFIER, SCP714Item)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        if (selected && entity is LivingEntity) {
            if (entity is PlayerEntity) {
                entity.incrementStat(SCP714.USING_STAT)
            }
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.WEAKNESS, 0, 1, true, false))
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 0, 0, true, false))
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 0, 1, true, false))
        }
    }

}
