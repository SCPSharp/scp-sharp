/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.misc.permission

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import scpsharp.misc.permission.provider.BuiltinPermissionCards
import scpsharp.misc.permission.provider.CardReaderBlock
import scpsharp.util.logger

object SCPPermission {

    val LOGGER = logger("Permission")

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
                    if (isReceivingStrongPermission(world, pos.add(x, y, z), id))
                        return true
                }
            }
        }
        return false
    }

    fun isReceivingStrongPermission(world: World, pos: BlockPos, id: Identifier): Boolean {
        for (direction in Direction.entries) {
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
