package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Mowzie's Mobs mod.
 * Handles food value modifications for mob drops.
 */
public class MowziesMobsModule {

    private static final String MOWZIES_MOBS_MOD_ID = "mowziesmobs";
    private static boolean mowziesMobsLoaded = false;

    /**
     * Initialize Mowzie's Mobs integration.
     */
    public void init() {
        mowziesMobsLoaded = Platform.isModLoaded(MOWZIES_MOBS_MOD_ID);

        if (!mowziesMobsLoaded) {
            HOReborn.LOGGER.debug("Mowzie's Mobs not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Mowzie's Mobs integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Mowzie's Mobs items.
     * Meal Type Reasoning:
     * - LIGHT_MEAL: Simple processed jelly (mob drop + processing)
     */
    private void applyFoodValueModifications() {
        // LIGHT_MEAL (4 hunger) - Simple processed jelly
        setMowziesMobsFoodValue("glowing_jelly", 4, 0.2f, "LIGHT_MEAL"); // Jelly = mob drop + processing
    }

    /**
     * Set food values for a specific Mowzie's Mobs item.
     */
    private void setMowziesMobsFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = MOWZIES_MOBS_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "MOWZIES_MOBS_" + category
        ));
    }

    /**
     * Check if Mowzie's Mobs is loaded.
     */
    public static boolean isMowziesMobsLoaded() {
        return mowziesMobsLoaded;
    }

    /**
     * Get the Mowzie's Mobs mod ID.
     */
    public static String getMowziesMobsModId() {
        return MOWZIES_MOBS_MOD_ID;
    }
}

