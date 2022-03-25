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
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class FacilityGenerator(
    val access: StructureWorldAccess,
    val random: Random,
    val origin: BlockPos,
    val chunkGenerator: ChunkGenerator
) {

    companion object {

        val extendedStackSize = 1024L * 1024L * 16
        val generateTimeout = 1000L * 60L * 2

        fun generate(context: FeatureContext<*>, factory: ComponentFactory<*>): Boolean {
            val synchronizer = Object()
            val result = AtomicBoolean()
            Thread(Thread.currentThread().threadGroup, {
                result.set(factory.startRandomGenerate(FacilityGenerator(context)))
                synchronizer.notifyAll()
            }, "SCP Site Facility Generator", extendedStackSize).start()
            synchronizer.wait(generateTimeout)
            return result.get()
        }

    }

    val allocator = SpaceAllocator()

    constructor(context: FeatureContext<*>) : this(
        context.world,
        context.random,
        context.origin.withY(
            context.generator.getHeight(
                context.origin.x,
                context.origin.z,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world
            ) - 15
        ),
        context.generator
    )

    operator fun get(pos: BlockPos): BlockState = access.getBlockState(pos)

    operator fun set(pos: BlockPos, state: BlockState) = access.setBlockState(pos, state, 0)

    operator fun set(pos: BlockPos, block: Block) = set(pos, block.defaultState)

    val seed = access.seed
    val world: ServerWorld = access.toServerWorld()
    val server = access.server

    fun getSurfaceHeight(pos: BlockPos) = getSurfaceHeight(pos.x, pos.z)

    fun getSurfaceHeight(x: Int, z: Int) =
        chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, access)

    fun validateSpace(box: BlockBox) = BlockPos.stream(box).allMatch(::validateBlock)

    fun validateSpaces(boxes: Collection<BlockBox>) = boxes.all(::validateSpace)

    fun validateSpaces(boxes: Array<BlockBox>) = boxes.all(::validateSpace)

    fun validateBlock(pos: BlockPos) = validateBlock(get(pos)) && pos.y < getSurfaceHeight(pos)

    fun validateBlock(state: BlockState): Boolean = state.isAir || state.isIn(BlockTags.STONE_ORE_REPLACEABLES)

    fun validateReferences(refs: Collection<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateReferences(refs: Array<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateRefs(refs: Array<ComponentRef<*>>) = validateReferences(refs)

    fun validate(boxes: Array<BlockBox>, refs: Array<ComponentRef<*>>): Boolean =
        validateSpaces(boxes) && validateReferences(refs)

}