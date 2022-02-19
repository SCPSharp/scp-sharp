package com.xtex.scpsharp.content

import com.xtex.scpsharp.content.scp914.SCP914ControllerBlock
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

val scpSubjectItemGroup: ItemGroup = FabricItemGroupBuilder.create(id("scp_subject"))
    .icon { ItemStack(SCP914ControllerBlock.item) }
    .build()
