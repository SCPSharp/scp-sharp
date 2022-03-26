package scpsharp.content.facility.generator

import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class StructureComponent(
    val structure: Structure,
    private val placementData: StructurePlacementData,
    pos: BlockPos,
    override val refs: Array<ComponentRef<*>>
) : SimpleComponent {

    override val boxes: Array<BlockBox> = arrayOf(structure.calculateBoundingBox(placementData, pos))

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean =
        structure.place(generator.access, pos, BlockPos(direction.vector), placementData, generator.random, 0)

}

class StructureComponentFactory(
    val structureId: Identifier,
    val refsProvider: () -> Array<ComponentRef<*>> = ::emptyArray
) : ComponentFactory<StructureComponent> {

    override fun construct(generator: FacilityGenerator, pos: BlockPos, direction: Direction): StructureComponent =
        StructureComponent(
            generator.world.structureManager.getStructure(structureId)
                .orElseThrow { IllegalArgumentException("Structure with id $structureId not found") },
            StructurePlacementData()
                .setRandom(generator.random)
                .setUpdateNeighbors(true),
            pos,
            refsProvider()
        )

}
