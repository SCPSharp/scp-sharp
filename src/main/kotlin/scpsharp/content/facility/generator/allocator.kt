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

import net.minecraft.util.math.BlockBox
import java.util.*

class SpaceAllocator {

    private val stack = Stack<MutableSet<BlockBox>>()

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

    fun pushStack(): SpaceStackAllocation {
        validateActive()
        stack.push(mutableSetOf())
        return SpaceStackAllocation(this, stack.size)
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

    fun allocate(box: BlockBox) =
        tryAllocate(box) ?: throw IllegalStateException("Target box is colliding with other allocated box")

    fun tryAllocate(box: BlockBox): SpaceAllocation? {
        validateActive()
        if (allAllocatedSpaces.any(box::intersects)) {
            return null
        }
        if (!stack.peek().add(box)) {
            throw IllegalStateException("Target box has been allocated")
        }
        return SpaceAllocation(this, box)
    }

    fun free(box: BlockBox) {
        validateActive()
        if (!stack.peek().remove(box)) {
            if (allAllocatedSpaces.contains(box)) {
                throw IllegalStateException("Target box is not on the top stack, $box")
            } else {
                throw IllegalStateException("Target box is not allocated")
            }
        }
    }

    fun validate(boxes: Array<BlockBox>, validator: () -> Boolean = { true }): Boolean {
        val allocations = boxes.map(this::tryAllocate)
        return if (!allocations.any(Objects::isNull) && validator()) {
            true
        } else {
            allocations.filterNotNull()
                .forEach(SpaceAllocation::close)
            false
        }
    }

}

data class SpaceStackAllocation(val allocator: SpaceAllocator, val position: Int) : AutoCloseable {

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

data class SpaceAllocation(val allocator: SpaceAllocator, val box: BlockBox) : AutoCloseable {

    override fun close() {
        allocator.free(box)
    }

    fun validate(validator: () -> Boolean) = if (validator()) {
        true
    } else {
        close()
        false
    }

}