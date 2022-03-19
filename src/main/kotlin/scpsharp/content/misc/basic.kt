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
package scpsharp.content.misc

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import scpsharp.content.misc.permission.SCPPermission
import scpsharp.content.misc.permission.provider.CardReaderBlock
import scpsharp.util.id

object SCPMisc {

    val itemGroup: ItemGroup = FabricItemGroupBuilder.create(id("scp_misc"))
        .icon { ItemStack(CardReaderBlock.item) }
        .build()

    init {
        SCPPermission
    }

}
