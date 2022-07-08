/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.tag.BiomeTags
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier
import scpsharp.content.facility.generator.ComponentTags
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.util.id

object Site63Feature : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {

    val IDENTIFIER = id("site63")

    val DEFAULT_CONFIGURATION = ConfiguredFeature(this, FeatureConfig.DEFAULT)

    val DEFAULT_PLACE: PlacedFeature

    init {
        Registry.register(Registry.FEATURE, IDENTIFIER, this)
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, IDENTIFIER, DEFAULT_CONFIGURATION)
        DEFAULT_PLACE = PlacedFeature(
            BuiltinRegistries.CONFIGURED_FEATURE.getOrCreateEntry(
                BuiltinRegistries.CONFIGURED_FEATURE.getKey(
                    DEFAULT_CONFIGURATION
                ).orElseThrow()
            ),
            arrayListOf(
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(512),
                SquarePlacementModifier()
            )
        )
        Registry.register(BuiltinRegistries.PLACED_FEATURE, IDENTIFIER, DEFAULT_PLACE)
        BiomeModifications.create(IDENTIFIER)
            .add(ModificationPhase.ADDITIONS, { context ->
                context.canGenerateIn(DimensionOptions.OVERWORLD)
                        && !context.hasTag(BiomeTags.IS_RIVER)
                        && !context.hasTag(BiomeTags.IS_OCEAN)
                        && !context.hasTag(BiomeTags.IS_DEEP_OCEAN)
                        && context.biomeKey.value.namespace == "minecraft"
                        && BuiltinRegistries.BIOME.containsId(context.biomeKey.value)
            }) { context ->
                context.generationSettings.addFeature(
                    GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                    BuiltinRegistries.PLACED_FEATURE.getKey(DEFAULT_PLACE).orElseThrow()
                )
            }
    }

    override fun generate(context: FeatureContext<DefaultFeatureConfig>) =
        FacilityGenerator(context).tryRandomGenerate(ComponentTags.SITE63_START) {
            // @TODO: Fix this
            true
            //ExtraValidators.maxDepth(this, 2)
        }

}