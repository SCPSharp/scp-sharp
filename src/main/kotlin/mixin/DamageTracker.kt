/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.mixin

import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008ContainmentBlockEntity
import com.xtex.scpsharp.content.scp008.SCP008StatusEffect
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTracker
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.state.property.Properties
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(DamageTracker::class)
abstract class DamageTrackerMixin {

    @get:Shadow
    @get:Final
    abstract val entity: LivingEntity

    @Inject(
        method = ["Lnet/minecraft/entity/damage/DamageTracker;onDamage(Lnet/minecraft/entity/damage/DamageSource;FF)V"],
        at = [At("RETURN")]
    )
    fun onDamage(damageSource: DamageSource, originalHealth: Float, damage: Float, info: CallbackInfo) {
        // Infect SCP-008 from containment block
        linkedSetOf(
            entity.world.getWorldChunk(entity.blockPos.add(6, 0, -6)),
            entity.world.getWorldChunk(entity.blockPos.add(6, 0, 6)),
            entity.world.getWorldChunk(entity.blockPos.add(-6, 0, -6)),
            entity.world.getWorldChunk(entity.blockPos.add(-6, 0, 6))
        )
            .flatMap { it.blockEntities.values }
            .filterIsInstance(SCP008ContainmentBlockEntity::class.java)
            .filter { it.pos.getSquaredDistance(entity.blockPos) <= 6 * 6 }
            .filter { entity.world.getBlockState(it.pos)[Properties.OPEN] }
            .forEach {
                SCP008.logger.info("$entity got $damageSource x$damage and getting infected from a open containment box at ${it.pos}")
                SCP008StatusEffect.infect(entity, damageSource.source)
            }
    }

}
