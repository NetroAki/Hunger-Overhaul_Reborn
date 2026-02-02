package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.handlers.FoodEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "eat", at = @At("HEAD"))
    private void onEat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if ((Object) this instanceof Player player) {
            FoodEventHandler.onFoodConsumed(player, stack);
        }
    }
}
