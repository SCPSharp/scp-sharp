/*
 * Copyright (C) 2022  SCPSharp Team
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
package scpsharp.content.facility.site63

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.util.id

object Site63Feature : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {

    val identifier = id("site63")

    val defaultConfiguration = ConfiguredFeature(this, FeatureConfig.DEFAULT)

    val defaultPlace: PlacedFeature

    init {
        Registry.register(Registry.FEATURE, identifier, this)
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, defaultConfiguration)
        defaultPlace = PlacedFeature(
            BuiltinRegistries.CONFIGURED_FEATURE.getOrCreateEntry(
                BuiltinRegistries.CONFIGURED_FEATURE.getKey(
                    defaultConfiguration
                ).orElseThrow()
            ),
            arrayListOf(
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(256),
                SquarePlacementModifier()
            )
        )
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, defaultPlace)
        BiomeModifications.create(identifier)
            .add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld()) { context ->
                context.generationSettings.addFeature(
                    GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                    BuiltinRegistries.PLACED_FEATURE.getKey(defaultPlace).orElseThrow()
                )
            }
    }

    override fun generate(context: FeatureContext<DefaultFeatureConfig>) =
        FacilityGenerator.generate(context, Site63.gateFactory)

}