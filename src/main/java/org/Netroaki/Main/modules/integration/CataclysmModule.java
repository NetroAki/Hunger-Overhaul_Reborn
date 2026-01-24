package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Cataclysm mod (L_Ender's Cataclysm).
 * Handles food value modifications for Cataclysm mob drops and items.
 */
public class CataclysmModule {

    private static final String CATACLYSM_MOD_ID = "cataclysm";
    private static boolean cataclysmLoaded = false;

    /**
     * Initialize Cataclysm integration.
     */
    public void init() {
        cataclysmLoaded = Platform.isModLoaded(CATACLYSM_MOD_ID);

        if (!cataclysmLoaded) {
            HOReborn.LOGGER.debug("Cataclysm not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Cataclysm integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Cataclysm items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fish/seafood (no crafting)
     * - COOKED_FOOD: Simply cooked seafood (furnace/smoker)
     * - AVERAGE_MEAL: Blessed/processed versions (special crafting)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw seafood
        setCataclysmFoodValue("lionfish", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked seafood
        setCataclysmFoodValue("amethyst_crab_meat", 2, 0.25f, "COOKED_FOOD");
        
        // AVERAGE_MEAL (6 hunger) - Blessed version requires special crafting
        setCataclysmFoodValue("blessed_amethyst_crab_meat", 6, 0.4f, "AVERAGE_MEAL");
    }

    /**
     * Set food values for a specific Cataclysm item.
     */
    private void setCataclysmFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = CATACLYSM_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "CATACLYSM_" + category
        ));
    }

    /**
     * Check if Cataclysm is loaded.
     */
    public static boolean isCataclysmLoaded() {
        return cataclysmLoaded;
    }

    /**
     * Get the Cataclysm mod ID.
     */
    public static String getCataclysmModId() {
        return CATACLYSM_MOD_ID;
    }
}

