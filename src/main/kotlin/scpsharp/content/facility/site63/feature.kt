/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
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
import scpsharp.content.facility.generator.ComponentTags
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.util.id
import sun.misc.Unsafe

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
                RarityFilterPlacementModifier.of(512),
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
        FacilityGenerator(context).tryRandomGenerate(ComponentTags.site63Start)

}