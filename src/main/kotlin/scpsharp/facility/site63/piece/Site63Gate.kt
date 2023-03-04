/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.facility.site63.piece

import net.minecraft.block.Block
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.StructureContext
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import scpsharp.facility.FacilityGenerator
import scpsharp.facility.FacilityStructurePiece
import scpsharp.facility.site63.Site63Tags
import scpsharp.util.asBlockRotation
import scpsharp.util.id

class Site63Gate : FacilityStructurePiece {

    companion object {

        val IDENTIFIER = id("site63/gate")
        val TYPE = object : Type {

            override fun invoke(generator: FacilityGenerator, pos: BlockPos, direction: Direction) = generator.piece {
                add(
                    Site63Gate(
                        ctx.structureTemplateManager.getTemplateOrBlank(IDENTIFIER)!!,
                        depth,
                        pos.down(5),
                        direction
                    )
                ) && generator.random(
                    Site63Tags.CORRIDOR,
                    pos.offset(direction, 10).offset(direction.rotateYCounterclockwise()).down(5),
                    direction
                )
            }

            override fun load(context: StructureContext, nbt: NbtCompound) =
                Site63Gate(context.structureTemplateManager().getTemplateOrBlank(IDENTIFIER)!!, nbt)

        }

        init {
            Registry.register(Registries.STRUCTURE_PIECE, IDENTIFIER, TYPE)
        }

        fun createPlacementData(direction: Direction) =
            StructurePlacementData().apply { rotation = direction.asBlockRotation }

    }

    private val template: StructureTemplate
    private val pos: BlockPos

    constructor(template: StructureTemplate, depth: Int, pos: BlockPos, direction: Direction) : super(
        TYPE, depth, template.calculateBoundingBox(createPlacementData(direction), pos)
    ) {
        this.template = template
        this.pos = pos
        this.setOrientation(direction)
    }

    constructor(template: StructureTemplate, nbt: NbtCompound) : super(TYPE, nbt) {
        this.template = template
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("Pos"))
    }

    override fun writeNbt(context: StructureContext, nbt: NbtCompound) {
        nbt.put("Pos", NbtHelper.fromBlockPos(pos))
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
        val placementData = createPlacementData(facing!!)
        placementData.boundingBox = chunkBox
        boundingBox = template.calculateBoundingBox(placementData, pos)
        template.place(world, pos, pivot, placementData, random, Block.NOTIFY_LISTENERS)
    }

}