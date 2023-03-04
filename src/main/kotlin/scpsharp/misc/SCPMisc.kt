/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.misc

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import scpsharp.misc.permission.SCPPermission
import scpsharp.misc.permission.provider.CardReaderBlock
import scpsharp.util.id

object SCPMisc {

    val ITEM_GROUP: ItemGroup = FabricItemGroup.builder(id("scp_misc"))
        .icon { ItemStack(CardReaderBlock.ITEM) }
        .build()

    init {
        SCPPermission
    }

}
