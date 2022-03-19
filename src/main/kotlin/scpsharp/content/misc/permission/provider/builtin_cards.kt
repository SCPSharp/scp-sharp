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
package scpsharp.content.misc.permission.provider

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import scpsharp.content.misc.SCPMisc
import scpsharp.util.id

object BuiltinPermissionCards {

    val o5Id = id("keycard_o5")
    val o5Item = SimplePermissionCardItem(
        o5Id, FabricItemSettings()
            .group(SCPMisc.itemGroup)
            .maxCount(1)
            .fireproof()
            .rarity(Rarity.EPIC)
    )

    init {
        Registry.register(Registry.ITEM, o5Id, o5Item)
    }

}