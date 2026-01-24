package org.Netroaki.Main.mixin.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to disable hunger drain when healing (vanilla 1.6.2 behavior).
 */
@Mixin(Player.class)
public class PlayerHealingMixin {

    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
    private void hor_onCauseFoodExhaustion(float exhaustion, CallbackInfo ci) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Check if we should disable healing hunger drain
        if (config.health.disableHealingHungerDrain) {
            Player player = (Player) (Object) this;

            // Check if this exhaustion is from healing
            // In vanilla, healing causes 6.0F exhaustion
            if (exhaustion == 6.0F) {
                // Cancel the exhaustion to prevent hunger drain from healing
                ci.cancel();
            }
        }
    }
}
