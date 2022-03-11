/*
 * Copyright (C) 2022  SCPSharp
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
package scpsharp.content.subject.scp500

import scpsharp.util.id
import scpsharp.util.logger
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.util.registry.Registry

object SCP500 {

    val logger = logger("SCP-500")

    val takingOutStat = id("take_out_scp500")

    val eatingStat = id("eat_scp500")

    init {
        SCP5001Item
        SCP500JarItem

        Registry.register(Registry.CUSTOM_STAT, takingOutStat, takingOutStat)
        Stats.CUSTOM.getOrCreateStat(takingOutStat, StatFormatter.DEFAULT)
        Registry.register(Registry.CUSTOM_STAT, eatingStat, eatingStat)
        Stats.CUSTOM.getOrCreateStat(eatingStat, StatFormatter.DEFAULT)
    }

}
