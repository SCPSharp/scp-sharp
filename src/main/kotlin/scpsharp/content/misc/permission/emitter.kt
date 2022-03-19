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
