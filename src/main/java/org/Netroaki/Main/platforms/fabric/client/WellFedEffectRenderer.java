package org.Netroaki.Main.platforms.fabric.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.Netroaki.Main.effects.WellFedEffect;

public class WellFedEffectRenderer {
    
    public static boolean renderEffectIcon(MobEffectInstance effect, GuiGraphics graphics, int x, int y, float alpha) {
        if (effect.getEffect() instanceof WellFedEffect) {
            // Render bread item as the effect icon
            ItemStack breadStack = new ItemStack(Items.BREAD);
            graphics.renderItem(breadStack, x, y);
            return true;
        }
        return false;
    }
}
