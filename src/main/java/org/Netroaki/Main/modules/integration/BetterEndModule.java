package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Better End mod.
 * Handles food value modifications for End dimension foods.
 */
public class BetterEndModule {

    private static final String BETTER_END_MOD_ID = "betterend";
    private static boolean betterEndLoaded = false;

    /**
     * Initialize Better End integration.
     */
    public void init() {
        betterEndLoaded = Platform.isModLoaded(BETTER_END_MOD_ID);

        if (!betterEndLoaded) {
            HOReborn.LOGGER.debug("Better End not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Better End integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Better End items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw berries, mushrooms, fish, roots (no crafting)
     * - COOKED_FOOD: Simply cooked items (furnace/smoker)
     * - LIGHT_MEAL: Simple processed items (jelly, juice - 2-3 ingredients)
     * - AVERAGE_MEAL: Moderate crafting (pie - 4-5 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw berries, mushrooms, fish, roots
        setBetterEndFoodValue("shadow_berry_raw", 1, 0.05f, "RAW_FOOD");
        setBetterEndFoodValue("blossom_berry", 1, 0.05f, "RAW_FOOD");
        setBetterEndFoodValue("chorus_mushroom_raw", 1, 0.05f, "RAW_FOOD");
        setBetterEndFoodValue("end_fish_raw", 1, 0.05f, "RAW_FOOD");
        setBetterEndFoodValue("amber_root_raw", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked items
        setBetterEndFoodValue("shadow_berry_cooked", 2, 0.25f, "COOKED_FOOD");
        setBetterEndFoodValue("chorus_mushroom_cooked", 2, 0.25f, "COOKED_FOOD");
        setBetterEndFoodValue("bolux_mushroom_cooked", 2, 0.25f, "COOKED_FOOD");
        setBetterEndFoodValue("end_fish_cooked", 2, 0.25f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple processed items (2-3 ingredients)
        setBetterEndFoodValue("shadow_berry_jelly", 4, 0.2f, "LIGHT_MEAL"); // Berry + sugar + processing
        setBetterEndFoodValue("blossom_berry_jelly", 4, 0.2f, "LIGHT_MEAL"); // Berry + sugar + processing
        setBetterEndFoodValue("sweet_berry_jelly", 4, 0.2f, "LIGHT_MEAL"); // Berry + sugar + processing
        setBetterEndFoodValue("umbrella_cluster_juice", 4, 0.2f, "LIGHT_MEAL"); // Cluster + processing
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (4-5 ingredients)
        setBetterEndFoodValue("cave_pumpkin_pie", 6, 0.4f, "AVERAGE_MEAL"); // Pumpkin + dough + sugar + spices
    }

    /**
     * Set food values for a specific Better End item.
     */
    private void setBetterEndFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = BETTER_END_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "BETTER_END_" + category
        ));
    }

    /**
     * Check if Better End is loaded.
     */
    public static boolean isBetterEndLoaded() {
        return betterEndLoaded;
    }

    /**
     * Get the Better End mod ID.
     */
    public static String getBetterEndModId() {
        return BETTER_END_MOD_ID;
    }
}

