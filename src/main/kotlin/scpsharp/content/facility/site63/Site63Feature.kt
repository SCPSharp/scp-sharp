/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.facility.site63

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.util.FeatureContext
import scpsharp.content.facility.generator.ComponentTags
import scpsharp.content.facility.generator.FacilityGenerator
import scpsharp.util.id

object Site63Feature : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {

    val IDENTIFIER = id("site63")

    val DEFAULT_CONFIGURATION_KEY: RegistryKey<ConfiguredFeature<*, *>> = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, IDENTIFIER)
    val DEFAULT_PLACE_KEY: RegistryKey<PlacedFeature> = RegistryKey.of(RegistryKeys.PLACED_FEATURE, IDENTIFIER)

    init {
        Registry.register(Registries.FEATURE, IDENTIFIER, this)
        BiomeModifications.create(IDENTIFIER)
            .add(ModificationPhase.ADDITIONS, { context ->
                context.canGenerateIn(DimensionOptions.OVERWORLD)
                        && !context.hasTag(BiomeTags.IS_RIVER)
                        && !context.hasTag(BiomeTags.IS_OCEAN)
                        && !context.hasTag(BiomeTags.IS_DEEP_OCEAN)
                        && context.biomeKey.value.namespace == "minecraft"
            }) { context ->
                context.generationSettings.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, DEFAULT_PLACE_KEY)
            }
    }

    override fun generate(context: FeatureContext<DefaultFeatureConfig>) =
        FacilityGenerator(context).tryRandomGenerate(ComponentTags.SITE63_START) {
            // @TODO: Fix this
            true
            //ExtraValidators.maxDepth(this, 2)
        }

}