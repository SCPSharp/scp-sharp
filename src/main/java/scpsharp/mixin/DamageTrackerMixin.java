/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import kotlin.collections.CollectionsKt;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scpsharp.subject.scp008.SCP008;
import scpsharp.subject.scp008.SCP008ContainmentBlockEntity;
import scpsharp.subject.scp008.SCP008StatusEffect;

import java.util.Arrays;
import java.util.Objects;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin {

    @Inject(
            method = "onDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
            at = @At("RETURN")
    )
    public void onDamage(DamageSource damageSource, float damage, CallbackInfo info) {
        // Infect SCP-008 from containment block
        var entity = ((DamageTracker) (Object) this).entity;
        var world = entity.getWorld();
        var chunks = new WorldChunk[]{
                world.getWorldChunk(entity.getBlockPos().add(6, 0, 6)),
                world.getWorldChunk(entity.getBlockPos().add(6, 0, -6)),
                world.getWorldChunk(entity.getBlockPos().add(-6, 0, 6)),
                world.getWorldChunk(entity.getBlockPos().add(-6, 0, -6)),
        };
        var entities = CollectionsKt.flatMap(Arrays.asList(chunks), (chunk) -> chunk.getBlockEntities().values());
        entities = CollectionsKt.filter(entities, (blockEntity) -> blockEntity instanceof SCP008ContainmentBlockEntity);
        entities = CollectionsKt.filter(entities, (blockEntity) -> blockEntity.getPos().getSquaredDistance(entity.getBlockPos()) <= 6 * 6);
        entities = CollectionsKt.filter(entities, (blockEntity) -> Objects.requireNonNull(blockEntity.getWorld()).getBlockState(blockEntity.getPos()).get(Properties.OPEN));
        for (var blockEntity : entities) {
            SCP008.INSTANCE.getLOGGER().info(entity + " got " + damageSource + " x" + damage + " and getting infected from a open containment box at " + blockEntity.getPos());
            if (entity instanceof PlayerEntity playerEntity) {
                playerEntity.incrementStat(SCP008.INSTANCE.getINFECTING_STAT());
            }
            SCP008StatusEffect.INSTANCE.infect(entity, damageSource.getSource());
            return;
        }
    }

}
