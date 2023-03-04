/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp

import scpsharp.facility.site63.Site63
import scpsharp.misc.SCPMisc
import scpsharp.subject.SCPSubjects
import scpsharp.subject.SCPSubjectsClient

fun initMain() {
    SCPMisc
    scpsharp.subject.SCPSubjects
    Site63
}

fun initClient() {
    scpsharp.subject.SCPSubjectsClient
}
