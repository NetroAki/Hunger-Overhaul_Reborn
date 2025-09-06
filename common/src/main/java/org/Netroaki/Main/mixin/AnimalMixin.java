package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Animal.class)
public class AnimalMixin {
    
    // Animal modifications are handled in AnimalModule
    // This mixin is for any direct modifications needed
}
