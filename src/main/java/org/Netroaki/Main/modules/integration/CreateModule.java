package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Create mod (not Create: Food).
 * Handles food value modifications for Create mod food items.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw produce (no crafting)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients, basic recipes)
 */
public class CreateModule {

    private static final String CREATE_MOD_ID = "create";
    private static boolean createLoaded = false;

    /**
     * Initialize Create integration.
     */
    public void init() {
        createLoaded = Platform.isModLoaded(CREATE_MOD_ID);

        if (!createLoaded) {
            HOReborn.LOGGER.debug("Create not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Create integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Create items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw produce (honeyed_apple, chocolate_glazed_berries - berries are raw)
     * - LIGHT_MEAL: Simple crafted items (bar_of_chocolate, builders_tea, sweet_roll, chocolate_glazed_berries - glazed version)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw produce
        setCreateFoodValue("honeyed_apple", 1, 0.05f, "RAW_FOOD"); // Apple with honey, still raw produce
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items (2-3 ingredients)
        setCreateFoodValue("bar_of_chocolate", 4, 0.2f, "LIGHT_MEAL"); // Chocolate processing
        setCreateFoodValue("builders_tea", 4, 0.25f, "LIGHT_MEAL"); // Tea = water + leaves + optional
        setCreateFoodValue("sweet_roll", 4, 0.25f, "LIGHT_MEAL"); // Roll = flour + sugar + optional
        setCreateFoodValue("chocolate_glazed_berries", 4, 0.2f, "LIGHT_MEAL"); // Berries + chocolate glaze
    }

    /**
     * Set food values for a specific Create item.
     */
    private void setCreateFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = CREATE_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "CREATE_" + category
        ));
    }

    /**
     * Check if Create is loaded.
     */
    public static boolean isCreateLoaded() {
        return createLoaded;
    }

    /**
     * Get the Create mod ID.
     */
    public static String getCreateModId() {
        return CREATE_MOD_ID;
    }
}

