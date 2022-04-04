/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.world.World

open class SCPEntity(entityType: EntityType<out SCPEntity>, world: World) : PathAwareEntity(entityType, world),
    SCPIgnoredEntity {

    override fun cannotDespawn() = true

    override fun isDisallowedInPeaceful() = true

}

/* @TODO: move to tag */
interface SCPIgnoredEntity
