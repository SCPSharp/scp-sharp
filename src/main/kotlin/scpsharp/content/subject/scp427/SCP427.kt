/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp427

import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import scpsharp.util.id

object SCP427 {

    val BYPASS_TAG: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id("scp427_bypass"))

    val OPENING_STAT = id("opening_scp427")
    val CLOSING_STAT = id("closing_scp427")

    val APPLYING_STAT = id("applying_scp427")

    init {
        SCP427Item

        Registry.register(Registries.CUSTOM_STAT, OPENING_STAT, OPENING_STAT)
        Stats.CUSTOM.getOrCreateStat(OPENING_STAT, StatFormatter.DEFAULT)
        Registry.register(Registries.CUSTOM_STAT, CLOSING_STAT, CLOSING_STAT)
        Stats.CUSTOM.getOrCreateStat(CLOSING_STAT, StatFormatter.DEFAULT)
        Registry.register(Registries.CUSTOM_STAT, APPLYING_STAT, APPLYING_STAT)
        Stats.CUSTOM.getOrCreateStat(APPLYING_STAT, StatFormatter.TIME)
    }

}