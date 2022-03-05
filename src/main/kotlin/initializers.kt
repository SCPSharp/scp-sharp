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
package com.xtex.scpsharp

import com.xtex.scpsharp.content.scp005.SCP005
import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008Client
import com.xtex.scpsharp.content.scp173.SCP173
import com.xtex.scpsharp.content.scp173.SCP173Client
import com.xtex.scpsharp.content.scp427.SCP427
import com.xtex.scpsharp.content.scp500.SCP500
import com.xtex.scpsharp.content.scp714.SCP714
import com.xtex.scpsharp.content.scp914.SCP914

fun initMain() {
    SCP005
    SCP008
    SCP173
    SCP427
    SCP500
    SCP714
    SCP914
}

fun initClient() {
    SCP008Client
    SCP173Client
}
