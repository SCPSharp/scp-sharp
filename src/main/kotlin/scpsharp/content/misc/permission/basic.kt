/*
 * Copyright (C) 2022  SCPSharp Team
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
package scpsharp.content.misc.permission

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import scpsharp.content.misc.permission.provider.BuiltinPermissionCards
import scpsharp.content.misc.permission.provider.CardReaderBlock
import scpsharp.util.logger

object SCPPermission {

    val logger = logger("Permission")

    init {
        CardReaderBlock
        BuiltinPermissionCards
    }

    fun updateDoubleNeighbors(world: World, pos: BlockPos) {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val offsetPos = pos.add(x, y, z)
                    world.updateNeighborsAlways(offsetPos, world.getBlockState(offsetPos).block)
                }
            }
        }
    }

    fun isPermissionPermitted(world: World, pos: BlockPos, id: Identifier): Boolean {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (isReceivingStrongPermission(world, pos.add(x, y, z), id)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun isReceivingStrongPermission(world: World, pos: BlockPos, id: Identifier): Boolean {
        for (direction in Direction.values()) {
            val offsetPos = pos.offset(direction)
            val state = world.getBlockState(offsetPos)
            if (state.block is SCPPermissionEmitterBlock) {
                if ((state.block as SCPPermissionEmitterBlock).isEmitting(world, offsetPos, id)) {
                    return true
                }
            }
        }
        return false
    }

}
