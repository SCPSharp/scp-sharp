/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.misc

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import scpsharp.content.misc.permission.SCPPermission
import scpsharp.content.misc.permission.provider.CardReaderBlock
import scpsharp.util.id

object SCPMisc {

    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(id("scp_misc"))
        .icon { ItemStack(CardReaderBlock.ITEM) }
        .build()

    init {
        SCPPermission
    }

}
