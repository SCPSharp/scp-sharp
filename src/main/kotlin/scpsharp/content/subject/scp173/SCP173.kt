/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp173

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.world.World
import scpsharp.util.id

object SCP173 {

    val ROTATE_SOUND: SoundEvent = SoundEvent.of(id("scp173_rotate"))

    init {
        SCP173Entity
        Registry.register(Registries.SOUND_EVENT, ROTATE_SOUND.id, ROTATE_SOUND)
    }

}

object SCP173Client {

    init {
        SCP173EntityRenderer
    }

}

fun World.canSCP173MoveNow() = (time and 0b0111) == 0L
