/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.content.scp914

import com.xtex.scpsharp.util.id
import com.xtex.scpsharp.util.logger
import net.minecraft.sound.SoundEvent
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.registry.Registry
import java.lang.invoke.MethodHandles

object SCP914 {

    val logger = logger("SCP-914")

    val modeProperty: EnumProperty<SCP914Mode> = EnumProperty.of("mode", SCP914Mode::class.java)

    val workingSoundEvent = SoundEvent(id("scp914_working"))

    init {
        MethodHandles.lookup().ensureInitialized(SCP914FrameworkBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP914ControllerBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP914Recipe::class.java)
        Registry.register(Registry.SOUND_EVENT, workingSoundEvent.id, workingSoundEvent)
    }

}

enum class SCP914Mode : StringIdentifiable {

    VERY_BAD, BAD, NORMAL, FINE, VERY_FINE;

    override fun asString() = name.lowercase()

    val next
        get() = when (this) {
            VERY_BAD -> BAD
            BAD -> NORMAL
            NORMAL -> FINE
            FINE -> VERY_FINE
            VERY_FINE -> VERY_BAD
        }

}
