/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp005

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import scpsharp.subject.SCPSubjects
import scpsharp.util.addItem
import scpsharp.util.id

object SCP005Item : Item(
    FabricItemSettings()
        .fireproof()
        .maxCount(1)
) {

    val IDENTIFIER = id("scp005")

    init {
        Registry.register(Registries.ITEM, scpsharp.subject.scp005.SCP005Item.IDENTIFIER, this)
        SCPSubjects.ITEM_GROUP_KEY.addItem(this)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val state = context.world.getBlockState(context.blockPos)
        if (Properties.OPEN in state.properties
            && !state.isIn(scpsharp.subject.scp005.SCP005.BYPASS_TAG)
            && state.isIn(scpsharp.subject.scp005.SCP005.DOOR_FILTER_TAG)
        ) {
            scpsharp.subject.scp005.SCP005.LOGGER.info("${context.player} using SCP-005 on $state at ${context.world} ${context.blockPos}")
            context.world.setBlockState(context.blockPos, state.cycle(Properties.OPEN))
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }

}
