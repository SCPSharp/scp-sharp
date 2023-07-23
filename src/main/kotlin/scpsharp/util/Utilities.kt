/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.util

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun id(path: String) = Identifier("scpsharp", path)

fun logger(name: String): Logger = LoggerFactory.getLogger("SCPSharp/$name")

fun RegistryKey<ItemGroup>.addItem(item: Item) {
    ItemGroupEvents.modifyEntriesEvent(this)
        .register(ItemGroupEvents.ModifyEntries { content ->
            content.add(item)
        })
}
