package com.xtex.scpsharp.mixin

import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008StatusEffect
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.MilkBucketItem
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(MilkBucketItem::class)
class MilkBucketItemMixin {

    @Inject(
        method = ["finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;"],
        at = [At("HEAD")],
        cancellable = true
    )
    fun finishUsing(stack: ItemStack, world: World, user: LivingEntity, info: CallbackInfoReturnable<ItemStack>) {
        // SCP-008 bypass
        if(user.hasStatusEffect(SCP008StatusEffect) && !world.isClient) {
            SCP008.logger.info("$user trying to drink milk when is SCP-008 infected")
            user.removeStatusEffect(SCP008StatusEffect)
            user.addStatusEffect(StatusEffectInstance(SCP008StatusEffect, 20 * (13 + world.random.nextInt(4))))
            stack.decrement(1)
            info.returnValue = if (stack.isEmpty) ItemStack(Items.BUCKET) else stack
        }
    }

}