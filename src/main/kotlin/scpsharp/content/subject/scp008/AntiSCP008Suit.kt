/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 * ./grad
 */
package scpsharp.content.subject.scp008

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import scpsharp.content.subject.SCPSubjects
import scpsharp.util.addItem
import scpsharp.util.id


object AntiSCP008Suit : ArmorMaterial {

    val HELMET = ArmorItem(this, EquipmentSlot.HEAD, FabricItemSettings())
    val HELMET_ID = id("anti_scp008_suit_helmet")
    val CHEST = ArmorItem(this, EquipmentSlot.CHEST, FabricItemSettings())
    val CHEST_ID = id("anti_scp008_suit_chest")
    val LEGS = ArmorItem(this, EquipmentSlot.LEGS, FabricItemSettings())
    val LEGS_ID = id("anti_scp008_suit_legs")
    val FEET = ArmorItem(this, EquipmentSlot.FEET, FabricItemSettings())
    val FEET_ID = id("anti_scp008_suit_feet")

    init {
        Registry.register(Registries.ITEM, HELMET_ID, HELMET)
        SCPSubjects.ITEM_GROUP.addItem(HELMET)
        Registry.register(Registries.ITEM, CHEST_ID, CHEST)
        SCPSubjects.ITEM_GROUP.addItem(CHEST)
        Registry.register(Registries.ITEM, LEGS_ID, LEGS)
        SCPSubjects.ITEM_GROUP.addItem(LEGS)
        Registry.register(Registries.ITEM, FEET_ID, FEET)
        SCPSubjects.ITEM_GROUP.addItem(FEET)
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