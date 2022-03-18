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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
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
        for (var stack : thisEntity.getItemsHand()) {
            if (stack.getItem() instanceof SCP714Item) {
                if (thisEntity.getAttacker() != null && !thisEntity.getType().isIn(SCP714.INSTANCE.getBypassTag())) {
                    if (thisEntity instanceof PlayerEntity thisPlayer
                            && (!(thisEntity instanceof ServerPlayerEntity)
                            || ((ServerPlayerEntity) thisEntity).networkHandler != null)) {
                        thisPlayer.sendMessage(new TranslatableText("scpsharp.scp714.wake_up_suppressed"), true);
                    }
                    info.cancel();
                }
                break;
            }
        }
    }

}
