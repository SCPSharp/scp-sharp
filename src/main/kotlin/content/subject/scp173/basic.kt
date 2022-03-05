/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.content.subject.scp173

import com.xtex.scpsharp.util.id
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
