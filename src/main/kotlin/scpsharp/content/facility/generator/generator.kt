/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.Heightmap
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*

class FacilityGenerator(
    val access: StructureWorldAccess,
    val random: Random,
    val origin: BlockPos,
    val chunkGenerator: ChunkGenerator
) {

    companion object {

        fun generate(context: FeatureContext<*>, factory: ComponentFactory<*>) =
            FacilityGeneratorPool.request(context, factory)

    }

    val allocator = StackAllocator(BlockBox::intersects)

    constructor(context: FeatureContext<*>) : this(
        context.world,
        context.random,
        context.origin.withY(
            context.generator.getHeight(
                context.origin.x,
                context.origin.z,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world
            ) - 3
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

    fun tryRandomGenerate(factory: ComponentFactory<*>) =
        factory.generate(this, origin, Direction.random(random), freezeAllocator = true)

    fun validateBlock(pos: BlockPos, exposedInAir: Boolean = false) =
        validateBlock(get(pos), exposedInAir) && (pos.y < getSurfaceHeight(pos) || exposedInAir)

    fun validateBlock(state: BlockState, exposedInAir: Boolean = false): Boolean =
        (!state.isAir || exposedInAir) || state.isIn(BlockTags.STONE_ORE_REPLACEABLES) || state.isIn(ComponentTags.facilityReplaceable)

    fun validateSpace(box: BlockBox, exposedInAir: Boolean = false) =
        BlockPos.stream(box).allMatch { validateBlock(it, exposedInAir) }

    fun validateSpaces(boxes: Collection<BlockBox>, exposedInAir: Boolean = false) =
        boxes.all { validateSpace(it, exposedInAir) }

    fun validateSpaces(boxes: Array<BlockBox>, exposedInAir: Boolean = false) =
        boxes.all { validateSpace(it, exposedInAir) }

    fun validateReferences(refs: Collection<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateReferences(refs: Array<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateRefs(refs: Array<ComponentRef<*>>) = validateReferences(refs)

    fun validate(boxes: Array<BlockBox>, refs: Array<ComponentRef<*>>, exposedInAir: Boolean = false): Boolean =
        validateSpaces(boxes, exposedInAir) && validateReferences(refs)

}
