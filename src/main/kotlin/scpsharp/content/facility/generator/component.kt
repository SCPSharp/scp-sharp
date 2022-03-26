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

import com.mojang.serialization.Lifecycle
import net.minecraft.tag.TagKey
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.*
import scpsharp.util.id
import java.util.function.Predicate
import java.util.stream.Stream

abstract class Component {

    abstract fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

    abstract fun generate(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

}

abstract class SimpleComponent : Component() {

    abstract val boxes: Array<BlockBox>

    abstract val refs: Array<ComponentRef<*>>

    override fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction) =
        generator.allocator.pushStack().validate {
            generator.allocator.validate(boxes) {
                generator.validate(boxes, refs)
            }
        }

    override fun generate(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean =
        refs.filter { it.component != null }
            .map(ComponentRef<*>::generate)
            .all { it }
                && place(generator, pos, direction)

    abstract fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

}

abstract class ComponentFactory<T : Component> {

    companion object {

        val registryId = id("worldgen/facility_component_factory")
        val registryKey = RegistryKey.ofRegistry<ComponentFactory<*>>(registryId)
        val registry = SimpleRegistry(registryKey, Lifecycle.stable(), ComponentFactory<*>::registryEntry)

        init {
            @Suppress("UNCHECKED_CAST")
            Registry.ROOT.add(registryKey as RegistryKey<MutableRegistry<*>>, registry, registry.lifecycle)
        }

    }

    @Suppress("LeakingThis")
    val registryEntry: RegistryEntry.Reference<ComponentFactory<*>> = registry.createEntry(this)

    fun isIn(tag: TagKey<ComponentFactory<*>>) = registryEntry.isIn(tag)

    fun matches(predicate: Predicate<RegistryKey<ComponentFactory<*>>>) = registryEntry.matches(predicate)

    fun streamTags(): Stream<TagKey<ComponentFactory<*>>> = registryEntry.streamTags()

    abstract fun construct(generator: FacilityGenerator, pos: BlockPos, direction: Direction): T

    fun create(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        maxTries: Int = 5
    ): T? {
        for (i in 0 until maxTries) {
            val component = construct(generator, pos, direction)
            if (component.validate(generator, pos, direction)) {
                return component
            }
        }
        return null
    }

    fun generate(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        maxTries: Int = 5,
        freezeAllocator: Boolean = false
    ): Boolean {
        val component = create(generator, pos, direction, maxTries)
        if (freezeAllocator) {
            generator.allocator.freeze()
            if (!generator.allocator.isOnBaseStack) {
                throw IllegalStateException("Space allocator frozen but not on the base stack")
            }
            // @TODO: Log here
        }
        if (component != null && component.generate(generator, pos, direction)) {
            return true
        }
        return false
    }

    fun startRandomGenerate(generator: FacilityGenerator) =
        generate(generator, generator.origin, Direction.random(generator.random), freezeAllocator = true)

}

data class ComponentReference<T : Component>(
    val generator: FacilityGenerator,
    val factory: ComponentFactory<T>,
    val pos: BlockPos,
    val direction: Direction
) {

    val component: T? = factory.create(generator, pos, direction)

    fun generate() = component!!.generate(generator, pos, direction)

}

typealias ComponentRef<T> = ComponentReference<T>
