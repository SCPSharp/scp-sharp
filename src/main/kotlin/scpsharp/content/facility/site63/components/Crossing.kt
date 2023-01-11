/*
 * Copyright (C) 2023  SCPSharp Team
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

class Site63CrossingComponent(
    pos: BlockPos,
    direction: Direction,
    val left: ComponentRef<*>?,
    val right: ComponentRef<*>?,
    val front: ComponentRef<*>?,
    override val type: ComponentFactory<*>
) : SimpleComponent() {

    override val refs: Array<ComponentRef<*>> = setOf(left, right, front).filterNotNull().toTypedArray()

    val mainBoundingBox = BlockBox(
        pos,
        pos.offset(direction, 6).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
    )

    override val boxes: Array<BlockBox> by lazy {
        val boxes = mutableListOf<BlockBox>(mainBoundingBox)
        run { // Left box
            if (left?.component != null) {
                boxes.add(
                    BlockBox(
                        pos.offset(direction.rotateYCounterclockwise()).offset(direction),
                        pos.offset(direction.rotateYCounterclockwise()).offset(direction, 6).offset(Direction.UP, 5)
                    )
                )
            }
        }
        run { // Right box
            if (right?.component != null) {
                boxes.add(
                    BlockBox(
                        pos.offset(direction.rotateYClockwise(), 6).offset(direction),
                        pos.offset(direction.rotateYClockwise(), 6).offset(direction, 6).offset(Direction.UP, 5)
                    )
                )
            }
        }
        run { // Front box
            if (front?.component != null) {
                boxes.add(
                    BlockBox(
                        pos.offset(direction, 7),
                        pos.offset(direction, 7).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
                    )
                )
            }
        }
        boxes.toTypedArray()
    }

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean {
        run { // Main box
            for (x in mainBoundingBox.minX..mainBoundingBox.maxX) {
                for (y in mainBoundingBox.minY..mainBoundingBox.maxY) {
                    for (z in mainBoundingBox.minZ..mainBoundingBox.maxZ) {
                        generator[BlockPos(x, y, z)] =
                            if ((x == mainBoundingBox.minX || x == mainBoundingBox.maxX)
                                || (y == mainBoundingBox.minY || y == mainBoundingBox.maxY)
                                || (z == mainBoundingBox.minZ || z == mainBoundingBox.maxZ)
                            ) {
                                Blocks.POLISHED_DEEPSLATE
                            } else {
                                Blocks.AIR
                            }
                    }
                }
            }
        }
        run { // Incoming ring
            generator.fillBlocks(
                BlockBox(
                    pos,
                    pos.offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
                ), Blocks.POLISHED_DEEPSLATE
            )
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4).offset(direction)
                ), Blocks.AIR
            )
        }
        run { // Left ring
            if (left?.component != null) {
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction.rotateYCounterclockwise()).offset(direction),
                        pos.offset(direction.rotateYCounterclockwise()).offset(direction, 6).offset(Direction.UP, 5)
                    ), Blocks.POLISHED_DEEPSLATE
                )
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction.rotateYCounterclockwise()).offset(direction, 5).offset(Direction.UP),
                        pos.offset(direction, 2).offset(Direction.UP, 4)
                    ), Blocks.AIR
                )
            }
        }
        run { // Right ring
            if (right?.component != null) {
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction.rotateYClockwise(), 6).offset(direction),
                        pos.offset(direction.rotateYClockwise(), 6).offset(direction, 6).offset(Direction.UP, 5)
                    ), Blocks.POLISHED_DEEPSLATE
                )
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction.rotateYClockwise(), 6).offset(direction, 2).offset(Direction.UP),
                        pos.offset(direction.rotateYClockwise(), 5).offset(direction, 5).offset(Direction.UP, 4)
                    ), Blocks.AIR
                )
            }
        }
        run { // Front ring
            if (front?.component != null) {
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction, 7),
                        pos.offset(direction, 7).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
                    ), Blocks.POLISHED_DEEPSLATE
                )
                generator.fillBlocks(
                    BlockBox(
                        pos.offset(direction, 7).offset(direction.rotateYClockwise()).offset(Direction.UP),
                        pos.offset(direction, 6).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                    ), Blocks.AIR
                )
            }
        }
        return true
    }

}

object Site63CrossingComponentFactory : ComponentFactory<Site63CrossingComponent>() {

    val IDENTIFIER = id("site63_crossing")

    init {
        Registry.register(ComponentFactory.REGISTRY, IDENTIFIER, this)
    }

    override fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int
    ): Site63CrossingComponent {
        val left = if (generator.random.nextInt(10) <= 4) generator.randomComponentRef(
            ComponentTags.SITE63_CORRIDOR_CONNECTED,
            pos.offset(direction).offset(direction.rotateYCounterclockwise(), 2),
            direction.rotateYCounterclockwise(),
            depth + 1
        ) else null
        val right = if (generator.random.nextInt(10) <= 4) generator.randomComponentRef(
            ComponentTags.SITE63_CORRIDOR_CONNECTED,
            pos.offset(direction, 6).offset(direction.rotateYClockwise(), 7),
            direction.rotateYClockwise(),
            depth + 1
        ) else null
        val front =
            if (generator.random.nextInt(10) <= 3 && (left != null || right != null)) generator.randomComponentRef(
                ComponentTags.SITE63_CORRIDOR_CONNECTED,
                pos.offset(direction, 8),
                direction,
                depth + 1
            ) else null
        return Site63CrossingComponent(pos, direction, left, right, front, this)
    }

}
