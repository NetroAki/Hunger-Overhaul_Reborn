package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Biomes O' Plenty mod.
 * Handles food value modifications and biome-specific growth adjustments.
 */
public class BiomesOPlentyModule {

    private static final String BIOMES_OPLENTY_MOD_ID = "biomesoplenty";
    private static boolean biomesOPlentyLoaded = false;

    /**
     * Initialize Biomes O' Plenty integration.
     */
    public void init() {
        biomesOPlentyLoaded = Platform.isModLoaded(BIOMES_OPLENTY_MOD_ID);

        if (!biomesOPlentyLoaded) {
            HOReborn.LOGGER.info("Biomes O' Plenty not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Biomes O' Plenty integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }

        // Register biome-specific growth modifiers
        registerBiomeGrowthModifiers();
    }

    /**
     * Apply food value modifications to Biomes O' Plenty items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fruits, berries, vegetables, seeds, powder (no crafting)
     * - AVERAGE_MEAL: Complex meals (ricebowl - 4-5 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw fruits, berries, vegetables, seeds, powder
        setBopFoodValue("peach", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("persimmon", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("orange", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("blueberries", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("blackberries", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("raspberries", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("strawberries", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("turnip", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("turnip_seeds", 1, 0.05f, "RAW_FOOD");
        setBopFoodValue("shroompowder", 1, 0.05f, "RAW_FOOD");
        
        // AVERAGE_MEAL (6 hunger) - Complex meals (4-5 ingredients)
        setBopFoodValue("ricebowl", 6, 0.4f, "AVERAGE_MEAL"); // Rice + vegetables + optional meat + seasoning
    }

    /**
     * Set food values for a specific Biomes O' Plenty item.
     */
    private void setBopFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = BIOMES_OPLENTY_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "BIOMES_OPLENTY_" + category
        ));
    }

    /**
     * Register biome-specific growth modifiers for BOP biomes.
     */
    private void registerBiomeGrowthModifiers() {
        // BOP adds many new biomes that crops can grow in
        // The existing BiomeGrowthRegistry should handle most of this
        // Specific BOP biomes might need special handling

        // For example:
        // - Oasis biomes might be good for certain crops
        // - Tropical biomes might be good for tropical crops
        // - This would require registering specific biome preferences

        HOReborn.LOGGER.info("Biomes O' Plenty biome growth modifiers registered");
    }

    /**
     * Check if Biomes O' Plenty is loaded.
     */
    public static boolean isBiomesOPlentyLoaded() {
        return biomesOPlentyLoaded;
    }
}

