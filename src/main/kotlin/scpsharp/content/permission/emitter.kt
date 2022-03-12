package scpsharp.content.permission

import net.minecraft.block.BlockState
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface SCPPermissionEmitterBlock {

    fun getAllProvidedPermissions(world: World, pos: BlockPos, state: BlockState): Collection<Identifier>

    fun getAllProvidedPermissions(world: World, pos: BlockPos) = getAllProvidedPermissions(world, pos, world.getBlockState(pos))

    fun isEmitting(world: World, pos: BlockPos, id: Identifier) = id in getAllProvidedPermissions(world, pos)

}
