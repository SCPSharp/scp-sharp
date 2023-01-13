/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.util

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.world.StructureWorldAccess
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun id(path: String) = Identifier("scpsharp", path)

fun logger(name: String): Logger = LoggerFactory.getLogger("SCPSharp/$name")

fun BlockBox(pos1: BlockPos, pos2: BlockPos): BlockBox =
    BlockBox.create(Vec3i(pos1.x, pos1.y, pos1.z), Vec3i(pos2.x, pos2.y, pos2.z))

val Direction.asBlockRotation
    get() = when (this) {
        Direction.EAST -> BlockRotation.NONE
        Direction.WEST -> BlockRotation.CLOCKWISE_180
        Direction.SOUTH -> BlockRotation.CLOCKWISE_90
        Direction.NORTH -> BlockRotation.COUNTERCLOCKWISE_90
        else -> throw IllegalArgumentException(name)
    }

fun ItemGroup.addItem(item: Item) {
    ItemGroupEvents.modifyEntriesEvent(this)
        .register(ItemGroupEvents.ModifyEntries { content ->
            content.add(item)
        })
}

operator fun StructureWorldAccess.set(pos: BlockPos, state: BlockState) =
    setBlockState(pos, state, Block.NOTIFY_LISTENERS)

operator fun StructureWorldAccess.set(pos: BlockPos, block: Block) = set(pos, block.defaultState)
