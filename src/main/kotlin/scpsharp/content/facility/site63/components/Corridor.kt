/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63.components

import net.minecraft.block.Blocks
import net.minecraft.registry.Registry
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import scpsharp.content.facility.generator.*
import scpsharp.util.BlockBox
import scpsharp.util.id

class Site63CorridorComponent(
    pos: BlockPos,
    direction: Direction,
    val length: Int,
    override val refs: Array<ComponentRef<*>>,
    override val type: ComponentFactory<*>
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
                    generator[BlockPos(x, y, z)] =
                        if (((x == boundingBox.minX || x == boundingBox.maxX) && direction.offsetX == 0)
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
        run { // Light
            for (i in 0 until length) {
                // @TODO: Use frog light after 1.19
                val basePos = pos.offset(direction, i).add(0, 5, 0)
                generator[basePos.offset(direction.rotateYClockwise(), 2)] = Blocks.SEA_LANTERN
                generator[basePos.offset(direction.rotateYClockwise(), 3)] = Blocks.SEA_LANTERN
            }
        }
        return true
    }

}

object Site63CorridorComponentFactory : ComponentFactory<Site63CorridorComponent>() {

    val IDENTIFIER = id("site63_corridor")

    init {
        Registry.register(REGISTRY, IDENTIFIER, this)
    }

    override fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int
    ): Site63CorridorComponent {
        val length = generator.random.nextBetween(8, 13)
        val connected = generator.randomComponentRef(
            if (generator.random.nextInt(10) < 2) ComponentTags.SITE63_GENERATING else ComponentTags.SITE63_CORRIDOR_CONNECTED,
            pos.offset(direction, length + 1),
            direction,
            depth + 1
        )
        return Site63CorridorComponent(pos, direction, length, arrayOf(connected), this)
    }

}
