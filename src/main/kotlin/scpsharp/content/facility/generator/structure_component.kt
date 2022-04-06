/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

open class StructureComponent(
    val structure: Structure,
    private val placementData: StructurePlacementData,
    pos: BlockPos,
    override val refs: Array<ComponentRef<*>>,
) : SimpleComponent() {

    override val boxes: Array<BlockBox> = arrayOf(structure.calculateBoundingBox(placementData, pos))

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean =
        structure.place(generator.access, pos, BlockPos(direction.vector), placementData, generator.random, 3 /* NOTIFY_ALL */)

}

class StructureComponentFactory(
    val structureId: Identifier,
    val rotation: BlockRotation = BlockRotation.NONE,
    val mirror: BlockMirror = BlockMirror.NONE,
    val refsProvider: (generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int) -> Array<ComponentRef<*>> =
        { _: FacilityGenerator, _: BlockPos, _: Direction, _: Int -> emptyArray<ComponentRef<*>>() }
) : ComponentFactory<StructureComponent>() {

    override fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int
    ): StructureComponent =
        StructureComponent(
            generator.world.structureManager.getStructure(structureId)
                .orElseThrow { IllegalArgumentException("Structure with id $structureId not found") },
            StructurePlacementData()
                .setRandom(generator.random)
                .setRotation(rotation)
                .setMirror(mirror)
                .setUpdateNeighbors(true),
            pos, refsProvider(generator, pos, direction, depth + 1)
        )

}
