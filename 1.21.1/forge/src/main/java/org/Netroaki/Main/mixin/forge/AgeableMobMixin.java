package org.Netroaki.Main.mixin.forge;

import net.minecraft.world.entity.AgeableMob;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin {

    @Inject(method = "setBaby", at = @At("HEAD"), cancellable = true)
    private void hor_setBaby(boolean baby, CallbackInfo ci) {
        if (baby) {
            HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
            if (config != null) {
                double multiplier = config.animals.childDurationMultiplier;
                if (multiplier != 1.0) {
                    ((AgeableMob) (Object) this).setAge((int) (-24000 * multiplier));
                    ci.cancel();
                }
            }
        }
    }
}
