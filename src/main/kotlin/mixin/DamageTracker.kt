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
