package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for The Aether mod.
 * Handles food value modifications for Aether dimension foods.
 */
public class AetherModule {

    private static final String AETHER_MOD_ID = "aether";
    private static boolean aetherLoaded = false;

    /**
     * Initialize Aether integration.
     */
    public void init() {
        aetherLoaded = Platform.isModLoaded(AETHER_MOD_ID);

        if (!aetherLoaded) {
            HOReborn.LOGGER.debug("The Aether not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing The Aether integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Aether items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw berries and fruits (no crafting)
     * - LIGHT_MEAL: Simple crafted items (candy, gummy swets, gingerbread - 2-3 ingredients)
     * - AVERAGE_MEAL: Special items (enchanted berry, healing stone - special crafting)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw berries and fruits
        setAetherFoodValue("blue_berry", 1, 0.05f, "RAW_FOOD");
        setAetherFoodValue("white_apple", 1, 0.05f, "RAW_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items (2-3 ingredients)
        setAetherFoodValue("blue_gummy_swet", 4, 0.2f, "LIGHT_MEAL"); // Swet + sugar/processing
        setAetherFoodValue("golden_gummy_swet", 4, 0.25f, "LIGHT_MEAL"); // Swet + gold + processing
        setAetherFoodValue("candy_cane", 4, 0.2f, "LIGHT_MEAL"); // Sugar + flavoring
        setAetherFoodValue("gingerbread_man", 4, 0.25f, "LIGHT_MEAL"); // Flour + sugar + spices
        
        // AVERAGE_MEAL (6 hunger) - Special items (special crafting)
        setAetherFoodValue("enchanted_berry", 6, 0.4f, "AVERAGE_MEAL"); // Berry + enchantment
        setAetherFoodValue("healing_stone", 6, 0.4f, "AVERAGE_MEAL"); // Special healing item
    }

    /**
     * Set food values for a specific Aether item.
     */
    private void setAetherFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = AETHER_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "AETHER_" + category
        ));
    }

    /**
     * Check if The Aether is loaded.
     */
    public static boolean isAetherLoaded() {
        return aetherLoaded;
    }

    /**
     * Get the Aether mod ID.
     */
    public static String getAetherModId() {
        return AETHER_MOD_ID;
    }
}

