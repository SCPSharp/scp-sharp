/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.misc.permission.provider

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import scpsharp.content.misc.SCPMisc
import scpsharp.util.id

object BuiltinPermissionCards {

    val O5_ID = id("keycard_o5")
    val O5_ITEM = SimplePermissionCardItem(
        O5_ID, FabricItemSettings()
            .group(SCPMisc.ITEM_GROUP)
            .maxCount(1)
            .fireproof()
            .rarity(Rarity.EPIC)
    )

    init {
        Registry.register(Registry.ITEM, O5_ID, O5_ITEM)
    }

}