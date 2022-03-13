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
package scpsharp.content.subject

import scpsharp.content.subject.scp005.SCP005
import scpsharp.content.subject.scp008.SCP008
import scpsharp.content.subject.scp008.SCP008Client
import scpsharp.content.subject.scp173.SCP173
import scpsharp.content.subject.scp173.SCP173Client
import scpsharp.content.subject.scp427.SCP427
import scpsharp.content.subject.scp500.SCP500
import scpsharp.content.subject.scp714.SCP714
import scpsharp.content.subject.scp914.SCP914
import scpsharp.content.subject.scp914.SCP914ControllerBlock
import scpsharp.util.id
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object SCPSubjects {

    val itemGroup: ItemGroup = FabricItemGroupBuilder.create(id("scp_subjects"))
        .icon { ItemStack(SCP914ControllerBlock.item) }
        .build()

    init {
        SCP005
        SCP008
        SCP173
        SCP427
        SCP500
        SCP714
        SCP914
    }

}

object SCPSubjectsClient {

    init {
        SCP008Client
        SCP173Client
    }

}
