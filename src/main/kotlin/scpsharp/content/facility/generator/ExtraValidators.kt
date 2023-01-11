/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.facility.generator

object ExtraValidators {

    fun maxDepth(component: Component, minMaxDepth: Int): Boolean {
        val depth = mutableMapOf(component to 0)
        while (depth.isNotEmpty()) {
            if (depth.values.any { it >= minMaxDepth }) {
                return true
            }
            depth.entries.forEach {
                if (it is SimpleComponent) {
                    it.refs.forEach { ref ->
                        if (ref.component != null) {
                            depth[ref.component!!] = it.value + 1
                        }
                    }
                }
                depth.remove(it.key)
            }
        }
        return false
    }

}
