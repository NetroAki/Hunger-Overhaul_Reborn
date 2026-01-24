package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Productive Bees mod.
 * Handles food value modifications for honey and bee-related foods.
 */
public class ProductiveBeesModule {

    private static final String PRODUCTIVE_BEES_MOD_ID = "productivebees";
    private static boolean productiveBeesLoaded = false;

    /**
     * Initialize Productive Bees integration.
     */
    public void init() {
        productiveBeesLoaded = Platform.isModLoaded(PRODUCTIVE_BEES_MOD_ID);

        if (!productiveBeesLoaded) {
            HOReborn.LOGGER.debug("Productive Bees not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Productive Bees integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Productive Bees items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw honeycomb (no crafting, direct from bees)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw honeycomb
        setProductiveBeesFoodValue("sugarbag_honeycomb", 1, 0.05f, "RAW_FOOD"); // Direct from bees, no crafting
    }

    /**
     * Set food values for a specific Productive Bees item.
     */
    private void setProductiveBeesFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = PRODUCTIVE_BEES_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "PRODUCTIVE_BEES_" + category
        ));
    }

    /**
     * Check if Productive Bees is loaded.
     */
    public static boolean isProductiveBeesLoaded() {
        return productiveBeesLoaded;
    }

    /**
     * Get the Productive Bees mod ID.
     */
    public static String getProductiveBeesModId() {
        return PRODUCTIVE_BEES_MOD_ID;
    }
}

