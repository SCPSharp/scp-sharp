/*
 * Copyright (C) 2022  SCPSharp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package scpsharp.content.subject.scp500

import scpsharp.content.subject.SCPSubjects
import scpsharp.util.id
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import kotlin.math.min

object SCP5001Item : Item(
    FabricItemSettings()
        .group(SCPSubjects.itemGroup)
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

    val identifier = id("scp500")

    init {
        Registry.register(Registry.ITEM, identifier, SCP5001Item)
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient) {
            SCP500.logger.info("$user ate a SCP-500")
            user.activeStatusEffects.values
                .filter { !it.effectType.isBeneficial }
                .shuffled(world.random)
                .run { subList(0, min(world.random.nextInt(5), size)) }
                .filter { user.removeStatusEffect(it.effectType) }
                .onEach { SCP500.logger.info("$it removed successfully by SCP-500 for $user") }
                .apply { SCP500.logger.info("All $size non-beneficial effects removed for $user") }
            if (user is PlayerEntity) {
                user.incrementStat(SCP500.eatingStat)
            }
        }
        return super.finishUsing(stack, world, user)
    }

}

object SCP500JarItem : Item(
    FabricItemSettings()
        .group(SCPSubjects.itemGroup)
        .maxDamage(5)
) {

    val identifier = id("scp500_jar")

    init {
        Registry.register(Registry.ITEM, identifier, SCP500JarItem)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        stack.damage(1, user) {}
        if (!world.isClient) {
            user.giveItemStack(ItemStack(SCP5001Item))
            user.incrementStat(SCP500.takingOutStat)
            SCP500.logger.info("$user took out a SCP-500 from jar")
        }
        return TypedActionResult.consume(stack)
    }

}
