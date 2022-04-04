/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
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

    open val exposedInAir: Boolean = false

    override fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction) =
        generator.spaceAllocator.pushStack().validate {
            generator.componentAllocator.pushStack().validate {
                generator.componentAllocator.allocate(this).validate {
                    generator.spaceAllocator.validate(boxes) {
                        generator.validate(boxes, refs, exposedInAir)
                    }
                }
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

        val registryId = id("worldgen/scpsharp_facility_component")
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
            generator.spaceAllocator.freeze()
            if (!generator.spaceAllocator.isOnBaseStack) {
                throw IllegalStateException("Space allocator frozen but not on the base stack")
            }
            generator.componentAllocator.freeze()
            if (!generator.componentAllocator.isOnBaseStack) {
                throw IllegalStateException("Component allocator frozen but not on the base stack")
            }
            // @TODO: Log here
        }
        if (component != null && component.generate(generator, pos, direction)) {
            return true
        }
        return false
    }

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
