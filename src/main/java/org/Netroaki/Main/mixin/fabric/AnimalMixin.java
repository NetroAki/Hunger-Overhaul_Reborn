package org.Netroaki.Main.mixin.fabric;

import net.minecraft.world.entity.animal.Animal;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Animal.class)
public class AnimalMixin {

    @ModifyConstant(method = "breed", constant = @Constant(intValue = 6000))
    private int hor_modifyBreedingCooldown(int constant) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (config != null) {
            return (int) (constant * config.animals.breedingTimeoutMultiplier);
        }
        return constant;
    }
}
