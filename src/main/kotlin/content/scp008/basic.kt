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
package com.xtex.scpsharp.content.scp008

import com.xtex.scpsharp.util.logger
import java.lang.invoke.MethodHandles

object SCP008 {

    val logger = logger("SCP-008")

    init {
        MethodHandles.lookup().ensureInitialized(SCP008ContainmentBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP008StatusEffect::class.java)
    }

}

object SCP008Client {

    init {
        MethodHandles.lookup().ensureInitialized(SCP008OverlayRenderer::class.java)
    }

}
