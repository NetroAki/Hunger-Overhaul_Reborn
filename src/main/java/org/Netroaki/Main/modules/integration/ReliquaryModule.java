package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Reliquary mod.
 * Handles food value modifications for Reliquary items.
 * 
 * Note: Reliquary is primarily an item mod that adds relics and special items,
 * but it may have some food-related items or potions that could be considered food.
 */
public class ReliquaryModule {

    private static final String RELIQUARY_MOD_ID = "reliquary";
    private static boolean reliquaryLoaded = false;

    /**
     * Initialize Reliquary integration.
     */
    public void init() {
        reliquaryLoaded = Platform.isModLoaded(RELIQUARY_MOD_ID);

        if (!reliquaryLoaded) {
            HOReborn.LOGGER.debug("Reliquary not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Reliquary integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Reliquary items.
     * Meal Type Reasoning:
     * - LIGHT_MEAL: Simple crafted bread (flour + water + special ingredient)
     */
    private void applyFoodValueModifications() {
        // LIGHT_MEAL (4 hunger) - Simple crafted bread
        setReliquaryFoodValue("glowing_bread", 4, 0.3f, "LIGHT_MEAL"); // Bread = flour + water + glow ingredient
    }

    /**
     * Set food values for a specific Reliquary item.
     */
    private void setReliquaryFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = RELIQUARY_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "RELIQUARY_" + category
        ));
    }

    /**
     * Check if Reliquary is loaded.
     */
    public static boolean isReliquaryLoaded() {
        return reliquaryLoaded;
    }

    /**
     * Get the Reliquary mod ID.
     */
    public static String getReliquaryModId() {
        return RELIQUARY_MOD_ID;
    }
}

