package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Deeper and Darker mod.
 * Handles food value modifications for Deeper and Darker food items.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw berries (no crafting)
 */
public class DeeperDarkerModule {

    private static final String DEEPER_DARKER_MOD_ID = "deeperdarker";
    private static boolean deeperDarkerLoaded = false;

    /**
     * Initialize Deeper and Darker integration.
     */
    public void init() {
        deeperDarkerLoaded = Platform.isModLoaded(DEEPER_DARKER_MOD_ID);

        if (!deeperDarkerLoaded) {
            HOReborn.LOGGER.debug("Deeper and Darker not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Deeper and Darker integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Deeper and Darker items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw berries (no crafting required)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw berries
        setDeeperDarkerFoodValue("bloom_berries", 1, 0.05f, "RAW_FOOD");
    }

    /**
     * Set food values for a specific Deeper and Darker item.
     */
    private void setDeeperDarkerFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = DEEPER_DARKER_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "DEEPER_DARKER_" + category
        ));
    }

    /**
     * Check if Deeper and Darker is loaded.
     */
    public static boolean isDeeperDarkerLoaded() {
        return deeperDarkerLoaded;
    }

    /**
     * Get the Deeper and Darker mod ID.
     */
    public static String getDeeperDarkerModId() {
        return DEEPER_DARKER_MOD_ID;
    }
}

