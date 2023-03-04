/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp008

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import scpsharp.util.id
import scpsharp.util.logger

object SCP008 {

    val LOGGER = logger("SCP-008")

    val INFECTING_STAT = id("infecting_scp008_from_containment_box")

    val DYING_STAT = id("dying_scp008")

    init {
        SCP008ContainmentBlock
        SCP008StatusEffect
        scpsharp.subject.scp008.AntiSCP008Suit

        Registry.register(Registries.CUSTOM_STAT, INFECTING_STAT, INFECTING_STAT)
        Stats.CUSTOM.getOrCreateStat(INFECTING_STAT, StatFormatter.DEFAULT)
        Registry.register(Registries.CUSTOM_STAT, DYING_STAT, DYING_STAT)
        Stats.CUSTOM.getOrCreateStat(DYING_STAT, StatFormatter.DEFAULT)
    }

}

object SCP008Client {

    init {
        SCP008OverlayRenderer
    }

}
