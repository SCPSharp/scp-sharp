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

import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

open class StructureComponent(
    val structure: Structure,
    private val placementData: StructurePlacementData,
    pos: BlockPos,
    override val refs: Array<ComponentRef<*>>,
    override val exposedInAir: Boolean
) : SimpleComponent() {

    override val boxes: Array<BlockBox> = arrayOf(structure.calculateBoundingBox(placementData, pos))

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean =
        structure.place(generator.access, pos, BlockPos(direction.vector), placementData, generator.random, 0)

}

class StructureComponentFactory(
    val structureId: Identifier,
    val refsProvider: () -> Array<ComponentRef<*>> = ::emptyArray
) : ComponentFactory<StructureComponent>() {

    override fun construct(generator: FacilityGenerator, pos: BlockPos, direction: Direction): StructureComponent =
        StructureComponent(
            generator.world.structureManager.getStructure(structureId)
                .orElseThrow { IllegalArgumentException("Structure with id $structureId not found") },
            StructurePlacementData()
                .setRandom(generator.random)
                .setUpdateNeighbors(true),
            pos, refsProvider(), isIn(ComponentTags.structureExposedInAir)
        )

}
