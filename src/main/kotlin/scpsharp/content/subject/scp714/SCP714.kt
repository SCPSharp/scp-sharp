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

    val BYPASS_TAG: TagKey<EntityType<*>> = TagKey.of(Registry.ENTITY_TYPE_KEY, id("scp714_bypass"))

    val USING_STAT = id("using_scp714")

    val SLEEP_WITH_STAT = id("sleep_with_scp714")

    init {
        SCP714Item

        Registry.register(Registry.CUSTOM_STAT, USING_STAT, USING_STAT)
        Stats.CUSTOM.getOrCreateStat(USING_STAT, StatFormatter.TIME)
        Registry.register(Registry.CUSTOM_STAT, SLEEP_WITH_STAT, SLEEP_WITH_STAT)
        Stats.CUSTOM.getOrCreateStat(SLEEP_WITH_STAT, StatFormatter.DEFAULT)
    }

}