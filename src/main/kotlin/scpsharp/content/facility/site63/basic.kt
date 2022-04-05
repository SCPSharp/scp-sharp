/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63

import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import scpsharp.content.facility.generator.ComponentFactory
import scpsharp.content.facility.generator.ComponentTags
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.content.facility.generator.StructureComponentFactory
import scpsharp.content.facility.site63.components.Site63CorridorComponentFactory
import scpsharp.util.id

object Site63 {

    val gateId = id("site63_gate")
    val gateFactory: StructureComponentFactory =
        StructureComponentFactory(gateId, mirror = BlockMirror.FRONT_BACK) { generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int ->
            arrayOf(
                generator.randomComponentRef(
                    ComponentTags.site63Corridor,
                    pos.offset(direction, 9),
                    direction,
                    depth
                )
            )
        }

    init {
        Site63Feature
        Site63CorridorComponentFactory
        Registry.register(ComponentFactory.registry, gateId, gateFactory)
    }

}