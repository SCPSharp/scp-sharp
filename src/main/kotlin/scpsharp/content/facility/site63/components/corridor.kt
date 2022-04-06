/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63.components

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import scpsharp.content.facility.generator.*
import scpsharp.util.BlockBox
import scpsharp.util.id

class Site63CorridorComponent(
    pos: BlockPos,
    direction: Direction,
    val length: Int,
    override val refs: Array<ComponentRef<*>>
) : SimpleComponent() {

    val boundingBox = BlockBox(
        pos, pos.offset(direction.rotateYClockwise(), 5)
            .offset(direction, length)
            .add(0, 5, 0)
    )

    override val boxes: Array<BlockBox> = arrayOf(boundingBox)

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean {
        for (x in boundingBox.minX..boundingBox.maxX) {
            for (y in boundingBox.minY..boundingBox.maxY) {
                for (z in boundingBox.minZ..boundingBox.maxZ) {
                    generator[BlockPos(x, y, z)] = if (((x == boundingBox.minX || x == boundingBox.maxX) && direction.offsetX == 0)
                        || ((y == boundingBox.minY || y == boundingBox.maxY) && direction.offsetY == 0)
                        || ((z == boundingBox.minZ || z == boundingBox.maxZ) && direction.offsetZ == 0)
                    ) {
                        Blocks.POLISHED_DEEPSLATE
                    } else {
                        Blocks.AIR
                    }
                }
            }
        }
        return true
    }

}

object Site63CorridorComponentFactory : ComponentFactory<Site63CorridorComponent>() {

    val identifier = id("site63_corridor")

    init {
        Registry.register(ComponentFactory.registry, identifier, this)
    }

    override fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int
    ): Site63CorridorComponent {
        val length = generator.random.nextInt(8, 13)
        return Site63CorridorComponent(
            pos,
            direction,
            length,
            if(depth <= 2) arrayOf(
                generator.randomComponentRef(
                    ComponentTags.site63Generating,
                    pos.offset(direction, length + 1),
                    direction,
                    depth + 1
                )
            ) else emptyArray()
        )
    }

}
