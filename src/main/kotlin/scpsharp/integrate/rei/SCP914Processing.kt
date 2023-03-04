/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.integrate.rei

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
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent
import net.minecraft.util.Identifier
import scpsharp.subject.scp914.SCP914ControllerBlock
import scpsharp.subject.scp914.SCP914Recipe
import scpsharp.util.id
import java.util.*

object SCP914REICategory : DisplayCategory<SCP914REIDisplay> {

    val identifier: CategoryIdentifier<SCP914REIDisplay> = CategoryIdentifier.of(id("scp-914"))

    override fun getIcon(): Renderer = EntryStacks.of(SCP914ControllerBlock.ITEM)

    override fun getTitle(): Text = Text.translatable("category.rei.scp914")

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
