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

import com.mojang.authlib.GameProfile
import com.mojang.datafixers.util.Either
import com.xtex.scpsharp.content.subject.scp714.SCP714
import com.xtex.scpsharp.content.subject.scp714.SCP714Item
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Unit
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ServerPlayerEntity::class)
abstract class ServerPlayerEntityMixin(world: World?, pos: BlockPos?, yaw: Float, profile: GameProfile?)
    : PlayerEntity(world, pos, yaw, profile){

    @Inject(
        method = ["trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;"],
        at = [At("HEAD")],
        cancellable = true
    )
    fun trySleep(pos: BlockPos, info: CallbackInfoReturnable<Either<SleepFailureReason, Unit>>) {
        if (itemsHand.any { it.item is SCP714Item }) {
            incrementStat(SCP714.sleepWithStat)
            info.returnValue = super.trySleep(pos)
        }
    }

}