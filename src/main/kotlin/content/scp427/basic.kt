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
package com.xtex.scpsharp.content.scp427

import com.xtex.scpsharp.util.id
import net.minecraft.entity.EntityType
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object SCP427 {

    val bypassTag: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp427_bypass"))

    init {
        SCP427Item
    }

}