/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import scpsharp.subject.scp005.SCP005
import scpsharp.subject.scp008.SCP008
import scpsharp.subject.scp008.SCP008Client
import scpsharp.subject.scp173.SCP173
import scpsharp.subject.scp173.SCP173Client
import scpsharp.subject.scp427.SCP427
import scpsharp.subject.scp500.SCP500
import scpsharp.subject.scp714.SCP714
import scpsharp.subject.scp914.SCP914
import scpsharp.subject.scp914.SCP914ControllerBlock
import scpsharp.util.id

object SCPSubjects {

    val ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("scp_subjects"))

    val ITEM_GROUP: ItemGroup = FabricItemGroup.builder()
        .displayName(Text.translatable("itemGroup.scpsharp.scp_subjects"))
        .icon { ItemStack(SCP914ControllerBlock.ITEM) }
        .build()

    init {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP)

        SCP005
        SCP008
        SCP173
        SCP427
        SCP500
        SCP714
        SCP914
    }

}

object SCPSubjectsClient {

    init {
        SCP008Client
        SCP173Client
    }

}
