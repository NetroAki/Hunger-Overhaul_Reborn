package org.Netroaki.Main.mixin.forge;

import net.minecraft.world.entity.animal.Chicken;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chicken.class)
public abstract class ChickenMixin {

    @org.spongepowered.asm.mixin.Shadow
    public int eggTime;

    @Redirect(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Chicken;eggTime:I", opcode = org.objectweb.asm.Opcodes.PUTFIELD))
    private void hor_modifyEggTime(int value) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (config != null) {
            value = (int) (value * config.animals.eggTimeoutMultiplier);
        }
        this.eggTime = value;
    }
}
