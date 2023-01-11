/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.misc.permission

import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface SCPPermissionEmitterBlock {

    fun getAllProvidedPermissions(world: World, pos: BlockPos, state: BlockState): Collection<Identifier>

    fun getAllProvidedPermissions(world: World, pos: BlockPos) = getAllProvidedPermissions(world, pos, world.getBlockState(pos))

    fun isEmitting(world: World, pos: BlockPos, id: Identifier) = id in getAllProvidedPermissions(world, pos)

}

interface SCPPermissionCardItem {

    fun getAllProvidedPermissions(world: World, stack: ItemStack): Collection<Identifier>

    fun isEmitting(world: World, stack: ItemStack, id: Identifier) = id in getAllProvidedPermissions(world, stack)

}
