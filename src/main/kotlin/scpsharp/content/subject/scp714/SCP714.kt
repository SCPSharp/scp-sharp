/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.subject.scp714

import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import scpsharp.util.id

object SCP714 {

    val BYPASS_TAG: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id("scp714_bypass"))

    val USING_STAT = id("using_scp714")

    val SLEEP_WITH_STAT = id("sleep_with_scp714")

    init {
        SCP714Item

        Registry.register(Registries.CUSTOM_STAT, USING_STAT, USING_STAT)
        Stats.CUSTOM.getOrCreateStat(USING_STAT, StatFormatter.TIME)
        Registry.register(Registries.CUSTOM_STAT, SLEEP_WITH_STAT, SLEEP_WITH_STAT)
        Stats.CUSTOM.getOrCreateStat(SLEEP_WITH_STAT, StatFormatter.DEFAULT)
    }

}