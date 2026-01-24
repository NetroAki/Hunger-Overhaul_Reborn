package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;

/**
 * Integration module for CookingForBlockheads mod.
 * 
 * Note: CookingForBlockheads is primarily a UI/crafting mod that doesn't add food items itself.
 * It provides a cooking station and recipe book for food mods. This module is for compatibility
 * and future integration possibilities (e.g., showing food values in the cooking book).
 */
public class CookingForBlockheadsModule {

    private static final String COOKING_FOR_BLOCKHEADS_MOD_ID = "cookingforblockheads";
    private static boolean cookingForBlockheadsLoaded = false;

    /**
     * Initialize CookingForBlockheads integration.
     */
    public void init() {
        cookingForBlockheadsLoaded = Platform.isModLoaded(COOKING_FOR_BLOCKHEADS_MOD_ID);

        if (!cookingForBlockheadsLoaded) {
            HOReborn.LOGGER.debug("CookingForBlockheads not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing CookingForBlockheads integration");

        // CookingForBlockheads doesn't add food items, so we don't need to apply food values
        // Future integration could:
        // - Show food values in the cooking book
        // - Display hunger/saturation in the cooking station UI
        // - Integrate with the recipe system
    }

    /**
     * Check if CookingForBlockheads is loaded.
     */
    public static boolean isCookingForBlockheadsLoaded() {
        return cookingForBlockheadsLoaded;
    }

    /**
     * Get the CookingForBlockheads mod ID.
     */
    public static String getCookingForBlockheadsModId() {
        return COOKING_FOR_BLOCKHEADS_MOD_ID;
    }
}

