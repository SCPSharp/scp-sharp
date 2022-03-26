/*
 * Copyright (C) 2022  SCPSharp Team
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
package scpsharp.content.facility.site63

import net.minecraft.util.registry.Registry
import scpsharp.content.facility.generator.ComponentFactory
import scpsharp.content.facility.generator.StructureComponentFactory
import scpsharp.util.id

object Site63 {

    val gateId = id("site63_gate")
    val gateFactory: StructureComponentFactory = StructureComponentFactory(id("site63_gate"))

    init {
        Site63Feature
        Registry.register(ComponentFactory.registry, gateId, gateFactory)
    }

}