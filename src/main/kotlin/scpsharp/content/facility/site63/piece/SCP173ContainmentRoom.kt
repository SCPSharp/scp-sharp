package scpsharp.content.facility.site63.piece

import net.minecraft.block.Blocks
import net.minecraft.entity.SpawnReason
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
import scpsharp.content.subject.scp173.SCP173Entity
import scpsharp.util.*

class SCP173ContainmentRoom : FacilityStructurePiece {

    companion object {

        val IDENTIFIER = id("site63/scp173_containment_room")
        val TYPE: Type = object : Type {

            override fun invoke(generator: FacilityGenerator, pos: BlockPos, direction: Direction) =
                generator.count(SCP173ContainmentRoom::class) == 0 && generator.piece {
                    add(
                        SCP173ContainmentRoom(
                            depth, pos, direction,
                            if (generator.ctx.random.nextInt(10) > 3)
                                pos.offset(direction, 6)
                                    .up()
                                    .add(
                                        generator.ctx.random.nextInt(2),
                                        0, generator.ctx.random.nextInt(2)
                                    )
                            else null,
                        )
                    )
                }

            override fun load(context: StructureContext, nbt: NbtCompound) =
                SCP173ContainmentRoom(nbt)

        }

        init {
            Registry.register(Registries.STRUCTURE_PIECE, IDENTIFIER, TYPE)
        }

        fun calculateBoundingBox(pos: BlockPos, direction: Direction) = BlockBox(
            pos.offset(direction.rotateYClockwise(), 5),
            pos.offset(direction.rotateYCounterclockwise(), 4)
                .offset(direction, 11)
                .up(5)
        )

    }

    private val pos: BlockPos
    private val entityPos: BlockPos?

    constructor(depth: Int, pos: BlockPos, direction: Direction, entityPos: BlockPos?) : super(
        TYPE, depth, calculateBoundingBox(pos, direction)
    ) {
        this.pos = pos
        this.entityPos = entityPos
        this.setOrientation(direction)
    }

    constructor(nbt: NbtCompound) : super(TYPE, nbt) {
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("Pos"))
        this.entityPos = if ("EPos" in nbt) NbtHelper.toBlockPos(nbt.getCompound("EPos")) else null
    }

    override fun writeNbt(context: StructureContext, nbt: NbtCompound) {
        nbt.put("Pos", NbtHelper.fromBlockPos(pos))
        if (entityPos != null) nbt.put("EPos", NbtHelper.fromBlockPos(entityPos))
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
        // Monitor Room
        val monitorRoomBox = BlockBox(
            pos.offset(direction).offset(direction.rotateYCounterclockwise(), 4),
            pos.offset(direction, 4).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 4)
        )
        if (monitorRoomBox.intersects(chunkBox)) {
            world.fillBlocks(monitorRoomBox.coerce(chunkBox), Blocks.POLISHED_DEEPSLATE)
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction, 2).offset(direction.rotateYCounterclockwise(), 3).offset(Direction.UP),
                    pos.offset(direction, 4).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 3)
                ).coerce(chunkBox),
                Blocks.AIR
            )
            // Lectern
            world[chunkBox, pos.offset(direction, 4).offset(direction.rotateYCounterclockwise(), 3)
                .offset(Direction.UP)] = Blocks.LECTERN
            // Light
            world[chunkBox, pos.offset(direction, 3).offset(direction.rotateYCounterclockwise())
                .offset(Direction.UP, 4)] = Blocks.SEA_LANTERN
            world[chunkBox, pos.offset(direction, 3).offset(direction.rotateYClockwise(), 2).offset(Direction.UP, 4)] =
                Blocks.SEA_LANTERN
        }

        // Containment Room
        val containmentRoomBox = BlockBox(
            pos.offset(direction, 5).offset(direction.rotateYCounterclockwise(), 4),
            pos.offset(direction, 11).offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
        )
        if (containmentRoomBox.intersects(chunkBox)) {
            world.fillBlocks(containmentRoomBox.coerce(chunkBox), Blocks.DEEPSLATE_BRICKS)
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction, 6).offset(direction.rotateYCounterclockwise(), 3).offset(Direction.UP),
                    pos.offset(direction, 10).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                ).coerce(chunkBox),
                Blocks.AIR
            )
            // Light
            world[chunkBox, pos.offset(direction, 7).offset(direction.rotateYCounterclockwise(), 2)
                .offset(Direction.UP, 5)] =
                Blocks.PEARLESCENT_FROGLIGHT
            world[chunkBox, pos.offset(direction, 7).offset(direction.rotateYClockwise(), 3).offset(Direction.UP, 5)] =
                Blocks.PEARLESCENT_FROGLIGHT
            // Connect to monitor room
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction, 5).offset(direction.rotateYClockwise(), 2).offset(Direction.UP),
                    pos.offset(direction, 5).offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 3)
                ).coerce(chunkBox),
                Blocks.AIR
            )
        }
        // Incoming Ring
        val incomingRingBox = BlockBox(
            pos,
            pos.offset(direction.rotateYClockwise(), 5).offset(Direction.UP, 5)
        )
        if (incomingRingBox.intersects(chunkBox)) {
            world.fillBlocks(incomingRingBox.coerce(chunkBox), Blocks.POLISHED_DEEPSLATE)
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction.rotateYClockwise(), 4).offset(Direction.UP, 4)
                ).coerce(chunkBox),
                Blocks.AIR
            )
            // Connect to monitor room
            world.fillBlocks(
                BlockBox(
                    pos.offset(direction).offset(direction.rotateYClockwise()).offset(Direction.UP),
                    pos.offset(direction).offset(direction.rotateYClockwise(), 2).offset(Direction.UP, 3)
                ).coerce(chunkBox),
                Blocks.AIR
            )
        }
        // Spawn SCP-173
        if (entityPos != null && entityPos in chunkBox) {
            world.spawnEntity(SCP173Entity.TYPE.spawn(world.toServerWorld(), entityPos, SpawnReason.STRUCTURE))
        }
    }

}