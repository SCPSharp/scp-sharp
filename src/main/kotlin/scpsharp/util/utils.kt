/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.util

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun id(path: String) = Identifier("scpsharp", path)

fun logger(name: String): Logger = LoggerFactory.getLogger("SCPSharp/$name")
