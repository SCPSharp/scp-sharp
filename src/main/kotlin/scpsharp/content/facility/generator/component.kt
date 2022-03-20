package scpsharp.content.facility.generator

import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

interface Component {

    fun validate(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

    fun generate(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

}

interface SimpleComponent : Component {

    val boxes: Array<BlockBox>

    val refs: Array<ComponentRef<*>>

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

    fun place(generator: FacilityGenerator, pos: BlockPos, direction: Direction): Boolean

}

interface ComponentFactory<T : Component> {

    fun create(generator: FacilityGenerator, pos: BlockPos, direction: Direction): T

    fun construct(
        generator: FacilityGenerator,
        pos: BlockPos,
        direction: Direction,
        maxTries: Int = 5
    ): T? {
        for (i in 0 until maxTries) {
            val component = create(generator, pos, direction)
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
        val component = construct(generator, pos, direction, maxTries)
        if(freezeAllocator) {
            generator.allocator.freeze()
            if(!generator.allocator.isOnBaseStack) {
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

    val component: T? = factory.construct(generator, pos, direction)

    fun generate() = component!!.generate(generator, pos, direction)

}

typealias ComponentRef<T> = ComponentReference<T>
