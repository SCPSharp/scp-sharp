package scpsharp.content.facility

import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import scpsharp.util.id

object FacilityTags {

    val FACILITY_KEEP: TagKey<Block> = TagKey.of(Registries.BLOCK.key, id("facility_keep"))

    val START = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/start"))!!
    val PART = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/part"))!!
    val CONTAINMENT_ROOM = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/containment_room"))!!
    val CORRIDOR = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/corridor"))!!
    val CROSSING = TagKey.of(RegistryKeys.STRUCTURE_PIECE, id("facility/crossing"))!!

}