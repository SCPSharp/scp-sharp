/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp914

import scpsharp.util.id
import scpsharp.util.logger
import net.minecraft.sound.SoundEvent
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.registry.Registry

object SCP914 {

    val logger = logger("SCP-914")

    val modeProperty: EnumProperty<SCP914Mode> = EnumProperty.of("mode", SCP914Mode::class.java)

    val workSoundEvent = SoundEvent(id("scp914_work"))

    init {
        SCP914ControllerBlock
        SCP914FrameworkBlock
        SCP914Recipe
        Registry.register(Registry.SOUND_EVENT, workSoundEvent.id, workSoundEvent)
    }

}

enum class SCP914Mode : StringIdentifiable {

    ROUGH, COARSE, NORMAL, FINE, VERY_FINE;

    override fun asString() = name.lowercase()

}
