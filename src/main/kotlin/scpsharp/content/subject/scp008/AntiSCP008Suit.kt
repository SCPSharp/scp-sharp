/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.subject.scp008

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.registry.Registry
import scpsharp.content.subject.SCPSubjects
import scpsharp.util.id


object AntiSCP008Suit : ArmorMaterial {

    val HELMET = ArmorItem(this, EquipmentSlot.HEAD, FabricItemSettings().group(SCPSubjects.ITEM_GROUP))
    val HELMET_ID = id("anti_scp008_suit_helmet")
    val CHEST = ArmorItem(this, EquipmentSlot.CHEST, FabricItemSettings().group(SCPSubjects.ITEM_GROUP))
    val CHEST_ID = id("anti_scp008_suit_chest")
    val LEGS = ArmorItem(this, EquipmentSlot.LEGS, FabricItemSettings().group(SCPSubjects.ITEM_GROUP))
    val LEGS_ID = id("anti_scp008_suit_legs")
    val FEET = ArmorItem(this, EquipmentSlot.FEET, FabricItemSettings().group(SCPSubjects.ITEM_GROUP))
    val FEET_ID = id("anti_scp008_suit_feet")

    init {
        Registry.register(Registry.ITEM, HELMET_ID, HELMET)
        Registry.register(Registry.ITEM, CHEST_ID, CHEST)
        Registry.register(Registry.ITEM, LEGS_ID, LEGS)
        Registry.register(Registry.ITEM, FEET_ID, FEET)
    }

    override fun getDurability(slot: EquipmentSlot) = 195

    override fun getProtectionAmount(slot: EquipmentSlot) = 1

    override fun getEnchantability() = 7

    override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER

    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.COPPER_INGOT)

    override fun getName() = "anti_scp008_suit"

    override fun getToughness() = 0f

    override fun getKnockbackResistance() = 0f

    fun isWoreFully(entity: Entity): Boolean {
        val items = entity.armorItems.map { it.item }
        return HELMET in items && CHEST in items && LEGS in items && FEET in items
    }

}