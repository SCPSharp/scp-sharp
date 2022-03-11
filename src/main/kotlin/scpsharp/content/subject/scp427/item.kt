/*
 * Copyright (C) 2022  SCPSharp Team
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
        .group(SCPSubjects.itemGroup)
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

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (open) {
            user.incrementStat(SCP427.closingStat)
            return TypedActionResult.success(ItemStack(closedItem))
        } else {
            user.incrementStat(SCP427.openingStat)
            return TypedActionResult.success(ItemStack(openItem))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        if (open) {
            world.getEntitiesByClass(
                LivingEntity::class.java,
                Box.from(entity.pos).expand(3.0),
            ) {
                !it.type.isIn(SCP427.bypassTag)
            }
                .forEach {
                    if (it is PlayerEntity) {
                        it.incrementStat(SCP427.applyingStat)
                    }
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 0, 0, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.RESISTANCE, 0, 1, true, false), entity)
                    it.addStatusEffect(StatusEffectInstance(StatusEffects.SATURATION, 1, 0, true, false), entity)
                }
        }
    }

}
