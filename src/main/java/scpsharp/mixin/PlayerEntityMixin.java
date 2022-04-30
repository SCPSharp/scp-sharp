/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scpsharp.content.subject.scp008.SCP008;
import scpsharp.content.subject.scp008.SCP008StatusEffect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(
            method = "collideWithEntity(Lnet/minecraft/entity/Entity;)V",
            at = @At("TAIL")
    )
    private void collideWithEntity(Entity entity, CallbackInfo info) {
        // Infect SCP-008 each other
        if (entity instanceof LivingEntity targetEntity) {
            var selfEntity = ((LivingEntity) (Object) this);
            var targetInfected = targetEntity.hasStatusEffect(SCP008StatusEffect.INSTANCE);
            var selfInfected = selfEntity.hasStatusEffect(SCP008StatusEffect.INSTANCE);
            if (targetInfected && !selfInfected && selfEntity.getHealth() <= selfEntity.getMaxHealth() * 0.8f) {
                SCP008.INSTANCE.getLOGGER().info(this + " infected SCP-008 from " + entity + " by colliding");
                SCP008StatusEffect.INSTANCE.infect(selfEntity, targetEntity);
            } else if (selfInfected && !targetInfected && targetEntity.getHealth() <= targetEntity.getMaxHealth() * 0.8f) {
                SCP008.INSTANCE.getLOGGER().info(this + " infected SCP-008 to " + entity + " by colliding");
                SCP008StatusEffect.INSTANCE.infect(targetEntity, selfEntity);
            }
        }
    }

}
