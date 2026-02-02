package org.Netroaki.Main.mixin.fabric;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @org.spongepowered.asm.mixin.Mutable
    @Accessor("maxStackSize")
    void setMaxStackSize(int size);
}
