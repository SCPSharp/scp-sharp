/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp914

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import scpsharp.util.id
import scpsharp.util.logger

object SCP914 {

    val LOGGER = logger("SCP-914")

    val MODE_PROPERTY: EnumProperty<SCP914Mode> = EnumProperty.of("mode", SCP914Mode::class.java)

    val WORK_SOUND_EVENT = SoundEvent.of(id("scp914_work"))

    init {
        SCP914ControllerBlock
        SCP914FrameworkBlock
        SCP914Recipe
        Registry.register(Registries.SOUND_EVENT, WORK_SOUND_EVENT.id, WORK_SOUND_EVENT)
    }

}

enum class SCP914Mode : StringIdentifiable {

    ROUGH, COARSE, NORMAL, FINE, VERY_FINE;

    override fun asString() = name.lowercase()

}
