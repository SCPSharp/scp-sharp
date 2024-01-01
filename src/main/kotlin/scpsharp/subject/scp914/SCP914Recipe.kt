/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp914

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.World
import scpsharp.util.id

data class SCP914Recipe(
    val source: Ingredient,
    val target: Ingredient,
    val output: ItemStack,
    val reverseOutput: ItemStack
) : Recipe<Inventory> {

    companion object {

        val TYPE: RecipeType<SCP914Recipe> = RecipeType.register<SCP914Recipe>("scpsharp:scp914_processing")

        val SERIALIZER: Serializer =
            Registry.register(Registries.RECIPE_SERIALIZER, id("scp914_processing"), Serializer())

        val SIMPLE_SERIALIZER =
            Registry.register(Registries.RECIPE_SERIALIZER, id("scp914_processing_simple"), SimpleSerializer())

    }

    private val matcher = source.or(target)

    override fun matches(inventory: Inventory, world: World) = matcher.test(inventory.getStack(0))

    override fun craft(inventory: Inventory, registryManager: DynamicRegistryManager): ItemStack {
        val input = inventory.getStack(0)
        return if (source.test(input)) {
            output
        } else {
            reverseOutput
        }.copy()
            .apply {
                count *= input.count
            }
    }

    override fun fits(width: Int, height: Int) = (width * height) == 1

    override fun getSerializer(): Serializer = SERIALIZER

    override fun getType(): RecipeType<SCP914Recipe> = TYPE

    override fun getResult(registryManager: DynamicRegistryManager?) = output

    open class Serializer : RecipeSerializer<SCP914Recipe> {

        companion object {
            val CODEC: Codec<SCP914Recipe> =
                RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SCP914Recipe> ->
                    instance.group(
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("source").forGetter { it.source },
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("target").forGetter { it.target },
                        ItemStack.RECIPE_RESULT_CODEC.fieldOf("output").forGetter { it.output },
                        ItemStack.RECIPE_RESULT_CODEC.fieldOf("reverse_output").forGetter { it.reverseOutput },
                    ).apply(instance) { source, target, output, reverseOutput ->
                        SCP914Recipe(
                            source, target, output, reverseOutput
                        )
                    }
                }
        }

        override fun read(buffer: PacketByteBuf) = SCP914Recipe(
            source = Ingredient.fromPacket(buffer),
            target = Ingredient.fromPacket(buffer),
            output = buffer.readItemStack(),
            reverseOutput = buffer.readItemStack()
        )

        override fun write(buffer: PacketByteBuf, recipe: SCP914Recipe) {
            recipe.source.write(buffer)
            recipe.target.write(buffer)
            buffer.writeItemStack(recipe.output)
            buffer.writeItemStack(recipe.reverseOutput)
        }

        override fun codec() = CODEC

    }

    open class SimpleSerializer : Serializer() {

        companion object {
            val CODEC: Codec<SCP914Recipe> =
                RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SCP914Recipe> ->
                    instance.group(
                        Registries.ITEM.codec.fieldOf("source").forGetter { it.reverseOutput.item },
                        Registries.ITEM.codec.fieldOf("target").forGetter { it.output.item },
                    ).apply(instance) { source, target ->
                        SCP914Recipe(
                            Ingredient.ofItems(source),
                            Ingredient.ofItems(target),
                            ItemStack(target),
                            ItemStack(source)
                        )
                    }
                }
        }

        override fun codec() = CODEC

    }

}
