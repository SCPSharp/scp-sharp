/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.facility

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id

object FacilityTags {

    val START = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/start"))!!
    val PART = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/part"))!!
    val CONTAINMENT_ROOM = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/containment_room"))!!
    val CORRIDOR = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/corridor"))!!
    val CROSSING = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/crossing"))!!

}