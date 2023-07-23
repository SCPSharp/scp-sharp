/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.misc

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import scpsharp.misc.permission.SCPPermission
import scpsharp.misc.permission.provider.CardReaderBlock
import scpsharp.util.id

object SCPMisc {

    val ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("scp_misc"))

    val ITEM_GROUP: ItemGroup = FabricItemGroup.builder()
        .icon { ItemStack(CardReaderBlock.ITEM) }
        .build()

    init {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP)

        SCPPermission
    }

}
