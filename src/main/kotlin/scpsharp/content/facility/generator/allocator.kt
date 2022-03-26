/*
 * Copyright (C) 2022  SCPSharp Team
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
package scpsharp.content.facility.generator

import java.util.*

class StackAllocator<T>(val conflictMatcher: (T, T) -> Boolean) {

    private val stack = Stack<MutableSet<T>>()

    @Transient
    var frozen = false
        private set
    val active get() = !frozen

    init {
        pushStack()
    }

    val allAllocatedSpaces get() = stack.flatten()
    val topStack get() = stack.peek().toSet()
    val currentStackDeepness get() = stack.size
    val isOnBaseStack get() = currentStackDeepness == 1

    private fun validateActive() {
        if (frozen) {
            throw IllegalStateException("Trying to operate frozen space allocator")
        }
    }

    fun freeze() {
        validateActive()
        frozen = true
    }

    fun pushStack(): StackAreaAllocation {
        validateActive()
        stack.push(mutableSetOf())
        return StackAreaAllocation(this, stack.size)
    }

    fun popStack() {
        validateActive()
        stack.pop()
        if (stack.isEmpty()) {
            throw IllegalStateException("Base stack popped")
        }
    }

    fun freeStack() {
        validateActive()
        stack.pop().apply {
            stack.peek().addAll(this)
        }
    }

    fun allocate(value: T) =
        tryAllocate(value) ?: throw IllegalStateException("Target box is colliding with other allocated box")

    fun tryAllocate(value: T): StackElementAllocation<T>? {
        validateActive()
        if (allAllocatedSpaces.any { conflictMatcher(value, it) }) {
            return null
        }
        if (!stack.peek().add(value)) {
            throw IllegalStateException("Target box has been allocated")
        }
        return StackElementAllocation(this, value)
    }

    fun free(value: T) {
        validateActive()
        if (!stack.peek().remove(value)) {
            if (allAllocatedSpaces.contains(value)) {
                throw IllegalStateException("Target box is not on the top stack, $value")
            } else {
                throw IllegalStateException("Target box is not allocated")
            }
        }
    }

    fun validate(value: Array<T>, validator: () -> Boolean = { true }): Boolean {
        val allocations = value.map(this::tryAllocate)
        return if (!allocations.any(Objects::isNull) && validator()) {
            true
        } else {
            allocations.filterNotNull()
                .forEach(StackElementAllocation<T>::close)
            false
        }
    }

}

data class StackAreaAllocation(val allocator: StackAllocator<*>, val position: Int) : AutoCloseable {

    override fun close() {
        allocator.popStack()
        validateFreed()
    }

    private fun validateFreed() {
        if (allocator.currentStackDeepness != position - 1) {
            throw IllegalStateException("The upper stacks are not popped, expect ${position - 1} but got ${allocator.currentStackDeepness}")
        }
    }

    fun free() {
        allocator.freeStack()
        validateFreed()
    }

    fun validate(validator: () -> Boolean) = if (validator()) {
        free()
        true
    } else {
        close()
        false
    }

}

data class StackElementAllocation<T>(val allocator: StackAllocator<T>, val value: T) : AutoCloseable {

    override fun close() {
        allocator.free(value)
    }

    fun validate(validator: () -> Boolean) = if (validator()) {
        true
    } else {
        close()
        false
    }

}