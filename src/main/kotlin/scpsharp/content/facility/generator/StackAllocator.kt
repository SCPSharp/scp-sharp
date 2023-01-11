/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.facility.generator

import java.util.*

class StackAllocator<T>(val conflictMatcher: (T, T) -> Boolean) {

    private val stack = Stack<MutableSet<T>>()

    val allAllocatedSpaces = mutableSetOf<T>()

    @Transient
    var frozen = false
        private set
    val active get() = !frozen

    init {
        pushStack()
    }

    val topStack get() = stack.peek().toSet()
    val currentStackDepth get() = stack.size
    val isOnBaseStack get() = currentStackDepth == 1

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
            throw IllegalStateException("Target box has been allocated in current stack")
        }
        if (!allAllocatedSpaces.add(value)) {
            throw IllegalStateException("Target box has been allocated in other stack")
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
        allAllocatedSpaces.remove(value)
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
        if (allocator.currentStackDepth != position - 1) {
            throw IllegalStateException("The upper stacks are not popped, expect ${position - 1} but got ${allocator.currentStackDepth}")
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