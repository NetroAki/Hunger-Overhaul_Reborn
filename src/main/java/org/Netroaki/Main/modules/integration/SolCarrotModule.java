package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;

/**
 * Integration module for Spice of Life: Carrot Edition mod.
 * Handles food value modifications and food diversity tracking.
 * 
 * Note: Spice of Life: Carrot Edition tracks food diversity but doesn't
 * add new food items. This module is for potential future integration or
 * compatibility with food diversity mechanics.
 */
public class SolCarrotModule {

    private static final String SOL_CARROT_MOD_ID = "solcarrot";
    private static boolean solCarrotLoaded = false;

    /**
     * Initialize Spice of Life: Carrot Edition integration.
     */
    public void init() {
        solCarrotLoaded = Platform.isModLoaded(SOL_CARROT_MOD_ID);

        if (!solCarrotLoaded) {
            HOReborn.LOGGER.debug("Spice of Life: Carrot Edition not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Spice of Life: Carrot Edition integration");

        // Spice of Life: Carrot Edition tracks food diversity
        // It doesn't add new food items, but tracks which foods have been eaten
        // For now, we just log that it's loaded
        // Future integration could adjust food values based on diversity bonuses
    }

    /**
     * Check if Spice of Life: Carrot Edition is loaded.
     */
    public static boolean isSolCarrotLoaded() {
        return solCarrotLoaded;
    }

    /**
     * Get the Spice of Life: Carrot Edition mod ID.
     */
    public static String getSolCarrotModId() {
        return SOL_CARROT_MOD_ID;
    }
}

