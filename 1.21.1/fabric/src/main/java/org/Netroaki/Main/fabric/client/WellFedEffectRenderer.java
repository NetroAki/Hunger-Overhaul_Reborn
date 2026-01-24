package org.Netroaki.Main.fabric.client;

import org.Netroaki.Main.HOReborn;

public class WellFedEffectRenderer {

    public static boolean renderEffectIcon(Object effect, Object graphics, int x, int y, float alpha) {
        try {
            // Check if this is a WellFed effect using reflection
            if (isWellFedEffect(effect)) {
                // Render bread item as the effect icon using reflection
                renderBreadIcon(graphics, x, y);
                return true;
            }
        } catch (Exception e) {
            HOReborn.LOGGER.debug("WellFed effect icon rendering failed: " + e.getMessage());
        }
        return false;
    }

    private static boolean isWellFedEffect(Object effect) {
        try {
            // Check if the effect matches our WellFed effect
            return effect != null && effect.equals(HOReborn.WELL_FED_EFFECT);
        } catch (Exception e) {
            return false;
        }
    }

    private static void renderBreadIcon(Object graphics, int x, int y) {
        try {
            // Create bread item stack using reflection
            Class<?> itemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            Class<?> itemsClass = Class.forName("net.minecraft.world.item.Items");
            Object breadItem = itemsClass.getField("BREAD").get(null);
            Object breadStack = itemStackClass.getConstructor(breadItem.getClass()).newInstance(breadItem);

            // Render the item using reflection
            Class<?> guiGraphicsClass = Class.forName("net.minecraft.client.gui.GuiGraphics");
            guiGraphicsClass.getMethod("renderItem", itemStackClass, int.class, int.class)
                           .invoke(graphics, breadStack, x, y);
        } catch (Exception e) {
            HOReborn.LOGGER.debug("Bread icon rendering failed: " + e.getMessage());
        }
    }
}
