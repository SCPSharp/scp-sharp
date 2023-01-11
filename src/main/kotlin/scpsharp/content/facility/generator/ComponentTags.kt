/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.facility.generator

import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id

object ComponentTags {

    val FACILITY_KEEP: TagKey<Block> = TagKey.of(Registries.BLOCK.key, id("facility_keep"))

    val FACILITY_START: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("facility_start"))
    val FACILITY_PART: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("facility_part"))
    val FACILITY_CONTAINMENT_ROOM: TagKey<ComponentFactory<*>> =
        TagKey.of(ComponentFactory.REGISTRY_KEY, id("facility_containment_room"))
    val FACILITY_CORRIDOR: TagKey<ComponentFactory<*>> =
        TagKey.of(ComponentFactory.REGISTRY_KEY, id("facility_corridor"))
    val FACILITY_CROSSING: TagKey<ComponentFactory<*>> =
        TagKey.of(ComponentFactory.REGISTRY_KEY, id("facility_crossing"))

    val SITE63: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63"))
    val SITE63_START: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63_start"))
    val SITE63_GENERATING: TagKey<ComponentFactory<*>> =
        TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63_generating"))
    val SITE63_CORRIDOR: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63_corridor"))
    val SITE63_CORRIDOR_CONNECTED: TagKey<ComponentFactory<*>> =
        TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63_corridor_connected"))
    val SITE63_CROSSING: TagKey<ComponentFactory<*>> = TagKey.of(ComponentFactory.REGISTRY_KEY, id("site63_crossing"))

}
