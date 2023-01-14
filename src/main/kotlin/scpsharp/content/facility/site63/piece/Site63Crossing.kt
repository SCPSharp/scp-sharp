/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63.piece

import net.minecraft.block.Blocks
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.StructureContext
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import scpsharp.content.facility.FacilityGenerator
import scpsharp.content.facility.FacilityStructurePiece
import scpsharp.content.facility.site63.Site63Tags
import scpsharp.util.BlockBox
import scpsharp.util.fillBlocks
import scpsharp.util.id

class Site63Crossing : FacilityStructurePiece {

    companion object {

        val IDENTIFIER = id("site63/crossing")
        val TYPE = object : Type {

            override fun invoke(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean {
                val genLeft = generator.ctx.random.nextInt(10) < 5
                val genFront = generator.ctx.random.nextInt(10) < 4
                val genRight = generator.ctx.random.nextInt(10) < 5
                for (left in setOf(genLeft, false)) {
                    for (front in setOf(genFront, false)) {
                        for (right in setOf(genRight, false)) {
                            if (!left && front && !right) continue
                            val succeeded = generator.piece {
                                add(Site63Crossing(depth, pos, direction, Triple(left, front, right))) &&
                                        (!left || generator.random(
                                            Site63Tags.CORRIDOR_CONNECTED,
                                            pos.offset(direction).offset(direction.rotateYCounterclockwise(), 2),
                                            direction.rotateYCounterclockwise(),
                                        )) &&
                                        (!front || generator.random(
                                            Site63Tags.CORRIDOR_CONNECTED,
                                            pos.offset(direction, 8),
                                            direction,
                                        )) &&
                                        (!right || generator.random(
                                            Site63Tags.CORRIDOR_CONNECTED,
                                            pos.offset(direction, 6).offset(direction.rotateYClockwise(), 7),
                                            direction.rotateYClockwise(),
                                        ))
                            }
                            if (succeeded) return true
                        }
                    }
                }
                return false
            }

            override fun load(context: StructureContext, nbt: NbtCompound) =
                Site63Crossing(nbt)

        }

        init {
            Registry.register(Registries.STRUCTURE_PIECE, IDENTIFIER, TYPE)
        }

        fun calculateBoundingBox(pos: BlockPos, direction: Direction) = BlockBox(
            pos.offset(direction.rotateYCounterclockwise()),
            pos.offset(direction, 7).offset(direction.rotateYClockwise(), 6).up(5)
        )

    }

    private val pos: BlockPos
    private val neighbors: Triple<Boolean, Boolean, Boolean>

    constructor(depth: Int, pos: BlockPos, direction: Direction, neighbors: Triple<Boolean, Boolean, Boolean>) : super(
        TYPE, depth, calculateBoundingBox(pos, direction)
    ) {
        this.pos = pos
        this.neighbors = neighbors
        this.setOrientation(direction)
    }

    constructor(nbt: NbtCompound) : super(TYPE, nbt) {
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("Pos"))
        this.neighbors = Triple(nbt.getBoolean("Left"), nbt.getBoolean("Front"), nbt.getBoolean("Right"))
    }

    override fun writeNbt(context: StructureContext, nbt: NbtCompound) {
        nbt.put("Pos", NbtHelper.fromBlockPos(pos))
        val (left, front, right) = neighbors
        nbt.putBoolean("Left", left)
        nbt.putBoolean("Front", front)
        nbt.putBoolean("Right", right)
    }

    override fun generate(
        world: StructureWorldAccess,
        structureAccessor: StructureAccessor,
        chunkGenerator: ChunkGenerator,
        random: Random,
        chunkBox: BlockBox,
        chunkPos: ChunkPos,
        pivot: BlockPos
    ) {
        val direction = this.facing!!
        // main
        world.fillBlocks(
            BlockBox(
                pos,
                pos.up(5)
                    .offset(direction, 6)
                    .offset(direction.rotateYClockwise(), 5)
            ), Blocks.POLISHED_DEEPSLATE
        )
        world.fillBlocks(
            BlockBox(
                pos.up(1)
                    .offset(direction.rotateYClockwise()),
                pos.up(4)
                    .offset(direction, 5)
                    .offset(direction.rotateYClockwise(), 4)
            ), Blocks.AIR
        )
        val (left, front, right) = neighbors
        if (left) {
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYCounterclockwise()).offset(direction),
                    pos.offset(direction.rotateYCounterclockwise()).offset(direction, 6).offset(Direction.UP, 5)
                ), Blocks.POLISHED_DEEPSLATE
            )
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYCounterclockwise()).offset(direction, 5).offset(Direction.UP),
                    pos.offset(direction, 2).offset(Direction.UP, 4)
                ), Blocks.AIR
            )
        }
        if (front) {
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction, 7),
                    pos.offset(direction, 7).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
                ), Blocks.POLISHED_DEEPSLATE
            )
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction, 7).offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction, 6).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                ), Blocks.AIR
            )
        }
        if (right) {
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYClockwise(), 6).offset(direction),
                    pos.offset(direction.rotateYClockwise(), 6).offset(direction, 6).offset(Direction.UP, 5)
                ), Blocks.POLISHED_DEEPSLATE
            )
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYClockwise(), 6).offset(direction, 2).offset(Direction.UP),
                    pos.offset(direction.rotateYClockwise(), 5).offset(direction, 5).offset(Direction.UP, 4)
                ), Blocks.AIR
            )
        }
    }

}