package org.Netroaki.Main.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onFoodDataTick(CallbackInfo ci) {
        // Food data tick logic is handled in PlayerEventHandler
        // This mixin is for any direct modifications needed
    }
}
