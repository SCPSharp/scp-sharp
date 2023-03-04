/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp914

import com.google.gson.JsonObject
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.World
import scpsharp.util.id

data class SCP914Recipe(
    private val id: Identifier,
    val source: Ingredient,
    val target: Ingredient,
    private val output: ItemStack,
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

    override fun getId() = id

    override fun getOutput(): ItemStack = output

    override fun matches(inventory: Inventory, world: World) = matcher.test(inventory.getStack(0))

    override fun craft(inventory: Inventory): ItemStack {
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

    open class Serializer : RecipeSerializer<SCP914Recipe> {

        override fun read(id: Identifier, json: JsonObject) = SCP914Recipe(
            id = id,
            source = Ingredient.fromJson(json["source"]),
            target = Ingredient.fromJson(json["target"]),
            output = ShapedRecipe.outputFromJson(json.getAsJsonObject("output")),
            reverseOutput = ShapedRecipe.outputFromJson(json.getAsJsonObject("reverse_output")),
        )

        override fun read(id: Identifier, buffer: PacketByteBuf) = SCP914Recipe(
            id = id,
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

    }

    open class SimpleSerializer : Serializer() {

        override fun read(id: Identifier, json: JsonObject): SCP914Recipe {
            val sourceItem = Registries.ITEM.get(Identifier(json["source"].asString))
            val targetItem = Registries.ITEM.get(Identifier(json["target"].asString))
            return SCP914Recipe(
                id = id,
                source = Ingredient.ofItems(sourceItem),
                target = Ingredient.ofItems(targetItem),
                output = ItemStack(targetItem),
                reverseOutput = ItemStack(sourceItem)
            )
        }

    }

}
