package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Ice and Fire mod.
 * Handles food value modifications for dragon meat and other creature foods.
 */
public class IceAndFireModule {

    private static final String ICE_AND_FIRE_MOD_ID = "iceandfire";
    private static boolean iceAndFireLoaded = false;

    /**
     * Initialize Ice and Fire integration.
     */
    public void init() {
        iceAndFireLoaded = Platform.isModLoaded(ICE_AND_FIRE_MOD_ID);

        if (!iceAndFireLoaded) {
            HOReborn.LOGGER.debug("Ice and Fire not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Ice and Fire integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Ice and Fire items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw dragon flesh, pixie dust (no crafting)
     * - LIGHT_MEAL: Simple crafted desserts (cannoli - pastry + filling)
     * - AVERAGE_MEAL: Special healing foods (ambrosia - complex crafting)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw dragon flesh
        setIceAndFireFoodValue("fire_dragon_flesh", 1, 0.05f, "RAW_FOOD");
        setIceAndFireFoodValue("ice_dragon_flesh", 1, 0.05f, "RAW_FOOD");
        setIceAndFireFoodValue("lightning_dragon_flesh", 1, 0.05f, "RAW_FOOD");
        // Note: pixie_dust is not food, it's a crafting ingredient
        
        // LIGHT_MEAL (4 hunger) - Simple crafted desserts
        setIceAndFireFoodValue("cannoli", 4, 0.3f, "LIGHT_MEAL"); // Pastry + filling
        
        // AVERAGE_MEAL (6 hunger) - Special healing food (complex crafting)
        setIceAndFireFoodValue("ambrosia", 6, 0.4f, "AVERAGE_MEAL"); // Special healing food
    }

    /**
     * Set food values for a specific Ice and Fire item.
     */
    private void setIceAndFireFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = ICE_AND_FIRE_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "ICE_AND_FIRE_" + category
        ));
    }

    /**
     * Check if Ice and Fire is loaded.
     */
    public static boolean isIceAndFireLoaded() {
        return iceAndFireLoaded;
    }

    /**
     * Get the Ice and Fire mod ID.
     */
    public static String getIceAndFireModId() {
        return ICE_AND_FIRE_MOD_ID;
    }
}

