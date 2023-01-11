/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.integrate.rei

import scpsharp.content.subject.scp914.SCP914ControllerBlock
import scpsharp.content.subject.scp914.SCP914Recipe
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.plugins.REIServerPlugin
import me.shedaniel.rei.api.common.util.EntryIngredients

object REIServerPlugin : REIServerPlugin {

    override fun registerDisplaySerializer(registry: DisplaySerializerRegistry) {
        registry.register(SCP914REICategory.identifier, BasicDisplay.Serializer.ofSimple(::SCP914REIDisplay))
    }

}

object REIClientPlugin : REIClientPlugin {

    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(SCP914REICategory)
        registry.addWorkstations(SCP914REICategory.identifier, EntryIngredients.of(SCP914ControllerBlock.ITEM))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        registry.registerRecipeFiller(SCP914Recipe::class.java, SCP914Recipe.TYPE, ::SCP914REIDisplay)
    }

}
