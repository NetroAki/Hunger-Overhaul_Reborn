package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Integration module for Oh The Trees You'll Grow mod.
 * Handles food value modifications for tree fruits and products.
 */
public class OhTheTreesModule {

    private static final String OH_THE_TREES_MOD_ID = "oh_the_trees_youll_grow";
    private static boolean ohTheTreesLoaded = false;

    /**
     * Initialize Oh The Trees You'll Grow integration.
     */
    public void init() {
        ohTheTreesLoaded = Platform.isModLoaded(OH_THE_TREES_MOD_ID);

        if (!ohTheTreesLoaded) {
            HOReborn.LOGGER.debug("Oh The Trees You'll Grow not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Oh The Trees You'll Grow integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Oh The Trees You'll Grow items.
     * Note: This mod may not be present in all modpacks. Module kept for future compatibility.
     */
    private void applyFoodValueModifications() {
        // Note: Oh The Trees You'll Grow items not found in current modpack dump
        // This module is kept for detection only, future items can be added here
        // When items are added, use FoodRegistry.setFoodValues() to register them
    }

    /**
     * Check if Oh The Trees You'll Grow is loaded.
     */
    public static boolean isOhTheTreesLoaded() {
        return ohTheTreesLoaded;
    }

    /**
     * Get the Oh The Trees You'll Grow mod ID.
     */
    public static String getOhTheTreesModId() {
        return OH_THE_TREES_MOD_ID;
    }
}

