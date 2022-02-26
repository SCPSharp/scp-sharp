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
package com.xtex.scpsharp

import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008Client
import com.xtex.scpsharp.content.scp173.SCP173
import com.xtex.scpsharp.content.scp173.SCP173Client
import com.xtex.scpsharp.content.scp500.SCP500
import com.xtex.scpsharp.content.scp914.SCP914
import java.lang.invoke.MethodHandles

fun initMain() {
    MethodHandles.lookup().ensureInitialized(SCP008::class.java)
    MethodHandles.lookup().ensureInitialized(SCP173::class.java)
    MethodHandles.lookup().ensureInitialized(SCP500::class.java)
    MethodHandles.lookup().ensureInitialized(SCP914::class.java)
}

fun initClient() {
    MethodHandles.lookup().ensureInitialized(SCP008Client::class.java)
    MethodHandles.lookup().ensureInitialized(SCP173Client::class.java)
}
