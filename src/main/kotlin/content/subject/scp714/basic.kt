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
package com.xtex.scpsharp.content.scp714

import com.xtex.scpsharp.util.id
import net.minecraft.entity.EntityType
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object SCP714 {

    val bypassTag: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp714_bypass"))

    val usingStat = id("using_scp714")

    val sleepWithStat = id("sleep_with_scp714")

    init {
        SCP714Item

        Registry.register(Registry.CUSTOM_STAT, usingStat, usingStat)
        Stats.CUSTOM.getOrCreateStat(usingStat, StatFormatter.TIME)
        Registry.register(Registry.CUSTOM_STAT, sleepWithStat, sleepWithStat)
        Stats.CUSTOM.getOrCreateStat(sleepWithStat, StatFormatter.DEFAULT)
    }

}