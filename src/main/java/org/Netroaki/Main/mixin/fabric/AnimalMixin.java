package org.Netroaki.Main.mixin.fabric;

import net.minecraft.world.entity.animal.Animal;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public abstract class AnimalMixin {

    @Shadow
    private int inLove;

    /**
     * Inject after breeding to extend the breeding cooldown (resetLove timer).
     * After an animal breeds, it gets a cooldown (resetLove) set to 6000 ticks (5
     * minutes).
     * We multiply this cooldown by the configured multiplier.
     * 
     * This targets the end of the spawnChildFromBreeding method in 1.21+ or
     * the breed method in 1.20.1, whichever exists.
     */
    @Inject(method = "spawnChildFromBreeding", at = @At("RETURN"), require = 0 // Make it optional - won't fail if
                                                                               // method doesn't exist
    )
    private void hor_extendBreedingCooldown_1_21(CallbackInfo ci) {
        extendCooldown();
    }

    @Inject(method = "breed", at = @At("RETURN"), require = 0 // Make it optional - won't fail if method doesn't exist
    )
    private void hor_extendBreedingCooldown_1_20(CallbackInfo ci) {
        extendCooldown();
    }

    private void extendCooldown() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (config != null && config.animals.breedingTimeoutMultiplier != 1.0) {
            // After breeding, inLove is reset to 0 and a cooldown starts
            // We need to access the resetLove field which is private
            // For now, just set inLove to negative (cooldown) value
            if (this.inLove == 0) {
                // Breeding just finished, apply extended cooldown
                this.inLove = -(int) (6000 * config.animals.breedingTimeoutMultiplier);
            }
        }
    }
}
