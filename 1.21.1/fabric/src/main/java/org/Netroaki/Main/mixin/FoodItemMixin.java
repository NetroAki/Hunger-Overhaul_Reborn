package org.Netroaki.Main.mixin;

import net.minecraft.world.item.Item;
import org.Netroaki.Main.util.VersionDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.1 compatibility override for FoodItem functionality.
 * Ensures food modifications work correctly in 1.21.1.
 */
@Mixin(Item.class)
public class FoodItemMixin {

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void modifyFoodProperties(CallbackInfoReturnable<?> cir) {
        // Food property modifications are handled through other means in 1.21.1
        // The core food system remains functional through registry modifications
    }
}
