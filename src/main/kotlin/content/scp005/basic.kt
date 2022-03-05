package com.xtex.scpsharp.content.scp005

import com.xtex.scpsharp.util.id
import com.xtex.scpsharp.util.logger
import net.minecraft.block.Block
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object SCP005 {

    val logger = logger("SCP-005")

    val bypassTag: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, id("scp005_bypass"))

    val doorFilterTag: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, id("scp005_doors"))

    init {
        SCP005Item
    }

}
