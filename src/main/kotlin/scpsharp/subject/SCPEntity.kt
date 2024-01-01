/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.World
import scpsharp.util.id

open class SCPEntity(entityType: EntityType<out scpsharp.subject.SCPEntity>, world: World) :
    PathAwareEntity(entityType, world) {

    companion object {

        val SUBJECT_TAG: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id("scp_subject"))

        val IGNORE_TAG: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id("scp_ignore"))

    }

    override fun cannotDespawn() = true

    override fun isDisallowedInPeaceful() = true

}
