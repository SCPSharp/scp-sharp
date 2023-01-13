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
                        Site63Corridor(depth, pos.down(5), direction, len)
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
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("CPos"))
        this.length = nbt.getInt("CLen")
    }

    override fun writeNbt(context: StructureContext, nbt: NbtCompound) {
        nbt.put("CPos", NbtHelper.fromBlockPos(pos))
        nbt.putInt("CLen", length)
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
        val xRange = boundingBox.minX.coerceAtLeast(chunkBox.minX)..boundingBox.maxX.coerceAtMost(chunkBox.maxX)
        val yRange = boundingBox.minY.coerceAtLeast(chunkBox.minY)..boundingBox.maxY.coerceAtMost(chunkBox.maxY)
        val zRange = boundingBox.minZ.coerceAtLeast(chunkBox.minZ)..boundingBox.maxZ.coerceAtMost(chunkBox.maxZ)
        val pos = BlockPos.Mutable()
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
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
            // @TODO: Use frog light after 1.19
            var pos = this.pos.offset(direction, i).add(0, 5, 0)
                .offset(ycwDirection, 2)
            if (pos in chunkBox) {
                world[pos] = lightBlock
            }
            pos = pos.offset(ycwDirection)
            if (pos in chunkBox) {
                world[pos] = lightBlock
            }
        }
    }

}