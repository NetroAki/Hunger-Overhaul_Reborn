package org.Netroaki.Main.mixin.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.handlers.FoodEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to modify food eating duration based on food value.
 */
@Mixin(Item.class)
public class EatingDurationMixin {

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void modifyUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!stack.isEdible()) {
            return;
        }

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (!config.food.modifyEatingSpeed) {
            return;
        }

        var food = stack.getItem().getFoodProperties();
        if (food != null) {
            int foodValue = food.getNutrition();
            int duration = FoodEventHandler.getEatingDuration(foodValue);
            cir.setReturnValue(duration);
        }
    }
}

