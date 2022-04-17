/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry
import scpsharp.util.id

object ComponentTags {

    val facilityKeep: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, id("facility_keep"))

    val facilityStart: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("facility_start"))
    val facilityPart: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("facility_part"))
    val facilityContainmentRoom: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("facility_containment_room"))
    val facilityCorridor: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("facility_corridor"))
    val facilityCrossing: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("facility_crossing"))

    val site63: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("site63"))
    val site63Start: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("site63_start"))
    val site63Generating: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("site63_generating"))
    val site63Corridor: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("site63_corridor"))
    val site63Crossing: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.registryKey, id("site63_crossing"))

}
