/*
 * Copyright (C) 2022  SCPSharp Team
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
                SCP008.INSTANCE.getLogger().info(this + " infected SCP-008 from " + entity + " by colliding");
                SCP008StatusEffect.INSTANCE.infect(selfEntity, targetEntity);
            } else if (selfInfected && !targetInfected && targetEntity.getHealth() <= targetEntity.getMaxHealth() * 0.8f) {
                SCP008.INSTANCE.getLogger().info(this + " infected SCP-008 to " + entity + " by colliding");
                SCP008StatusEffect.INSTANCE.infect(targetEntity, selfEntity);
            }
        }
    }

}
