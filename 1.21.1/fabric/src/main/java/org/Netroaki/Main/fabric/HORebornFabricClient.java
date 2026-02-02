package org.Netroaki.Main.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.modules.FoodModule;

public final class HORebornFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register tooltip callback for food descriptions
        // 1.21.1: (stack, context, type, lines)
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
                var tooltip = FoodModule.getFoodTooltip(stack);
                if (tooltip != null) {
                    lines.add(tooltip);
                }
            }
        });

        // Register HUD callback for low hunger/health warnings
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            Minecraft client = Minecraft.getInstance();
            if (client.getWindow() != null) {
                int width = client.getWindow().getGuiScaledWidth();
                int height = client.getWindow().getGuiScaledHeight();
                renderLowHungerWarning(graphics, width, height);
            }
        });
    }

    public static void renderLowHungerWarning(GuiGraphics graphics, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null)
            return;

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (!config.food.enableLowHungerWarnings)
            return;

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
