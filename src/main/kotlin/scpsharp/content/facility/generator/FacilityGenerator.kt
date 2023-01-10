/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.*
import net.minecraft.util.math.random.Random
import net.minecraft.world.Heightmap
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.util.FeatureContext

class FacilityGenerator(
    val access: StructureWorldAccess, val random: Random, val origin: BlockPos, val chunkGenerator: ChunkGenerator
) {

    val spaceAllocator = StackAllocator(BlockBox::intersects)
    val componentAllocator = StackAllocator<Component> { a, b -> a === b }

    constructor(context: FeatureContext<*>) : this(
        context.world, context.random, context.origin.withY(
            context.generator.getHeight(
                context.origin.x,
                context.origin.z,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world,
                (context.world.chunkManager as ServerChunkManager).noiseConfig
            ) - 5
        ), context.generator
    )

    operator fun get(pos: BlockPos): BlockState = if (isChunkLoaded(pos)) access.getBlockState(pos)
    else throw UnsupportedOperationException("Target chunk not loaded")

    operator fun set(pos: BlockPos, state: BlockState) =
        if (isChunkLoaded(pos)) access.setBlockState(pos, state, 3 /* NOTIFY_ALL */)
        else throw UnsupportedOperationException("Target chunk not loaded")

    operator fun set(pos: BlockPos, block: Block) = set(pos, block.defaultState)

    fun fillBlocks(box: BlockBox, state: BlockState) {
        for (x in box.minX..box.maxX) {
            for (y in box.minY..box.maxY) {
                for (z in box.minZ..box.maxZ) {
                    set(BlockPos(x, y, z), state)
                }
            }
        }
    }

    fun fillBlocks(box: BlockBox, block: Block) = fillBlocks(box, block.defaultState)

    val seed get() = access.seed
    val world: ServerWorld get() = access.toServerWorld()
    val server get() = access.server

    fun isChunkLoaded(posX: Int, posZ: Int) = access.chunkManager.isChunkLoaded(posX, posZ)
    fun isChunkLoaded(pos: ChunkPos) = isChunkLoaded(pos.x, pos.z)
    fun isChunkLoaded(pos: BlockPos) =
        isChunkLoaded(ChunkSectionPos.getSectionCoord(pos.x), ChunkSectionPos.getSectionCoord(pos.z))

    fun getSurfaceHeight(pos: BlockPos) = getSurfaceHeight(pos.x, pos.z)

    fun getSurfaceHeight(x: Int, z: Int) = chunkGenerator.getHeight(
        x, z, Heightmap.Type.WORLD_SURFACE_WG, access, (world.chunkManager as ServerChunkManager).noiseConfig
    )

    fun tryRandomGenerate(factory: TagKey<ComponentFactory<*>>, extraValidator: Component.() -> Boolean) =
        tryRandomGenerate(randomComponentFactory(factory), extraValidator)

    fun <T : Component> tryRandomGenerate(factory: ComponentFactory<T>, extraValidator: T.() -> Boolean) =
        factory.generate(
            this,
            origin,
            Direction.Type.HORIZONTAL.random(random),
            freezeAllocator = true,
            depth = 0,
            extraValidator = extraValidator
        )

    fun validateBlock(pos: BlockPos) = isChunkLoaded(pos) && validateBlock(get(pos))

    fun validateBlock(state: BlockState): Boolean = !state.isIn(ComponentTags.FACILITY_KEEP)

    fun validateSpace(box: BlockBox): Boolean {
        for (x in box.minX..box.maxX) {
            for (y in box.minY..box.maxY) {
                for (z in box.minZ..box.maxZ) {
                    if (!validateBlock(BlockPos(x, y, z))) return false
                }
            }
        }
        return true
    }

    fun validateSpaces(boxes: Collection<BlockBox>) = boxes.all { validateSpace(it) }

    fun validateSpaces(boxes: Array<BlockBox>) = boxes.all { validateSpace(it) }

    fun validateReferences(refs: Collection<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateReferences(refs: Array<ComponentRef<*>>) = refs.map(ComponentRef<*>::component).all { it != null }

    fun validateRefs(refs: Array<ComponentRef<*>>) = validateReferences(refs)

    fun validate(boxes: Array<BlockBox>, refs: Array<ComponentRef<*>>): Boolean =
        validateSpaces(boxes) && validateReferences(refs)

    fun randomComponentFactory(
        tag: TagKey<ComponentFactory<*>>, filter: (ComponentFactory<*>) -> Boolean = { true }
    ): ComponentFactory<*> =
        ComponentFactory.REGISTRY.getOrCreateEntryList(tag).map(RegistryEntry<ComponentFactory<*>>::value)
            .filter(filter).randomOrNull(kotlin.random.Random(random.nextLong()))
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

    fun spawnEntity(entity: Entity) = access.spawnEntity(entity)

    fun spawnEntity(type: EntityType<*>, pos: BlockPos) =
        access.spawnEntity(type.spawn(world, pos, SpawnReason.CHUNK_GENERATION))

}
