/*
 * Copyright (C) 2022  SCPSharp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
        .group(SCPSubjects.itemGroup)
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
            context.world.setBlockState(context.blockPos, state.cycle(Properties.OPEN))
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }

}
