/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp008

import scpsharp.util.id
import scpsharp.util.logger
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.util.registry.Registry

object SCP008 {

    val logger = logger("SCP-008")

    val infectingStat = id("infecting_scp008_from_containment_box")

    val dyingStat = id("dying_scp008")

    init {
        SCP008ContainmentBlock
        SCP008StatusEffect

        Registry.register(Registry.CUSTOM_STAT, infectingStat, infectingStat)
        Stats.CUSTOM.getOrCreateStat(infectingStat, StatFormatter.DEFAULT)
        Registry.register(Registry.CUSTOM_STAT, dyingStat, dyingStat)
        Stats.CUSTOM.getOrCreateStat(dyingStat, StatFormatter.DEFAULT)
    }

}

object SCP008Client {

    init {
        SCP008OverlayRenderer
    }

}
