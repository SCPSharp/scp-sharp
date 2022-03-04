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
package com.xtex.scpsharp.integrate.rei

import com.xtex.scpsharp.content.scp914.SCP914ControllerBlock
import com.xtex.scpsharp.content.scp914.SCP914Recipe
import com.xtex.scpsharp.util.id
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.util.*

object SCP914REICategory : DisplayCategory<SCP914REIDisplay> {

    val identifier: CategoryIdentifier<SCP914REIDisplay> = CategoryIdentifier.of(id("scp-914"))

    override fun getIcon(): Renderer = EntryStacks.of(SCP914ControllerBlock.item)

    override fun getTitle() = TranslatableText("category.rei.scp914")

    override fun getCategoryIdentifier() = identifier

    override fun setupDisplay(display: SCP914REIDisplay, bounds: Rectangle): List<Widget> {
        return listOf<Widget>(
            Widgets.createRecipeBase(bounds),
            Widgets.createSlot(Point(bounds.centerX - 26 - 9, bounds.centerY - 9))
                .markInput()
                .entries(display.inputEntries[0]),
            Widgets.createArrow(Point(bounds.centerX - 9, bounds.centerY - 9)),
            Widgets.createResultSlotBackground(Point(bounds.centerX + 26, bounds.centerY - 9)),
            Widgets.createSlot(Point(bounds.centerX + 26, bounds.centerY - 9))
                .markOutput()
                .disableBackground()
                .entries(display.outputEntries[0]),
        )
    }

    override fun getDisplayHeight() = 42

}

class SCP914REIDisplay(inputs: List<EntryIngredient>, outputs: List<EntryIngredient>, location: Optional<Identifier>) :
    BasicDisplay(inputs, outputs, location) {

    constructor(recipe: SCP914Recipe) : this(
        listOf(EntryIngredients.ofIngredient(recipe.source)), listOf(EntryIngredients.of(recipe.output)),
        Optional.of(recipe.id)
    )

    override fun getCategoryIdentifier() = SCP914REICategory.identifier

}
