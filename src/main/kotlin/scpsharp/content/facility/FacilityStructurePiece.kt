package scpsharp.content.facility

import net.minecraft.nbt.NbtCompound
import net.minecraft.structure.StructureContext
import net.minecraft.structure.StructurePiece
import net.minecraft.structure.StructurePieceType
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator

abstract class FacilityStructurePiece : StructurePiece {

    constructor(type: StructurePieceType, length: Int, boundingBox: BlockBox) : super(type, length, boundingBox)
    constructor(type: StructurePieceType, nbt: NbtCompound) : super(type, nbt)

    abstract override fun writeNbt(context: StructureContext, nbt: NbtCompound)

    abstract override fun generate(
        world: StructureWorldAccess,
        structureAccessor: StructureAccessor,
        chunkGenerator: ChunkGenerator,
        random: Random,
        chunkBox: BlockBox,
        chunkPos: ChunkPos,
        pivot: BlockPos
    )

    interface Type : StructurePieceType {

        override fun load(context: StructureContext, nbt: NbtCompound): StructurePiece

        operator fun invoke(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

    }

}
