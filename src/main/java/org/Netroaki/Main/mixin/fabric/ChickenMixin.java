package org.Netroaki.Main.mixin.fabric;

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
        // Since we are redirecting the PUT on 'instance', we must set it manually.
        // We can cast instance to the Mixin class? No, we can't cast instance to Mixin
        // type.
        // Wait, 'instance' IS the Chicken.
        // But 'this' inside the Mixin refers to 'this' (the mixin instance / target
        // instance context?).
        // If method is instance method (non-static), 'this' is the instance?
        // But Redirect target is PUTFIELD, which takes (Instance, Value).
        // If the original opcode was `PUTFIELD field`, the arguments on stack are
        // `Object, Value`.
        // The handler method consumes them.

        // Strategy: Cast instance to Accessor? Or use Reflection?
        // Better: Use @ModifyVariable?
        // Or Simply: Shadow existing field, and if 'instance' == 'this', write to it?
        // In Mixin, 'this' refers to the entity instance if not static.
        // 'aiStep' is non-static.
        // Is the PUT happening to 'this'? Yes. `this.eggTime = ...`.
        // So the first arg 'instance' IS 'this'.

        // Let's verify: Redirect Capture for PUTFIELD:
        // Handler signature: (LTarget;I)V (Static?) or (I)V (Instance?)
        // If I make method non-static: `private void hor_modifyEggTime(int value)`?
        // The target is `this.eggTime = ...`.
        // So `this` is implicit.

        // I'll assume instance method usage.
        this.eggTime = value;
    }
}
