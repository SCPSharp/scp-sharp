/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp008

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.Entity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import scpsharp.subject.SCPSubjects
import scpsharp.util.addItem
import scpsharp.util.id


object AntiSCP008Suit : ArmorMaterial {

    val HELMET = ArmorItem(this, ArmorItem.Type.HELMET, FabricItemSettings())
    val HELMET_ID = id("anti_scp008_suit_helmet")
    val CHEST = ArmorItem(this, ArmorItem.Type.CHESTPLATE, FabricItemSettings())
    val CHEST_ID = id("anti_scp008_suit_chest")
    val LEGS = ArmorItem(this, ArmorItem.Type.LEGGINGS, FabricItemSettings())
    val LEGS_ID = id("anti_scp008_suit_legs")
    val FEET = ArmorItem(this, ArmorItem.Type.BOOTS, FabricItemSettings())
    val FEET_ID = id("anti_scp008_suit_feet")

    init {
        Registry.register(Registries.ITEM, HELMET_ID, HELMET)
        SCPSubjects.ITEM_GROUP_KEY.addItem(HELMET)
        Registry.register(Registries.ITEM, CHEST_ID, CHEST)
        SCPSubjects.ITEM_GROUP_KEY.addItem(CHEST)
        Registry.register(Registries.ITEM, LEGS_ID, LEGS)
        SCPSubjects.ITEM_GROUP_KEY.addItem(LEGS)
        Registry.register(Registries.ITEM, FEET_ID, FEET)
        SCPSubjects.ITEM_GROUP_KEY.addItem(FEET)
    }

    override fun getDurability(type: ArmorItem.Type) = 195

    override fun getProtection(type: ArmorItem.Type) = 1

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