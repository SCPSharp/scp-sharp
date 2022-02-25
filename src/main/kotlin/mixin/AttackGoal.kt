package com.xtex.scpsharp.mixin

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.AttackGoal
import net.minecraft.entity.mob.MobEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(AttackGoal::class)
interface AttackGoalAccessor {

    @Accessor("mob")
    fun getMob(): MobEntity

    @Accessor("target")
    fun getTarget(): LivingEntity

    @Accessor("cooldown")
    fun getCoolDown(): Int

    @Accessor("cooldown")
    fun setCoolDown(value: Int)

}