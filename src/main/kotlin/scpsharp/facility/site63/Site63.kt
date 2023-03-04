/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.facility.site63

import scpsharp.facility.site63.piece.SCP173ContainmentRoom
import scpsharp.facility.site63.piece.Site63Corridor
import scpsharp.facility.site63.piece.Site63Crossing
import scpsharp.facility.site63.piece.Site63Gate

object Site63 {

    init {
        Site63Structure
        Site63Gate
        Site63Corridor
        Site63Crossing
        SCP173ContainmentRoom
    }

}