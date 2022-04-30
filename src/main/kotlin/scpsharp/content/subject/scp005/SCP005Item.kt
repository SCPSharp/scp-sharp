/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp005

import scpsharp.content.subject.SCPSubjects
import scpsharp.util.id
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.registry.Registry

object SCP005Item : Item(
    FabricItemSettings()
        .group(SCPSubjects.ITEM_GROUP)
        .fireproof()
        .maxCount(1)
) {

    val IDENTIFIER = id("scp005")

    init {
        Registry.register(Registry.ITEM, IDENTIFIER, this)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val state = context.world.getBlockState(context.blockPos)
        if (Properties.OPEN in state.properties
            && !state.isIn(SCP005.BYPASS_TAG)
            && state.isIn(SCP005.DOOR_FILTER_TAG)
        ) {
            SCP005.LOGGER.info("${context.player} using SCP-005 on $state at ${context.world} ${context.blockPos}")
            context.world.setBlockState(context.blockPos, state.cycle(Properties.OPEN))
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }

}
