/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility

import java.util.*

class StackAllocator<T>(val conflictMatcher: (T, T) -> Boolean) : Iterable<T> {

    private val layers = Stack<MutableSet<T>>()

    @Transient
    var frozen = false
        private set
    val active get() = !frozen

    init {
        push()
    }

    val topLayer get() = layers.peek()!!
    val depth get() = layers.size

    private fun checkActive() {
        check(!frozen) { "Trying to operate a frozen SpaceAllocator" }
    }

    fun freeze() {
        checkActive()
        frozen = true
    }

    fun push() {
        checkActive()
        layers.push(mutableSetOf())
    }

    fun drop() {
        checkActive()
        check(layers.size > 1) { "Could not drop the last layer" }
        layers.pop()
    }

    fun squash() {
        checkActive()
        check(layers.size > 1) { "Could not squash the last layer" }
        val elements = layers.pop()!!
        topLayer += elements
    }

    fun allocate(value: T) {
        check(tryAllocate(value)) { "Failed to allocate $value" }
    }

    fun tryAllocate(value: T): Boolean {
        checkActive()
        if (layers.any { layer -> layer.any { conflictMatcher(value, it) } }) {
            return false
        }
        return topLayer.add(value)
    }

    fun release(value: T) {
        checkActive()
        check(topLayer.remove(value)) { error("Element is not allocated on the top layer: $value") }
    }

    override fun iterator() = object : Iterator<T> {
        val layersIter = layers.iterator()
        var elementIter: Iterator<T>? = null

        private fun seekToNext() {
            while (elementIter?.hasNext() != true) {
                if (layersIter.hasNext()) {
                    elementIter = layersIter.next()!!.iterator()
                } else {
                    break
                }
            }
        }

        override fun hasNext(): Boolean {
            seekToNext()
            return elementIter?.hasNext() ?: false
        }

        override fun next(): T {
            seekToNext()
            return elementIter!!.next()
        }
    }

}
