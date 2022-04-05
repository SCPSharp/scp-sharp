/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.util

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun id(path: String) = Identifier("scpsharp", path)

fun logger(name: String): Logger = LoggerFactory.getLogger("SCPSharp/$name")

fun BlockBox(pos1: BlockPos, pos2: BlockPos): BlockBox = BlockBox.create(Vec3i(pos1.x, pos1.y, pos1.z), Vec3i(pos2.x, pos2.y, pos2.z))
