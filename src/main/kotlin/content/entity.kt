package com.xtex.scpsharp.content

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.world.World

open class SCPEntity(entityType: EntityType<out SCPEntity>, world: World) : PathAwareEntity(entityType, world), SCPIgnoredEntity

interface SCPIgnoredEntity
