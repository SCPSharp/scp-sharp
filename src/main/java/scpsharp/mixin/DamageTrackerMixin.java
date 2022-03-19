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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scpsharp.content.subject.scp008.SCP008;
import scpsharp.content.subject.scp008.SCP008ContainmentBlockEntity;
import scpsharp.content.subject.scp008.SCP008StatusEffect;

import java.util.LinkedHashSet;
import java.util.Objects;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin {

    @Shadow
    @Final
    public abstract LivingEntity getEntity();

    @Inject(
            method = "onDamage(Lnet/minecraft/entity/damage/DamageSource;FF)V",
            at = @At("RETURN")
    )
    public void onDamage(DamageSource damageSource, float originalHealth, float damage, CallbackInfo info) {
        // Infect SCP-008 from containment block
        var chunks = new LinkedHashSet<WorldChunk>();
        chunks.add(getEntity().getWorld().getWorldChunk(getEntity().getBlockPos().add(6, 0, -6)));
        chunks.add(getEntity().getWorld().getWorldChunk(getEntity().getBlockPos().add(6, 0, 6)));
        chunks.add(getEntity().getWorld().getWorldChunk(getEntity().getBlockPos().add(-6, 0, -6)));
        chunks.add(getEntity().getWorld().getWorldChunk(getEntity().getBlockPos().add(-6, 0, 6)));
        chunks.stream()
                .flatMap((chunk) -> chunk.getBlockEntities().values().stream())
                .filter((entity) -> entity instanceof SCP008ContainmentBlockEntity)
                .filter((entity) -> entity.getPos().getSquaredDistance(getEntity().getBlockPos()) <= 6 * 6)
                .filter((entity) -> Objects.requireNonNull(entity.getWorld()).getBlockState(entity.getPos()).get(Properties.OPEN))
                .forEach((entity) -> {
                    SCP008.INSTANCE.getLogger().info(getEntity() + " got " + damageSource + " x" + damage + " and getting infected from a open containment box at " + entity.getPos());
                    if (getEntity() != null && getEntity() instanceof PlayerEntity playerEntity) {
                        playerEntity.incrementStat(SCP008.INSTANCE.getInfectingStat());
                    }
                    SCP008StatusEffect.INSTANCE.infect(getEntity(), damageSource.getSource());
                });
    }

}
