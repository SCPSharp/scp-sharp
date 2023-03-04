/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp008

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
        Registry.register(Registries.ITEM,
            scpsharp.subject.scp008.AntiSCP008Suit.HELMET_ID,
            scpsharp.subject.scp008.AntiSCP008Suit.HELMET
        )
        scpsharp.subject.SCPSubjects.ITEM_GROUP.addItem(scpsharp.subject.scp008.AntiSCP008Suit.HELMET)
        Registry.register(Registries.ITEM,
            scpsharp.subject.scp008.AntiSCP008Suit.CHEST_ID,
            scpsharp.subject.scp008.AntiSCP008Suit.CHEST
        )
        scpsharp.subject.SCPSubjects.ITEM_GROUP.addItem(scpsharp.subject.scp008.AntiSCP008Suit.CHEST)
        Registry.register(Registries.ITEM,
            scpsharp.subject.scp008.AntiSCP008Suit.LEGS_ID,
            scpsharp.subject.scp008.AntiSCP008Suit.LEGS
        )
        scpsharp.subject.SCPSubjects.ITEM_GROUP.addItem(scpsharp.subject.scp008.AntiSCP008Suit.LEGS)
        Registry.register(Registries.ITEM,
            scpsharp.subject.scp008.AntiSCP008Suit.FEET_ID,
            scpsharp.subject.scp008.AntiSCP008Suit.FEET
        )
        scpsharp.subject.SCPSubjects.ITEM_GROUP.addItem(scpsharp.subject.scp008.AntiSCP008Suit.FEET)
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
        return scpsharp.subject.scp008.AntiSCP008Suit.HELMET in items && scpsharp.subject.scp008.AntiSCP008Suit.CHEST in items && scpsharp.subject.scp008.AntiSCP008Suit.LEGS in items && scpsharp.subject.scp008.AntiSCP008Suit.FEET in items
    }

}