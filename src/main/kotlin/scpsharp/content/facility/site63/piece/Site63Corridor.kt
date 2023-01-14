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
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import scpsharp.content.facility.FacilityGenerator
import scpsharp.content.facility.FacilityStructurePiece
import scpsharp.content.facility.site63.Site63Tags
import scpsharp.util.BlockBox
import scpsharp.util.coerce
import scpsharp.util.id
import scpsharp.util.set

class Site63Corridor : FacilityStructurePiece {

    companion object {

        val IDENTIFIER = id("site63/corridor")
        val TYPE = object : Type {

            override fun invoke(generator: FacilityGenerator, pos: BlockPos, direction: Direction) =
                generator.depth > 10 || generator.piece {
                    val len = ctx.random.nextBetween(8, 20)
                    add(
                        Site63Corridor(depth, pos, direction, len)
                    ) && generator.random(
                        Site63Tags.CORRIDOR_CONNECTED,
                        pos.offset(direction, len + 1),
                        direction
                    )
                }

            override fun load(context: StructureContext, nbt: NbtCompound) =
                Site63Corridor(nbt)

        }

        init {
            Registry.register(Registries.STRUCTURE_PIECE, IDENTIFIER, TYPE)
        }

        fun calculateBoundingBox(pos: BlockPos, direction: Direction, length: Int) = BlockBox(
            pos, pos.offset(direction.rotateYClockwise(), 5)
                .offset(direction, length)
                .add(0, 5, 0)
        )

    }

    private val pos: BlockPos
    private val length: Int

    constructor(depth: Int, pos: BlockPos, direction: Direction, length: Int) : super(
        TYPE, depth, calculateBoundingBox(pos, direction, length)
    ) {
        this.pos = pos
        this.length = length
        this.setOrientation(direction)
    }

    constructor(nbt: NbtCompound) : super(TYPE, nbt) {
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("Pos"))
        this.length = nbt.getInt("Len")
    }

    override fun writeNbt(context: StructureContext, nbt: NbtCompound) {
        nbt.put("Pos", NbtHelper.fromBlockPos(pos))
        nbt.putInt("Len", length)
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
        val box = boundingBox.coerce(chunkBox)
        val pos = BlockPos.Mutable()
        for (x in box.minX..box.maxX) {
            for (y in box.minY..box.maxY) {
                for (z in box.minZ..box.maxZ) {
                    pos.set(x, y, z)
                    val border = ((x == boundingBox.minX || x == boundingBox.maxX) && direction.offsetX == 0)
                            || ((y == boundingBox.minY || y == boundingBox.maxY) && direction.offsetY == 0)
                            || ((pos.z == boundingBox.minZ || z == boundingBox.maxZ) && direction.offsetZ == 0)
                    world[pos] = if (border) Blocks.POLISHED_DEEPSLATE else Blocks.AIR
                }
            }
        }
        // Light
        val ycwDirection = direction.rotateYClockwise()
        val lightBlock = if (Xoroshiro128PlusPlusRandom(chunkPos.toLong()).nextInt(20) == 2)
            Blocks.VERDANT_FROGLIGHT else Blocks.SEA_LANTERN
        for (i in 0 until length) {
            var lightPos = this.pos.offset(direction, i).add(0, 5, 0)
                .offset(ycwDirection, 2)
            if (lightPos in chunkBox) {
                world[lightPos] = lightBlock
            }
            lightPos = lightPos.offset(ycwDirection)
            if (lightPos in chunkBox) {
                world[lightPos] = lightBlock
            }
        }
    }

}