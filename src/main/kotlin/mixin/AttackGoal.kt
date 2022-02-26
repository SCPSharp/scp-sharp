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

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.AttackGoal
import net.minecraft.entity.mob.MobEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(AttackGoal::class)
interface AttackGoalAccessor {

    @get:Accessor("mob")
    val mob: MobEntity

    @get:Accessor("target")
    val target: LivingEntity

    @get:Accessor("cooldown")
    @set:Accessor("cooldown")
    var coolDown: Int

}