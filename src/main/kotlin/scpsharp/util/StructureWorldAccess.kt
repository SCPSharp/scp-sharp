/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.StructureWorldAccess

operator fun StructureWorldAccess.set(pos: BlockPos, state: BlockState) =
    setBlockState(pos, state, Block.NOTIFY_LISTENERS)

operator fun StructureWorldAccess.set(pos: BlockPos, block: Block) = set(pos, block.defaultState)

operator fun StructureWorldAccess.set(chunkBox: BlockBox, pos: BlockPos, block: Block) = pos !in chunkBox
        || set(pos, block.defaultState)

fun StructureWorldAccess.fillBlocks(box: BlockBox, state: BlockState) {
    for (x in box.minX..box.maxX) {
        for (y in box.minY..box.maxY) {
            for (z in box.minZ..box.maxZ) {
                set(BlockPos(x, y, z), state)
            }
        }
    }
}

fun StructureWorldAccess.fillBlocks(box: BlockBox, block: Block) = fillBlocks(box, block.defaultState)
