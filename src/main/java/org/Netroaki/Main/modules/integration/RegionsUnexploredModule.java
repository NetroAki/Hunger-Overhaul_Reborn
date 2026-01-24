package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Regions Unexplored mod.
 * Handles food value modifications for Regions Unexplored food items.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw fruits and produce (no crafting)
 */
public class RegionsUnexploredModule {

    private static final String REGIONS_UNEXPLORED_MOD_ID = "regions_unexplored";
    private static boolean regionsUnexploredLoaded = false;

    /**
     * Initialize Regions Unexplored integration.
     */
    public void init() {
        regionsUnexploredLoaded = Platform.isModLoaded(REGIONS_UNEXPLORED_MOD_ID);

        if (!regionsUnexploredLoaded) {
            HOReborn.LOGGER.debug("Regions Unexplored not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Regions Unexplored integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Regions Unexplored items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fruits and produce (no crafting required)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw fruits and produce
        setRegionsUnexploredFoodValue("duskmelon_slice", 1, 0.05f, "RAW_FOOD");
        setRegionsUnexploredFoodValue("hanging_earlight_fruit", 1, 0.05f, "RAW_FOOD");
    }

    /**
     * Set food values for a specific Regions Unexplored item.
     */
    private void setRegionsUnexploredFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = REGIONS_UNEXPLORED_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "REGIONS_UNEXPLORED_" + category
        ));
    }

    /**
     * Check if Regions Unexplored is loaded.
     */
    public static boolean isRegionsUnexploredLoaded() {
        return regionsUnexploredLoaded;
    }

    /**
     * Get the Regions Unexplored mod ID.
     */
    public static String getRegionsUnexploredModId() {
        return REGIONS_UNEXPLORED_MOD_ID;
    }
}

