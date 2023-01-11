/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import com.mojang.serialization.Lifecycle
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import scpsharp.util.id
import java.util.function.Predicate
import java.util.stream.Stream

abstract class Component {

    abstract val type: ComponentFactory<*>

    abstract fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean

    abstract fun generate(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean

    fun isIn(tag: TagKey<ComponentFactory<*>>) = type.isIn(tag)

    fun matches(predicate: Predicate<RegistryKey<ComponentFactory<*>>>) = type.matches(predicate)

    fun streamTags(): Stream<TagKey<ComponentFactory<*>>> = type.streamTags()

}

abstract class SimpleComponent : Component() {

    abstract val boxes: Array<BlockBox>

    abstract val refs: Array<ComponentRef<*>>

    override fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int) =
        generator.spaceAllocator.pushStack().validate {
            generator.componentAllocator.pushStack().validate {
                generator.componentAllocator.allocate(this).validate {
                    generator.spaceAllocator.validate(boxes) {
                        generator.validate(boxes, refs)
                    }
                }
            }
        }

    override fun generate(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean =
        refs.filter { it.component != null }
            .map(ComponentRef<*>::generate)
            .all { it }
                && place(generator, pos, direction, depth)

    abstract fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): Boolean

}

abstract class ComponentFactory<T : Component> {

    companion object {

        val REGISTRY_ID = id("worldgen/scpsharp_facility_component")
        val REGISTRY_KEY: RegistryKey<Registry<ComponentFactory<*>>> = RegistryKey.ofRegistry(REGISTRY_ID)
        val REGISTRY = SimpleRegistry(REGISTRY_KEY, Lifecycle.stable(), true)

        init {
            @Suppress("UNCHECKED_CAST")
            Registries.ROOT.add(REGISTRY_KEY as RegistryKey<MutableRegistry<*>>, REGISTRY, REGISTRY.lifecycle)
        }

    }

    @Suppress("LeakingThis")
    val registryEntry: RegistryEntry.Reference<ComponentFactory<*>> = REGISTRY.createEntry(this)

    fun isIn(tag: TagKey<ComponentFactory<*>>) = registryEntry.isIn(tag)

    fun matches(predicate: Predicate<RegistryKey<ComponentFactory<*>>>) = registryEntry.matches(predicate)

    fun streamTags(): Stream<TagKey<ComponentFactory<*>>> = registryEntry.streamTags()

    abstract fun construct(generator: FacilityGenerator, pos: BlockPos, direction: Direction, depth: Int): T

    fun create(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int,
        maxTries: Int = 2,
        maxDepth: Int = 16
    ): T? {
        if (depth >= maxDepth) {
            return null
        }
        for (i in 0 until maxTries) {
            val component = construct(generator, pos, direction, depth)
            if (component.validate(generator, pos, direction, depth)) {
                return component
            }
        }
        return null
    }

    fun generate(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        depth: Int,
        maxTries: Int = 2,
        maxDepth: Int = 16,
        freezeAllocator: Boolean = false,
        extraValidator: T.() -> Boolean = { true }
    ): Boolean {
        val component = create(generator, pos, direction, depth, maxTries, maxDepth)
        if (freezeAllocator) {
            generator.spaceAllocator.freeze()
            if (!generator.spaceAllocator.isOnBaseStack) {
                throw IllegalStateException("Space allocator frozen but not on the base stack")
            }
            generator.componentAllocator.freeze()
            if (!generator.componentAllocator.isOnBaseStack) {
                throw IllegalStateException("Component allocator frozen but not on the base stack")
            }
        }
        if (component != null && component.extraValidator() && component.generate(generator, pos, direction, depth)) {
            return true
        }
        return false
    }

}

data class ComponentReference<T : Component>(
    val generator: FacilityGenerator,
    val factory: ComponentFactory<T>,
    val pos: BlockPos,
    val direction: Direction,
    val depth: Int
) {

    val component: T? by lazy { factory.create(generator, pos, direction, depth) }

    fun generate() = component!!.generate(generator, pos, direction, depth)

}

typealias ComponentRef<T> = ComponentReference<T>
