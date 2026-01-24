package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Biomes We've Gone mod.
 * Handles food value modifications for Biomes We've Gone food items.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw fruits and produce (no crafting)
 * - COOKED_FOOD: Simply cooked items (furnace/smoker)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients)
 * - AVERAGE_MEAL: Moderate crafting (4-5 ingredients, soups/stews)
 */
public class BiomesWeveGoneModule {

    private static final String BIOMES_WEVE_GONE_MOD_ID = "biomeswevegone";
    private static boolean biomesWeveGoneLoaded = false;

    /**
     * Initialize Biomes We've Gone integration.
     */
    public void init() {
        biomesWeveGoneLoaded = Platform.isModLoaded(BIOMES_WEVE_GONE_MOD_ID);

        if (!biomesWeveGoneLoaded) {
            HOReborn.LOGGER.debug("Biomes We've Gone not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Biomes We've Gone integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Biomes We've Gone items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fruits, bulbs, mushrooms (no crafting)
     * - COOKED_FOOD: Simply cooked items (furnace/smoker)
     * - LIGHT_MEAL: Simple crafted items (pies, juice)
     * - AVERAGE_MEAL: Moderate crafting (soups/stews with multiple ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw fruits and produce
        setBiomesWeveGoneFoodValue("baobab_fruit", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("blueberries", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("green_apple", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("oddion_bulb", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("white_puffball_cap", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("yucca_fruit", 1, 0.05f, "RAW_FOOD");
        setBiomesWeveGoneFoodValue("blooming_oddion", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked items
        setBiomesWeveGoneFoodValue("cooked_oddion_bulb", 2, 0.25f, "COOKED_FOOD");
        setBiomesWeveGoneFoodValue("cooked_white_puffball_cap", 2, 0.25f, "COOKED_FOOD");
        setBiomesWeveGoneFoodValue("cooked_yucca_fruit", 2, 0.25f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items (2-3 ingredients)
        setBiomesWeveGoneFoodValue("blueberry_pie", 4, 0.3f, "LIGHT_MEAL"); // Pie = fruit + dough + sugar
        setBiomesWeveGoneFoodValue("green_apple_pie", 4, 0.3f, "LIGHT_MEAL"); // Pie = fruit + dough + sugar
        setBiomesWeveGoneFoodValue("aloe_vera_juice", 4, 0.2f, "LIGHT_MEAL"); // Juice = aloe + processing
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (4-5 ingredients, soups/stews)
        setBiomesWeveGoneFoodValue("allium_oddion_soup", 6, 0.4f, "AVERAGE_MEAL"); // Soup = multiple ingredients
        setBiomesWeveGoneFoodValue("white_puffball_stew", 6, 0.4f, "AVERAGE_MEAL"); // Stew = multiple ingredients
    }

    /**
     * Set food values for a specific Biomes We've Gone item.
     */
    private void setBiomesWeveGoneFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = BIOMES_WEVE_GONE_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "BIOMES_WEVE_GONE_" + category
        ));
    }

    /**
     * Check if Biomes We've Gone is loaded.
     */
    public static boolean isBiomesWeveGoneLoaded() {
        return biomesWeveGoneLoaded;
    }

    /**
     * Get the Biomes We've Gone mod ID.
     */
    public static String getBiomesWeveGoneModId() {
        return BIOMES_WEVE_GONE_MOD_ID;
    }
}

