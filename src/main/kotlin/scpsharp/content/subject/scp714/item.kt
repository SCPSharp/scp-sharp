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
        .group(SCPSubjects.itemGroup)
        .fireproof()
        .maxCount(1)
) {

    val identifier = id("scp714")

    init {
        Registry.register(Registry.ITEM, identifier, SCP714Item)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        if (selected && entity is LivingEntity) {
            if (entity is PlayerEntity) {
                entity.incrementStat(SCP714.usingStat)
            }
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.WEAKNESS, 0, 1, true, false))
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 0, 0, true, false))
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 0, 1, true, false))
        }
    }

}
