/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.misc

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import scpsharp.content.misc.permission.SCPPermission
import scpsharp.content.misc.permission.provider.CardReaderBlock
import scpsharp.util.id

object SCPMisc {

    val ITEM_GROUP: ItemGroup = FabricItemGroup.builder(id("scp_misc"))
        .icon { ItemStack(CardReaderBlock.ITEM) }
        .build()

    init {
        SCPPermission
    }

}
