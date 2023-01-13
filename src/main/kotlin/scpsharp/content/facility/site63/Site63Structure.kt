/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.site63

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.structure.StructureType
import scpsharp.content.facility.FacilityStructure
import scpsharp.util.id
import scpsharp.util.logger
import java.util.*

class Site63Structure(config: Config) : FacilityStructure(config) {

    companion object {

        val IDENTIFIER = id("site63")
        val LOGGER = logger("Site-63")

        val CODEC: Codec<Site63Structure> =
            RecordCodecBuilder.create { instance ->
                instance.group(configCodecBuilder(instance)).apply(instance) { Site63Structure(it) }
            }

        val STRUCTURE_TYPE: StructureType<Site63Structure> =
            Registry.register(Registries.STRUCTURE_TYPE, IDENTIFIER, object : StructureType<Site63Structure> {
                override fun codec() = CODEC
            })

    }

    override fun getStructurePosition(context: Context): Optional<StructurePosition> =
        getStructurePosition(context) { pos, direction ->
            random(Site63Tags.START, pos, direction)
        }

    override fun getType(): StructureType<Site63Structure> = STRUCTURE_TYPE

}