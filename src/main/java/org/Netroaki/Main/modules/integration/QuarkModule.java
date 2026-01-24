package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Quark mod.
 * Handles food value modifications for Quark food items.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw seafood and fruits (no crafting)
 * - COOKED_FOOD: Simply cooked seafood (furnace/smoker)
 */
public class QuarkModule {

    private static final String QUARK_MOD_ID = "quark";
    private static boolean quarkLoaded = false;

    /**
     * Initialize Quark integration.
     */
    public void init() {
        quarkLoaded = Platform.isModLoaded(QUARK_MOD_ID);

        if (!quarkLoaded) {
            HOReborn.LOGGER.debug("Quark not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Quark integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Quark items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw seafood and fruits (no crafting)
     * - COOKED_FOOD: Simply cooked seafood (furnace/smoker)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw seafood and fruits
        setQuarkFoodValue("crab_leg", 1, 0.05f, "RAW_FOOD");
        setQuarkFoodValue("ancient_fruit", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked seafood
        setQuarkFoodValue("cooked_crab_leg", 2, 0.25f, "COOKED_FOOD");
    }

    /**
     * Set food values for a specific Quark item.
     */
    private void setQuarkFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = QUARK_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "QUARK_" + category
        ));
    }

    /**
     * Check if Quark is loaded.
     */
    public static boolean isQuarkLoaded() {
        return quarkLoaded;
    }

    /**
     * Get the Quark mod ID.
     */
    public static String getQuarkModId() {
        return QUARK_MOD_ID;
    }
}

