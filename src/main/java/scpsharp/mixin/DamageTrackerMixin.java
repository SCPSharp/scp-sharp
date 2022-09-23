/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import kotlin.collections.CollectionsKt;
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

import java.util.Arrays;
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
        var world = getEntity().getWorld();
        var chunks = new WorldChunk[]{
                world.getWorldChunk(getEntity().getBlockPos().add(6, 0, 6)),
                world.getWorldChunk(getEntity().getBlockPos().add(6, 0, -6)),
                world.getWorldChunk(getEntity().getBlockPos().add(-6, 0, 6)),
                world.getWorldChunk(getEntity().getBlockPos().add(-6, 0, -6)),
        };
        var entities = CollectionsKt.flatMap(Arrays.asList(chunks), (chunk) -> chunk.getBlockEntities().values());
        entities = CollectionsKt.filter(entities, (entity) -> entity instanceof SCP008ContainmentBlockEntity);
        entities = CollectionsKt.filter(entities, (entity) -> entity.getPos().getSquaredDistance(getEntity().getBlockPos()) <= 6 * 6);
        entities = CollectionsKt.filter(entities, (entity) -> Objects.requireNonNull(entity.getWorld()).getBlockState(entity.getPos()).get(Properties.OPEN));
        for (var entity : entities) {
            SCP008.INSTANCE.getLOGGER().info(getEntity() + " got " + damageSource + " x" + damage + " and getting infected from a open containment box at " + entity.getPos());
            if (getEntity() != null && getEntity() instanceof PlayerEntity playerEntity) {
                playerEntity.incrementStat(SCP008.INSTANCE.getINFECTING_STAT());
            }
            SCP008StatusEffect.INSTANCE.infect(getEntity(), damageSource.getSource());
            return;
        }
    }

}
