/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp005

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id
import scpsharp.util.logger

object SCP005 {

    val LOGGER = logger("SCP-005")

    val BYPASS_TAG: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id("scp005_bypass"))

    val DOOR_FILTER_TAG: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id("scp005_doors"))

    init {
        SCP005Item
    }

}
