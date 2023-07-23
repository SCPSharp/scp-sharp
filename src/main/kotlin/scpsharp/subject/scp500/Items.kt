/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp500

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import scpsharp.subject.SCPSubjects
import scpsharp.util.addItem
import scpsharp.util.id
import kotlin.math.min
import kotlin.random.Random

object SCP5001Item : Item(
    FabricItemSettings()
        .fireproof()
        .maxCount(16)
        .food(
            FoodComponent.Builder()
                .hunger(15)
                .saturationModifier(0.3f)
                .snack()
                .alwaysEdible()
                .statusEffect(StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 5), 1.0f)
                .build()
        )
) {

    val IDENTIFIER = id("scp500")

    init {
        Registry.register(Registries.ITEM, IDENTIFIER, SCP5001Item)
        SCPSubjects.ITEM_GROUP_KEY.addItem(this)
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient) {
            SCP500.LOGGER.info("$user ate a SCP-500")
            user.activeStatusEffects.values
                .filter { !it.effectType.isBeneficial }
                .shuffled(Random(world.random.nextLong()))
                .run { subList(0, min(world.random.nextInt(5), size)) }
                .filter { user.removeStatusEffect(it.effectType) }
                .onEach { SCP500.LOGGER.info("$it removed successfully by SCP-500 for $user") }
                .apply { SCP500.LOGGER.info("All $size non-beneficial effects removed for $user") }
            if (user is PlayerEntity) {
                user.incrementStat(SCP500.EATING_STAT)
            }
        }
        return super.finishUsing(stack, world, user)
    }

}

object SCP500JarItem : Item(
    FabricItemSettings()
        .maxDamage(5)
) {

    val IDENTIFIER = id("scp500_jar")

    init {
        Registry.register(Registries.ITEM, IDENTIFIER, SCP500JarItem)
        SCPSubjects.ITEM_GROUP_KEY.addItem(this)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        stack.damage(1, user) {}
        if (!world.isClient) {
            user.giveItemStack(ItemStack(SCP5001Item))
            user.incrementStat(SCP500.TAKING_OUT_STAT)
            SCP500.LOGGER.info("$user took out a SCP-500 from jar")
        }
        return TypedActionResult.consume(stack)
    }

}
