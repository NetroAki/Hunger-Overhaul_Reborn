package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Twilight Forest mod.
 * Handles food value modifications for Twilight Forest dimension foods.
 */
public class TwilightForestModule {

    private static final String TWILIGHT_FOREST_MOD_ID = "twilightforest";
    private static boolean twilightForestLoaded = false;

    /**
     * Initialize Twilight Forest integration.
     */
    public void init() {
        twilightForestLoaded = Platform.isModLoaded(TWILIGHT_FOREST_MOD_ID);

        if (!twilightForestLoaded) {
            HOReborn.LOGGER.debug("Twilight Forest not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Twilight Forest integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Twilight Forest items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw berries and meats (no crafting)
     * - COOKED_FOOD: Simply cooked meats (furnace/smoker)
     * - LIGHT_MEAL: Simple crafted items (wafer, experiment - 2-3 ingredients)
     * - AVERAGE_MEAL: Moderate crafting (stroganoff - 4-5 ingredients)
     * - LARGE_MEAL: Complex/boss items (hydra chop - special crafting)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw berries and meats
        setTwilightForestFoodValue("torchberries", 1, 0.05f, "RAW_FOOD");
        setTwilightForestFoodValue("raw_venison", 1, 0.05f, "RAW_FOOD");
        setTwilightForestFoodValue("raw_meef", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked meats
        setTwilightForestFoodValue("cooked_venison", 2, 0.25f, "COOKED_FOOD");
        setTwilightForestFoodValue("cooked_meef", 2, 0.25f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items
        setTwilightForestFoodValue("maze_wafer", 4, 0.2f, "LIGHT_MEAL"); // Wafer = flour + sugar
        setTwilightForestFoodValue("experiment_115", 4, 0.3f, "LIGHT_MEAL"); // Special item, likely simple crafting
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (multiple ingredients)
        setTwilightForestFoodValue("meef_stroganoff", 6, 0.4f, "AVERAGE_MEAL"); // Meat + noodles + sauce + vegetables
        
        // LARGE_MEAL (8 hunger) - Boss drop, special crafting
        setTwilightForestFoodValue("hydra_chop", 8, 0.5f, "LARGE_MEAL"); // Boss drop, very high value
    }

    /**
     * Set food values for a specific Twilight Forest item.
     */
    private void setTwilightForestFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = TWILIGHT_FOREST_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "TWILIGHT_FOREST_" + category
        ));
    }

    /**
     * Check if Twilight Forest is loaded.
     */
    public static boolean isTwilightForestLoaded() {
        return twilightForestLoaded;
    }

    /**
     * Get the Twilight Forest mod ID.
     */
    public static String getTwilightForestModId() {
        return TWILIGHT_FOREST_MOD_ID;
    }
}

