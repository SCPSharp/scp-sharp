/*
 * Copyright (C) 2022  xtexChooser
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
package com.xtex.scpsharp.content.scp914

import com.google.gson.JsonObject
import com.xtex.scpsharp.util.id
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.*
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

data class SCP914Recipe(
    private val id: Identifier,
    val source: Ingredient,
    val target: Ingredient,
    private val output: ItemStack,
    val reverseOutput: ItemStack
) : Recipe<Inventory> {

    companion object {

        val type = RecipeType.register<SCP914Recipe>("scpsharp:scp914_processing")

        val serializer = Registry.register(Registry.RECIPE_SERIALIZER, id("scp914_processing"), Serializer())

        val simpleSerializer =
            Registry.register(Registry.RECIPE_SERIALIZER, id("scp914_processing_simple"), SimpleSerializer())

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

    override fun getSerializer() = SCP914Recipe.serializer

    override fun getType(): RecipeType<SCP914Recipe> = SCP914Recipe.type

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
            val sourceItem = Registry.ITEM.get(Identifier(json["source"].asString))
            val targetItem = Registry.ITEM.get(Identifier(json["target"].asString))
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
