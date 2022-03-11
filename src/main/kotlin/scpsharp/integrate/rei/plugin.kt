/*
 * Copyright (C) 2022  SCPSharp
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
        registry.removePlusButton(SCP914REICategory.identifier)
        registry.addWorkstations(SCP914REICategory.identifier, EntryIngredients.of(SCP914ControllerBlock.item))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        registry.registerRecipeFiller(SCP914Recipe::class.java, SCP914Recipe.type, ::SCP914REIDisplay)
    }

}
