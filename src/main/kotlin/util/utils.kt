package com.xtex.scpsharp.util

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import org.slf4j.LoggerFactory

fun id(path: String) = Identifier("scpsharp", path)

fun logger(name: String) = LoggerFactory.getLogger("SCPSharp/$name")
