/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scpsharp.subject.scp714.SCP714;
import scpsharp.subject.scp714.SCP714Item;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
            method = "trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void trySleep(BlockPos pos, CallbackInfoReturnable<Either<SleepFailureReason, Unit>> info) {
        for (var stack : getHandItems()) {
            if (stack.getItem() instanceof SCP714Item) {
                incrementStat(SCP714.INSTANCE.getSLEEP_WITH_STAT());
                info.setReturnValue(super.trySleep(pos));
                break;
            }
        }
    }

}
