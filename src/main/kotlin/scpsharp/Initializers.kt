/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp

import scpsharp.content.facility.site63.Site63
import scpsharp.content.misc.SCPMisc
import scpsharp.content.subject.SCPSubjects
import scpsharp.content.subject.SCPSubjectsClient

fun initMain() {
    SCPMisc
    SCPSubjects
    Site63
}

fun initClient() {
    SCPSubjectsClient
}
