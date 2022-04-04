/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp173

import scpsharp.util.id
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object SCP173 {

    val rotateSound = SoundEvent(id("scp173_rotate"))

    init {
        SCP173Entity
        Registry.register(Registry.SOUND_EVENT, rotateSound.id, rotateSound)
    }

}

object SCP173Client {

    init {
        SCP173EntityRenderer
    }

}

fun World.canSCP173MoveNow() = (time and 0b0111) == 0L
