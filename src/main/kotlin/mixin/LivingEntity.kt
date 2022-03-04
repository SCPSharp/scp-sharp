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

import com.xtex.scpsharp.content.scp714.SCP714Item
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LivingEntity::class)
class LivingEntityMixin {

    @Inject(
        method = ["wakeUp()V"],
        at = [At("HEAD")],
        cancellable = true
    )
    fun wakeUp(info: CallbackInfo) {
        this as LivingEntity
        if (itemsHand.any { it.item is SCP714Item } && attacker == null) {
            if ((this as LivingEntity) is PlayerEntity
                && ((this as LivingEntity) !is ServerPlayerEntity
                        || (this as ServerPlayerEntity).networkHandler != null)
            ) {
                (this as PlayerEntity).sendMessage(TranslatableText("scpsharp.scp714.wake_up_suppressed"), true)
            }
            info.cancel()
        }
    }

}