package org.Netroaki.Main.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Handles rendering GUI warnings and status text on the HUD.
 */
public class HudRenderer {

    private static final Minecraft minecraft = Minecraft.getInstance();

    /**
     * Render GUI warnings on the HUD.
     */
    public static void renderHudWarnings(GuiGraphics guiGraphics, float partialTick) {
        renderHudWarningsInternal(guiGraphics, partialTick);
    }

    /**
     * Internal rendering logic.
     */
    private static void renderHudWarningsInternal(GuiGraphics guiGraphics, float partialTick) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        if (!config.gui.addGuiText || minecraft.player == null) {
            return;
        }

        Player player = minecraft.player;
        Font font = minecraft.font;

        int x = config.gui.guiTextX;
        int y = config.gui.guiTextY;
        int lineHeight = font.lineHeight + 2;

        // Well Fed status is shown by Minecraft's built-in effect system

        // Show effect warnings (hunger, health, and status effects)
        if (config.gui.effect_warnings) {
            // Show hunger warnings
            int hungerLevel = player.getFoodData().getFoodLevel();

            if (hungerLevel <= config.gui.lowHungerTextThreshold) {
                Component hungerText;
                int color;

                if (hungerLevel <= 2) {
                    hungerText = Component.translatable("gui.hunger_overhaul_reborn.hunger.starving");
                    color = 0xFF0000; // Red
                } else if (hungerLevel <= 4) {
                    hungerText = Component.translatable("gui.hunger_overhaul_reborn.hunger.very_hungry");
                    color = 0xFF8800; // Orange
                } else {
                    hungerText = Component.translatable("gui.hunger_overhaul_reborn.hunger.hungry");
                    color = 0xFFFF00; // Yellow
                }

                guiGraphics.drawString(font, hungerText, x, y, color);
                y += lineHeight;
            }

            // Show health warnings
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();

            if (health <= config.gui.lowHealthTextThreshold) {
                Component healthText;
                int color;

                if (health <= 2.0F) {
                    healthText = Component.translatable("gui.hunger_overhaul_reborn.health.critical");
                    color = 0xFF0000; // Red
                } else if (health <= 4.0F) {
                    healthText = Component.translatable("gui.hunger_overhaul_reborn.health.low");
                    color = 0xFF8800; // Orange
                } else {
                    healthText = Component.translatable("gui.hunger_overhaul_reborn.health.wounded");
                    color = 0xFFFF00; // Yellow
                }

                guiGraphics.drawString(font, healthText, x, y, color);
                y += lineHeight;
            }

            // Show effect status - only if effects are available (1.20.1 only)
            if (HOReborn.LOW_HEALTH_EFFECT != null) {
                var lowHealthEffect = player.getEffect(HOReborn.LOW_HEALTH_EFFECT.get());
                if (lowHealthEffect != null) {
                    Component effectText = Component.translatable("gui.hunger_overhaul_reborn.low_health_active");
                    guiGraphics.drawString(font, effectText, x, y, 0xFF4444); // Dark red
                    y += lineHeight;
                }
            }

            if (HOReborn.HUNGRY_EFFECT != null) {
                var hungerEffect = player.getEffect(HOReborn.HUNGRY_EFFECT.get());
                if (hungerEffect != null) {
                    Component effectText = Component.translatable("gui.hunger_overhaul_reborn.hunger_effect_active");
                    guiGraphics.drawString(font, effectText, x, y, 0x884400); // Brown
                }
            }
        }
    }
}
