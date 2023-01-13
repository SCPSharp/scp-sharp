/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility

import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.structure.StructurePiece
import net.minecraft.structure.StructurePieceType
import net.minecraft.structure.StructurePiecesCollector
import net.minecraft.structure.StructurePiecesHolder
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.structure.Structure
import kotlin.jvm.optionals.getOrNull

class FacilityGenerator(val ctx: Structure.Context) : StructurePiecesHolder {

    val pieces = StackAllocator<StructurePiece> { a, b -> a.boundingBox.intersects(b.boundingBox) }
    val depth by pieces::depth

    inline fun piece(crossinline content: FacilityGenerator.() -> Boolean): Boolean {
        pieces.push()
        return if (content()) {
            pieces.squash()
            true
        } else {
            pieces.drop()
            false
        }
    }

    fun add(piece: StructurePiece) = pieces.tryAllocate(piece)

    override fun addPiece(piece: StructurePiece) {
        pieces.allocate(piece)
    }

    override fun getIntersecting(box: BlockBox) = pieces.find { it.boundingBox.intersects(box) }

    fun collect(collector: StructurePiecesCollector) {
        pieces.freeze()
        check(depth == 1) { "Trying to collect structure pieces when there are more than one layers" }
        val piecesIter = pieces.iterator()
        check(piecesIter.hasNext()) { "Nothing to collect" }
        piecesIter.forEach { collector.addPiece(it) }
    }

    fun random(tag: TagKey<StructurePieceType>, pos: BlockPos, direction: Direction) =
        Registries.STRUCTURE_PIECE.getOrCreateEntryList(tag)
            .toList()
            .run {
                if (isEmpty()) error("Nothing registered in $tag")
                val randomEntry = elementAt(ctx.random.nextInt(size))!!
                val random = randomEntry.keyOrValue
                val value = random.left().getOrNull()?.value?.let { Registries.STRUCTURE_PIECE.get(it) }
                    ?: random.right().get()
                if (value is FacilityStructurePiece.Type) {
                    value.invoke(this@FacilityGenerator, pos, direction)
                } else {
                    error("$tag registered $randomEntry but is SCP# incompatible structure pieces")
                }
            }

}