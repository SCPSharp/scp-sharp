/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.content.scp173

import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class SCP173Entity(entityType: EntityType<out PathAwareEntity>, world: World) : PathAwareEntity(entityType, world) {

    companion object {

        val id = id("scp173")
        val type: EntityType<SCP173Entity> = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SCP173Entity)
            .dimensions(
                EntityDimensions.fixed(
                    1.4f * SCP173EntityModel.modelScale,
                    5.0f * SCP173EntityModel.modelScale
                )
            )
            .fireImmune()
            .build()
        val eggItemId = id("scp173_spawn_egg")
        val eggItem = SpawnEggItem(type, 0xa87550, 0x825b3f, FabricItemSettings()
            .group(scpSubjectItemGroup)
            .rarity(Rarity.UNCOMMON))

        init {
            Registry.register(Registry.ENTITY_TYPE, id, type)
            Registry.register(Registry.ITEM, eggItemId, eggItem)
            FabricDefaultAttributeRegistry.register(type, createAttributes())
        }

        @JvmStatic
        fun createAttributes(): DefaultAttributeContainer.Builder = createMobAttributes()

    }

}
