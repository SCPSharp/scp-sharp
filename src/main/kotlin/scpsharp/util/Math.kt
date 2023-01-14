package scpsharp.util

import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import kotlin.math.max
import kotlin.math.min

fun BlockBox(pos1: BlockPos, pos2: BlockPos): BlockBox =
    BlockBox.create(Vec3i(pos1.x, pos1.y, pos1.z), Vec3i(pos2.x, pos2.y, pos2.z))

operator fun BlockBox.plus(box: BlockBox) = BlockBox(
    min(minX, box.minX), min(minY, box.minY), min(minZ, box.minZ),
    max(maxX, box.maxX), max(maxY, box.maxY), max(maxZ, box.maxZ),
)

fun BlockBox.coerce(box: BlockBox) = BlockBox(
    minX.coerceAtLeast(box.minX), minY.coerceAtLeast(box.minY), minZ.coerceAtLeast(box.minZ),
    maxX.coerceAtMost(box.maxX), maxY.coerceAtMost(box.maxY), maxZ.coerceAtMost(box.maxZ),
)

val Direction.asBlockRotation
    get() = when (this) {
        Direction.EAST -> BlockRotation.NONE
        Direction.WEST -> BlockRotation.CLOCKWISE_180
        Direction.SOUTH -> BlockRotation.CLOCKWISE_90
        Direction.NORTH -> BlockRotation.COUNTERCLOCKWISE_90
        else -> throw IllegalArgumentException(name)
    }
