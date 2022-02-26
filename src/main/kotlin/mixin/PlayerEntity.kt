@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.xtex.scpsharp.mixin

import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008StatusEffect
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
        // Infect SCP-008 each other
        if (entity is LivingEntity) {
            val targetInfected = entity.hasStatusEffect(SCP008StatusEffect)
            val selfInfected = !(this as PlayerEntity).hasStatusEffect(SCP008StatusEffect)
            if (targetInfected && selfInfected) {
                SCP008.logger.info("$this infected SCP-008 to $entity")
                SCP008StatusEffect.infect((this as PlayerEntity), entity)
            } else if (selfInfected && !targetInfected) {
                SCP008.logger.info("$this infected SCP-008 from $entity")
                SCP008StatusEffect.infect(entity, (this as PlayerEntity))
            }
        }
    }

}
