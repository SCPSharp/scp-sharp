/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import scpsharp.util.id

open class SCPEntity(entityType: EntityType<out SCPEntity>, world: World) : PathAwareEntity(entityType, world) {

    companion object {

        val SUBJECT_TAG: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp_subject"))

        val IGNORE_TAG: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp_ignore"))

    }

    override fun cannotDespawn() = true

    override fun isDisallowedInPeaceful() = true

}
