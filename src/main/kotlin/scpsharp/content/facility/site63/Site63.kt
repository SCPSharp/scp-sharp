/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import scpsharp.content.facility.generator.ComponentFactory
import scpsharp.content.facility.generator.ComponentTags
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.content.facility.generator.StructureComponentFactory
import scpsharp.content.facility.site63.components.SCP173ContainmentRoomComponentFactory
import scpsharp.content.facility.site63.components.Site63CorridorComponentFactory
import scpsharp.content.facility.site63.components.Site63CrossingComponentFactory
import scpsharp.util.id

object Site63 {

    val GATE_ID = id("site63_gate")
    val GATE_FACTORY: StructureComponentFactory =
        StructureComponentFactory(GATE_ID) { generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int ->
            arrayOf(
                generator.randomComponentRef(
                    ComponentTags.SITE63_CORRIDOR_CONNECTED,
                    pos.offset(direction, 10).offset(direction.rotateYCounterclockwise()),
                    direction,
                    depth
                )
            )
        }

    init {
        Site63Feature
        Site63CorridorComponentFactory
        Site63CrossingComponentFactory
        SCP173ContainmentRoomComponentFactory
        Registry.register(ComponentFactory.REGISTRY, GATE_ID, GATE_FACTORY)
    }

}