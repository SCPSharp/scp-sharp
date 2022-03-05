/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.content.scp008

import com.xtex.scpsharp.content.scp714.SCP714
import com.xtex.scpsharp.util.id
import com.xtex.scpsharp.util.logger
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
