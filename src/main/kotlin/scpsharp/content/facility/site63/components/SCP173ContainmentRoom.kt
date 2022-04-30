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
import scpsharp.content.facility.generator.ComponentFactory
import scpsharp.content.facility.generator.ComponentRef
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.content.facility.generator.SimpleComponent
import scpsharp.content.subject.scp173.SCP173Entity
import scpsharp.util.BlockBox
import scpsharp.util.id

class SCP173ContainmentRoomComponent(
    pos: BlockPos,
    direction: Direction,
    val entityPos: BlockPos?,
    override val type: ComponentFactory<*>
) : SimpleComponent() {

    val containmentRoomBox = BlockBox(
        pos.offset(direction, 5).offset(direction.rotateYCounterclockwise(), 4),
        pos.offset(direction, 11).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
    )

    val incomingRingBox = BlockBox(
        pos,
        pos.offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
    )

    override val boxes: Array<BlockBox> = arrayOf(
        containmentRoomBox, incomingRingBox, BlockBox(
            pos.offset(direction).offset(direction.rotateYCounterclockwise(), 4),
            pos.offset(direction, 4).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 4)
        )
    )

    override val refs: Array<ComponentRef<*>> = emptyArray()

    override fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean {
        run { // Monitor Room
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction).offset(direction.rotateYCounterclockwise(), 4),
                    pos.offset(direction, 4).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 4)
                ),
                Blocks.POLISHED_DEEPSLATE
            )
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction, 2).offset(direction.rotateYCounterclockwise(), 3).offset(Direction.UP),
                    pos.offset(direction, 4).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 3)
                ),
                Blocks.AIR
            )
            // Lectern
            generator[pos.offset(direction, 4).offset(direction.rotateYCounterclockwise(), 3).offset(Direction.UP)] =
                Blocks.LECTERN
            // Light
            generator[pos.offset(direction, 3).offset(direction.rotateYCounterclockwise()).offset(Direction.UP, 4)] =
                Blocks.SEA_LANTERN
            generator[pos.offset(direction, 3).offset(direction.rotateYClockwise(), 2).offset(Direction.UP, 4)] =
                Blocks.SEA_LANTERN
        }
        run { // Containment Room
            generator.fillBlocks(containmentRoomBox, Blocks.DEEPSLATE_BRICKS)
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction, 6).offset(direction.rotateYCounterclockwise(), 3).offset(Direction.UP),
                    pos.offset(direction, 10).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                ),
                Blocks.AIR
            )
            // Light
            generator[pos.offset(direction, 7).offset(direction.rotateYCounterclockwise(), 2).offset(Direction.UP, 5)] =
                Blocks.SEA_LANTERN
            generator[pos.offset(direction, 7).offset(direction.rotateYClockwise(), 3).offset(Direction.UP, 5)] =
                Blocks.SEA_LANTERN
            // Connect to monitor room
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction, 5).offset(direction.rotateYClockwise(), 2).offset(Direction.UP),
                    pos.offset(direction, 5).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 3)
                ),
                Blocks.AIR
            )
        }
        run { // Incoming Ring
            generator.fillBlocks(incomingRingBox, Blocks.POLISHED_DEEPSLATE)
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                ),
                Blocks.AIR
            )
            // Connect to monitor room
            generator.fillBlocks(
                BlockBox(
                    pos.offset(direction).offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction).offset(direction.rotateYClockwise(), 2).offset(Direction.UP, 3)
                ),
                Blocks.AIR
            )
        }
        run { // Spawn SCP-173
            if (entityPos != null) {
                generator.spawnEntity(SCP173Entity.TYPE, entityPos)
            }
        }
        return true
    }

}

object SCP173ContainmentRoomComponentFactory : ComponentFactory<SCP173ContainmentRoomComponent>() {

    val IDENTIFIER = id("scp173_containment_room")

    init {
        Registry.register(ComponentFactory.REGISTRY, IDENTIFIER, this)
    }

    override fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int
    ): SCP173ContainmentRoomComponent {
        return SCP173ContainmentRoomComponent(
            pos,
            direction,
            if (generator.random.nextInt(10) > 3) pos.offset(direction, 6)
                .offset(Direction.UP)
                .add(generator.random.nextInt(3), 0, generator.random.nextInt(3))
            else null,
            this
        )
    }

}
