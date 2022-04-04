/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp714

import scpsharp.util.id
import net.minecraft.entity.EntityType
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object SCP714 {

    val bypassTag: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp714_bypass"))

    val usingStat = id("using_scp714")

    val sleepWithStat = id("sleep_with_scp714")

    init {
        SCP714Item

        Registry.register(Registry.CUSTOM_STAT, usingStat, usingStat)
        Stats.CUSTOM.getOrCreateStat(usingStat, StatFormatter.TIME)
        Registry.register(Registry.CUSTOM_STAT, sleepWithStat, sleepWithStat)
        Stats.CUSTOM.getOrCreateStat(sleepWithStat, StatFormatter.DEFAULT)
    }

}