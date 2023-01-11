/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scpsharp.content.subject.scp714.SCP714;
import scpsharp.content.subject.scp714.SCP714Item;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(
            method = "wakeUp()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wakeUp(CallbackInfo info) {
        var thisEntity = (LivingEntity) (Object) this;
        for (var stack : thisEntity.getHandItems()) {
            if (stack.getItem() instanceof SCP714Item) {
                if (thisEntity.getAttacker() != null && !thisEntity.getType().isIn(SCP714.INSTANCE.getBYPASS_TAG())) {
                    if (thisEntity instanceof PlayerEntity thisPlayer
                            && (!(thisEntity instanceof ServerPlayerEntity)
                            || ((ServerPlayerEntity) thisEntity).networkHandler != null)) {
                        thisPlayer.sendMessage(Text.translatable("scpsharp.scp714.wake_up_suppressed"), true);
                    }
                    info.cancel();
                }
                break;
            }
        }
    }

}
