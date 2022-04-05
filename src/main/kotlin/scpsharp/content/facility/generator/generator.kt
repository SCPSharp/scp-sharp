/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ChunkTicketType
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.TagKey
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryEntry
import net.minecraft.world.Heightmap
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*
import kotlin.random.asKotlinRandom

class FacilityGenerator(
    val access: StructureWorldAccess,
    val random: Random,
    val origin: BlockPos,
    val chunkGenerator: ChunkGenerator
) {

    companion object {

        val chunkTicketType = ChunkTicketType.create("scpsharp_facility_generator", Vec3i::compareTo, 20 * 60 * 2)

        fun generate(context: FeatureContext<*>, factory: ComponentFactory<*>) =
            //FacilityGeneratorPool.request(context, factory)
            FacilityGenerator(context).tryRandomGenerate(factory)

    }

    val spaceAllocator = StackAllocator(BlockBox::intersects)
    val componentAllocator = StackAllocator<Component> { a, b -> a === b }

    constructor(context: FeatureContext<*>) : this(
        context.world,
        context.random,
        context.origin.withY(
            context.generator.getHeight(
                context.origin.x,
                context.origin.z,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world
            ) - 5
        ),
        context.generator
    )

    operator fun get(pos: BlockPos): BlockState =
        if (isChunkLoaded(pos)) access.getBlockState(pos)
        else throw UnsupportedOperationException("Target chunk not loaded")

    operator fun set(pos: BlockPos, state: BlockState) =
        if (isChunkLoaded(pos)) access.setBlockState(pos, state, 0)
        else throw UnsupportedOperationException("Target chunk not loaded")

    operator fun set(pos: BlockPos, block: Block) = set(pos, block.defaultState)

    val seed = access.seed
    val world: ServerWorld = access.toServerWorld()
    val server = access.server

    fun isChunkLoaded(pos: BlockPos) = access.chunkManager.isChunkLoaded(
        ChunkSectionPos.getSectionCoord(pos.x),
        ChunkSectionPos.getSectionCoord(pos.z)
    )

    fun getSurfaceHeight(pos: BlockPos) = getSurfaceHeight(pos.x, pos.z)

    fun getSurfaceHeight(x: Int, z: Int) =
        chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, access)

    fun tryRandomGenerate(factory: ComponentFactory<*>) =
        factory.generate(this, origin, Direction.Type.HORIZONTAL.random(random), freezeAllocator = true, depth = 0)

    fun validateBlock(pos: BlockPos) = isChunkLoaded(pos) && validateBlock(get(pos))

    fun validateBlock(state: BlockState): Boolean = !state.isIn(ComponentTags.facilityKeep)

    fun validateSpace(box: BlockBox) =
        BlockPos.stream(box).allMatch { validateBlock(it) }

    fun validateSpaces(boxes: Collection<BlockBox>) =
        boxes.all { validateSpace(it) }

    fun validateSpaces(boxes: Array<BlockBox>) =
        boxes.all { validateSpace(it) }

    fun validateReferences(refs: Collection<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateReferences(refs: Array<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateRefs(refs: Array<ComponentRef<*>>) = validateReferences(refs)

    fun validate(boxes: Array<BlockBox>, refs: Array<ComponentRef<*>>): Boolean =
        validateSpaces(boxes) && validateReferences(refs)

    fun randomComponentFactory(
        tag: TagKey<ComponentFactory<*>>,
        filter: (ComponentFactory<*>) -> Boolean = { true }
    ): ComponentFactory<*> =
        ComponentFactory.registry.getOrCreateEntryList(tag)
            .map(RegistryEntry<ComponentFactory<*>>::value)
            .filter(filter)
            .randomOrNull(random.asKotlinRandom())
            ?: throw IllegalArgumentException("No component factory with tag $tag found")

    fun randomComponent(
        tag: TagKey<ComponentFactory<*>>,
        pos: BlockPos,
        direction: Direction,
        depth: Int,
        filter: (ComponentFactory<*>) -> Boolean = { true }
    ): Component? = randomComponentFactory(tag, filter).create(this, pos, direction, depth)

    fun randomComponentRef(
        tag: TagKey<ComponentFactory<*>>,
        pos: BlockPos,
        direction: Direction,
        depth: Int,
        filter: (ComponentFactory<*>) -> Boolean = { true }
    ) = ComponentRef(this, randomComponentFactory(tag, filter), pos, direction, depth)

}
