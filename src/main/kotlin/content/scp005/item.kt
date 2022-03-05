package com.xtex.scpsharp.content.scp005

import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.registry.Registry

object SCP005Item : Item(
    FabricItemSettings()
        .group(scpSubjectItemGroup)
        .fireproof()
        .maxCount(1)
) {

    val identifier = id("scp005")

    init {
        Registry.register(Registry.ITEM, identifier, this)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val state = context.world.getBlockState(context.blockPos)
        if (Properties.OPEN in state.properties
            && !state.isIn(SCP005.bypassTag)
            && state.isIn(SCP005.doorFilterTag)
        ) {
            SCP005.logger.info("${context.player} using SCP-005 on $state at ${context.world} ${context.blockPos}")
            context.world.setBlockState(context.blockPos, state.with(Properties.OPEN, !state[Properties.OPEN]))
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }

}
