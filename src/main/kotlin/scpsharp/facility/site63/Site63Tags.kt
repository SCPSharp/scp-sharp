/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.facility.site63

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id

object Site63Tags {

    val START = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("site63/start"))!!
    val CONTAINMENT_ROOM = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("site63/containment_room"))!!
    val CORRIDOR = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("site63/corridor"))!!
    val CORRIDOR_CONNECTED = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("site63/corridor_connected"))!!
    val CROSSING = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("site63/crossing"))!!

}