/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp500

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import scpsharp.util.id
import scpsharp.util.logger

object SCP500 {

    val LOGGER = logger("SCP-500")

    val TAKING_OUT_STAT = id("take_out_scp500")

    val EATING_STAT = id("eat_scp500")

    init {
        SCP5001Item
        SCP500JarItem

        Registry.register(Registries.CUSTOM_STAT, TAKING_OUT_STAT, TAKING_OUT_STAT)
        Stats.CUSTOM.getOrCreateStat(TAKING_OUT_STAT, StatFormatter.DEFAULT)
        Registry.register(Registries.CUSTOM_STAT, EATING_STAT, EATING_STAT)
        Stats.CUSTOM.getOrCreateStat(EATING_STAT, StatFormatter.DEFAULT)
    }

}
