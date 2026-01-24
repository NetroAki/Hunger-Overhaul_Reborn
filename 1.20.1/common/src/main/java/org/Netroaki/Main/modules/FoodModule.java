package org.Netroaki.Main.modules;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.FoodUtil;

public class FoodModule {
    
    public void init() {
        HOReborn.LOGGER.info("Initializing Food Module");
        
        // Register client events for tooltips and GUI
        if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
            registerTooltipEvents();
        }
        
        if (HungerOverhaulConfig.getInstance().food.enableLowHungerWarnings) {
            registerGUIEvents();
        }
    }
    
    private void registerTooltipEvents() {
        // This would be implemented in the client-side module
        // For now, we'll just log that tooltips are enabled
        HOReborn.LOGGER.info("Food tooltips enabled");
    }
    
    private void registerGUIEvents() {
        // This would be implemented in the client-side module
        // For now, we'll just log that GUI warnings are enabled
        HOReborn.LOGGER.info("Low hunger/health warnings enabled");
    }
    
    public static Component getFoodTooltip(ItemStack stack) {
        if (!stack.isEdible()) return null;
        
        String description = FoodUtil.getFoodDescription(stack);
        if (description.isEmpty()) return null;
        
        return Component.literal(description);
    }
    
    public static void renderLowHungerWarning(GuiGraphics graphics, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (!config.food.enableLowHungerWarnings) return;
        
        int hungerLevel = minecraft.player.getFoodData().getFoodLevel();
        float health = minecraft.player.getHealth();
        float maxHealth = minecraft.player.getMaxHealth();
        
        Font font = minecraft.font;
        
        // Render hunger warning
        if (hungerLevel <= 6) {
            String hungerText = "Low Hunger!";
            int color = hungerLevel <= 2 ? 0xFF0000 : (hungerLevel <= 4 ? 0xFFAA00 : 0xFFFF00);
            
            int x = screenWidth - font.width(hungerText) - 10;
            int y = 10;
            
            graphics.drawString(font, hungerText, x, y, color);
        }
        
        // Render health warning
        if (health <= maxHealth * 0.3f) {
            String healthText = "Low Health!";
            int color = 0xFF0000;
            
            int x = 10;
            int y = 10;
            
            graphics.drawString(font, healthText, x, y, color);
        }
    }
}
