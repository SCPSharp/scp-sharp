/*
 * Copyright (C) 2022  xtexChooser
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
@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.xtex.scpsharp.mixin

import com.xtex.scpsharp.content.subject.scp008.SCP008
import com.xtex.scpsharp.content.subject.scp008.SCP008StatusEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerEntity::class)
class PlayerEntityMixin {

    @Inject(
        method = ["collideWithEntity(Lnet/minecraft/entity/Entity;)V"],
        at = [At("TAIL")]
    )
    fun collideWithEntity(entity: Entity, info: CallbackInfo) {
        this as PlayerEntity
        // Infect SCP-008 each other
        if (entity is LivingEntity) {
            val targetInfected = entity.hasStatusEffect(SCP008StatusEffect)
            val selfInfected = this.hasStatusEffect(SCP008StatusEffect)
            if (targetInfected && !selfInfected && this.health <= this.maxHealth * 0.8f) {
                SCP008.logger.info("$this infected SCP-008 from $entity by colliding")
                SCP008StatusEffect.infect(this, entity)
            } else if (selfInfected && !targetInfected && entity.health <= entity.maxHealth * 0.8f) {
                SCP008.logger.info("$this infected SCP-008 to $entity by colliding")
                SCP008StatusEffect.infect(entity, this)
            }
        }
    }

}
