/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp427

import scpsharp.util.id
import net.minecraft.entity.EntityType
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object SCP427 {

    val bypassTag: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp427_bypass"))

    val openingStat = id("opening_scp427")

    val closingStat = id("closing_scp427")

    val applyingStat = id("applying_scp427")

    init {
        SCP427Item

        Registry.register(Registry.CUSTOM_STAT, openingStat, openingStat)
        Stats.CUSTOM.getOrCreateStat(openingStat, StatFormatter.DEFAULT)
        Registry.register(Registry.CUSTOM_STAT, closingStat, closingStat)
        Stats.CUSTOM.getOrCreateStat(closingStat, StatFormatter.DEFAULT)
        Registry.register(Registry.CUSTOM_STAT, applyingStat, applyingStat)
        Stats.CUSTOM.getOrCreateStat(applyingStat, StatFormatter.TIME)
    }

}