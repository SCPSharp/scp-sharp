/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scpsharp.content.subject.scp008.SCP008;
import scpsharp.content.subject.scp008.SCP008StatusEffect;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @Inject(
            method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        // SCP-008 bypass
        if (user.hasStatusEffect(SCP008StatusEffect.INSTANCE) && !world.isClient) {
            SCP008.INSTANCE.getLogger().info(user + " trying to drink milk when is SCP-008 infected");
            user.removeStatusEffect(SCP008StatusEffect.INSTANCE);
            user.addStatusEffect(new StatusEffectInstance(SCP008StatusEffect.INSTANCE, 20 * (13 + world.random.nextInt(4))));
            stack.decrement(1);
            info.setReturnValue(stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack);
        }
    }

}
