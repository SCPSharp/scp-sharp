/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.misc.permission.provider

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import scpsharp.misc.SCPMisc
import scpsharp.util.addItem
import scpsharp.util.id


object BuiltinPermissionCards {

    val O5_ID = id("keycard_o5")
    val O5_ITEM = SimplePermissionCardItem(
        O5_ID, FabricItemSettings()
            .maxCount(1)
            .fireproof()
            .rarity(Rarity.EPIC)
    )

    init {
        Registry.register(Registries.ITEM, O5_ID, O5_ITEM)
        SCPMisc.ITEM_GROUP.addItem(O5_ITEM)
    }

}